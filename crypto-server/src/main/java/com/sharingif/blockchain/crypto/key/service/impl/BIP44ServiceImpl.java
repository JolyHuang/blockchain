package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.api.key.entity.*;
import com.sharingif.blockchain.crypto.app.components.Keystore;
import com.sharingif.blockchain.crypto.app.constants.ErrorConstants;
import com.sharingif.blockchain.crypto.key.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.key.service.BIP44Service;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.security.confidentiality.encrypt.digest.SHA256Encryptor;
import org.bitcoinj.crypto.*;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;

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
@Service("bip44Service")
public class BIP44ServiceImpl implements BIP44Service {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Keystore keystore;
    private SHA256Encryptor sha256Encryptor;

    @Resource
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    @Resource
    public void setSha256Encryptor(SHA256Encryptor sha256Encryptor) {
        this.sha256Encryptor = sha256Encryptor;
    }

    @Override
    public BIP44GenerateRsp generate(BIP44GenerateReq req) {

        return null;
    }

    @Override
    public BIP44ChangeRsp change(BIP44ChangeReq req) {
        String mnemonic = keystore.load(req.getMnemonicFilePath(), req.getMnemonicPassword());
        KeyPath keyPath = new KeyPath(req);

        DeterministicSeed seed;
        try {
            seed = new DeterministicSeed(mnemonic, null, "", MnemonicCode.BIP39_STANDARDISATION_TIME_SECS);
        } catch (UnreadableWalletException e) {
            throw new RuntimeException(ErrorConstants.GENERATE_CHANGE_ERROR, e);
        }
        DeterministicKeyChain chain = DeterministicKeyChain.builder().seed(seed).build();
        List<ChildNumber> childNumberList = HDUtils.parsePath(keyPath.getBitcoinjPath());
        DeterministicKey changeDeterministicKey = chain.getKeyByPath(childNumberList, true);
        String deterministicKeyStr = changeDeterministicKey.serializePrivB58(MainNetParams.get());

        String fileName = sha256Encryptor.encrypt(deterministicKeyStr);

        String filePath = keystore.persistenceExtendedKey(req.getMnemonicId(), keyPath.getPath(), fileName, deterministicKeyStr, req.getPassword());

        BIP44ChangeRsp bip44ChangeRsp = new BIP44ChangeRsp();
        bip44ChangeRsp.setFilePath(filePath);

        return bip44ChangeRsp;
    }

    @Override
    public BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req) {
        KeyPath keyPath = new KeyPath(req);

        String changeDeterministicKeyBase58 = keystore.load(req.getChangeExtendedKeyFilePath(), req.getChangeExtendedKeyPassword());
        DeterministicKey changeDeterministicKey = DeterministicKey.deserializeB58(changeDeterministicKeyBase58, MainNetParams.get());
        changeDeterministicKey = new DeterministicKey(HDUtils.append(HDUtils.parsePath(keyPath.getBitcoinjParentPath()), ChildNumber.ZERO), changeDeterministicKey.getChainCode(), changeDeterministicKey.getPrivKey(), changeDeterministicKey);

        DeterministicKey addressIndexDeterministicKey = HDKeyDerivation.deriveChildKey(changeDeterministicKey, new ChildNumber(req.getAddressIndex(), false));

        BIP44AddressIndexRsp bip44AddressIndexRsp = new BIP44AddressIndexRsp();
        if(BIP44GenerateReq.COIN_TYPE_ETH == req.getCoinType()) {
            ECKeyPair ecKeyPair = ECKeyPair.create(addressIndexDeterministicKey.getPrivKey());
            Credentials credentials = Credentials.create(ecKeyPair);
            bip44AddressIndexRsp.setAddress(credentials.getAddress());

            try {
                String directoryStr = new StringBuilder(keystore.getKeyRootPath())
                        .append("/").append(req.getMnemonicId())
                        .append("/").append(keyPath.getPath())
                        .toString();

                File directory = new File(directoryStr);

                if(!(directory.mkdirs())) {
                    logger.error("create directory error, path:{}", directoryStr);
                    throw new ValidationCubeException(ErrorConstants.GENERATE_ADDRESSINDEX_KEY_ERROR);
                }

                String fileName = WalletUtils.generateWalletFile(req.getPassword(), ecKeyPair, directory, true);

                String filePath = new StringBuilder(directoryStr).append("/").append(fileName).toString();

                bip44AddressIndexRsp.setFilePath(filePath);
            } catch (IOException | CipherException e) {
                logger.error("generate addressIndex key error", e);
                throw new ValidationCubeException(ErrorConstants.GENERATE_ADDRESSINDEX_KEY_ERROR);
            }
        }

        return bip44AddressIndexRsp;
    }

}
