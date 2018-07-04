package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.api.key.*;
import com.sharingif.blockchain.crypto.app.components.Keystore;
import com.sharingif.blockchain.crypto.key.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.key.service.BIP44Service;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.core.util.UUIDUtils;
import org.bitcoinj.crypto.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * KeyServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:38
 */
@Service("bip44ServiceImpl")
public class BIP44ServiceImpl implements BIP44Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Keystore keystore;

    @Resource
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    @Override
    public BIP44GenerateRsp generate(BIP44GenerateReq req) {

        return null;
    }

    @Override
    public BIP44ChangeRsp change(BIP44ChangeReq req) {
        String mnemonic = keystore.loadMnemonic(req.getMnemonicId(), req.getMnemonicFileName(), req.getMnemonicPassword());
        KeyPath keyPath = new KeyPath(req);

        DeterministicSeed seed;
        try {
            seed = new DeterministicSeed(mnemonic, null, "", MnemonicCode.BIP39_STANDARDISATION_TIME_SECS);
        } catch (UnreadableWalletException e) {
            throw new RuntimeException("generate change error", e);
        }
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> childNumberList = HDUtils.parsePath(keyPath.getBitcoinjPath());
        DeterministicKey changeDeterministicKey = chain.getKeyByPath(childNumberList, true);

        BIP44ChangeRsp bip44ChangeRsp = new BIP44ChangeRsp();
        bip44ChangeRsp.setExtendedKeyId(UUIDUtils.generateUUID());
        bip44ChangeRsp.setFileName(new StringBuilder(bip44ChangeRsp.getExtendedKeyId()).append(".").append(Keystore.EXTENDEDKEY_FILE_POSTFIX).toString());

        String deterministicKeyStr = changeDeterministicKey.serializePrivB58(MainNetParams.get());
        keystore.persistenceExtendedKey(req.getMnemonicId(), keyPath.getPath(), bip44ChangeRsp.getFileName(), deterministicKeyStr, req.getPassword());


        return bip44ChangeRsp;
    }

    @Override
    public BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req) {
        KeyPath keyPath = new KeyPath(req);

        String changeDeterministicKeyBase58 = keystore.loadExtendedKey(req.getMnemonicId(), keyPath.getParentPath(), req.getChangeExtendedKeyFileName(), req.getChangeExtendedKeyPassword());
        DeterministicKey changeDeterministicKey = DeterministicKey.deserializeB58(changeDeterministicKeyBase58, MainNetParams.get());
        changeDeterministicKey = new DeterministicKey(HDUtils.append(HDUtils.parsePath(keyPath.getBitcoinjParentPath()), ChildNumber.ZERO), changeDeterministicKey.getChainCode(), changeDeterministicKey.getPrivKey(), changeDeterministicKey);

        DeterministicKey addressIndexDeterministicKey = HDKeyDerivation.deriveChildKey(changeDeterministicKey, new ChildNumber(0, false));

        BIP44AddressIndexRsp bip44AddressIndexRsp = new BIP44AddressIndexRsp();
        bip44AddressIndexRsp.setKeyId(UUIDUtils.generateUUID());
        if(BIP44GenerateReq.COIN_TYPE_ETH.equals(req.getCoinType())) {
            ECKeyPair ecKeyPair = ECKeyPair.create(addressIndexDeterministicKey.getPrivKey());
            Credentials credentials = Credentials.create(ecKeyPair);
            bip44AddressIndexRsp.setAddress(credentials.getAddress());

            try {
                File filePath = new File(new StringBuilder(keystore.getKeyRootPath())
                        .append("/").append(req.getMnemonicId())
                        .append("/").append(keyPath.getPath())
                        .toString()
                );
                String fileName = WalletUtils.generateWalletFile(req.getPassword(), ecKeyPair, filePath, true);
                bip44AddressIndexRsp.setFileName(fileName);
            } catch (IOException | CipherException e) {
                logger.error("generate addressIndex key error", e);
                throw new ValidationCubeException("generate addressIndex key error");
            }
        }

        return bip44AddressIndexRsp;
    }

}
