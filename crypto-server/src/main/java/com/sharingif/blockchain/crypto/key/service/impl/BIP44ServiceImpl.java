package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.api.key.entity.*;
import com.sharingif.blockchain.crypto.api.key.service.BIP44ApiService;
import com.sharingif.blockchain.crypto.app.components.Keystore;
import com.sharingif.blockchain.crypto.app.constants.ErrorConstants;
import com.sharingif.blockchain.crypto.key.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.key.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.key.model.entity.ExtendedKey;
import com.sharingif.blockchain.crypto.key.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.key.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.key.service.BIP44Service;
import com.sharingif.blockchain.crypto.mnemonic.service.MnemonicService;
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
    private ExtendedKeyDAO extendedKeyDAO;
    private SecretKeyDAO secretKeyDAO;
    private MnemonicService mnemonicService;

    @Resource
    public void setKeystore(Keystore keystore) {
        this.keystore = keystore;
    }

    @Resource
    public void setSha256Encryptor(SHA256Encryptor sha256Encryptor) {
        this.sha256Encryptor = sha256Encryptor;
    }
    @Resource
    public void setExtendedKeyDAO(ExtendedKeyDAO extendedKeyDAO) {
        this.extendedKeyDAO = extendedKeyDAO;
    }
    @Resource
    public void setSecretKeyDAO(SecretKeyDAO secretKeyDAO) {
        this.secretKeyDAO = secretKeyDAO;
    }
    @Resource
    public void setMnemonicService(MnemonicService mnemonicService) {
        this.mnemonicService = mnemonicService;
    }

    @Override
    public BIP44GenerateRsp generate(BIP44GenerateReq req) {

        return null;
    }

    @Override
    public BIP44ChangeRsp change(BIP44ChangeReq req) {

        // BIP44路径
        KeyPath keyPath = new KeyPath(req);

        // 获取助记词文件路径
        String mnemonicFilePath = mnemonicService.getFilePath(req.getMnemonicId());

        String mnemonic = keystore.load(mnemonicFilePath, req.getMnemonicPassword());

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

        String mnemonicPath = mnemonicFilePath.split("/")[0];
        String filePath = keystore.persistenceExtendedKey(mnemonicPath, keyPath.getPath(), fileName, deterministicKeyStr, req.getPassword());

        // 保存助记词信息
        ExtendedKey extendedKey = new ExtendedKey();
        extendedKey.setMnemonicId(req.getMnemonicId());
        extendedKey.setExtendedKeyPath(keyPath.getPath());
        extendedKey.setFilePath(filePath);
        extendedKeyDAO.insert(extendedKey);

        // 返回助记词id
        BIP44ChangeRsp rsp = new BIP44ChangeRsp();
        rsp.setId(extendedKey.getId());

        return rsp;
    }

    protected SecretKey addressIndexETH(DeterministicKey addressIndexDeterministicKey, ExtendedKey extendedKey, KeyPath keyPath, String password) {
        SecretKey secretKey = new SecretKey();

        ECKeyPair ecKeyPair = ECKeyPair.create(addressIndexDeterministicKey.getPrivKey());
        Credentials credentials = Credentials.create(ecKeyPair);
        try {
            String mnemonicPath = extendedKey.getFilePath().split("/")[0];
            String directoryStr = new StringBuilder(keystore.getKeyRootPath())
                    .append("/").append(mnemonicPath)
                    .append("/").append(keyPath.getPath())
                    .toString();

            File directory = new File(directoryStr);

            if(!(directory.mkdirs())) {
                logger.error("create directory error, path:{}", directoryStr);
                throw new ValidationCubeException(ErrorConstants.GENERATE_ADDRESSINDEX_KEY_ERROR);
            }

            String fileName = WalletUtils.generateWalletFile(password, ecKeyPair, directory, true);

            String filePath = new StringBuilder(directoryStr).append("/").append(fileName).toString();

            secretKey.setAddress(credentials.getAddress());
            secretKey.setFilePath(filePath);

            return secretKey;
        } catch (IOException | CipherException e) {
            logger.error("generate addressIndex key error", e);
            throw new ValidationCubeException(ErrorConstants.GENERATE_ADDRESSINDEX_KEY_ERROR);
        }
    }

    protected SecretKey addressIndexBTC(DeterministicKey addressIndexDeterministicKey) {
        SecretKey secretKey = new SecretKey();

        secretKey.setAddress(addressIndexDeterministicKey.toAddress(MainNetParams.get()).toBase58());

        return secretKey;
    }

    @Override
    public BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req) {
        // 获取change ExtendedKey信息
        ExtendedKey extendedKey = extendedKeyDAO.queryById(req.getChangeExtendedKeyId());
        Integer addressIndex = extendedKey.getCurrentIndexNumber();
        if(addressIndex == null) {
            addressIndex = 0;
        } else {
            ++addressIndex;
        }
        ExtendedKey updateExtendedKey = new ExtendedKey();
        updateExtendedKey.setId(extendedKey.getId());
        updateExtendedKey.setCurrentIndexNumber(addressIndex);
        extendedKeyDAO.updateById(updateExtendedKey);

        // BIP44路径
        KeyPath keyPath = new KeyPath(req, addressIndex);

        String changeDeterministicKeyBase58 = keystore.load(extendedKey.getFilePath(), req.getChangeExtendedKeyPassword());
        DeterministicKey changeDeterministicKey = DeterministicKey.deserializeB58(changeDeterministicKeyBase58, MainNetParams.get());
        changeDeterministicKey = new DeterministicKey(HDUtils.append(HDUtils.parsePath(keyPath.getBitcoinjParentPath()), ChildNumber.ZERO), changeDeterministicKey.getChainCode(), changeDeterministicKey.getPrivKey(), changeDeterministicKey);

        DeterministicKey addressIndexDeterministicKey = HDKeyDerivation.deriveChildKey(changeDeterministicKey, new ChildNumber(addressIndex, false));

        SecretKey secretKey = null;
        if(BIP44GenerateReq.COIN_TYPE_ETH == req.getCoinType()) {
            secretKey = addressIndexETH(addressIndexDeterministicKey, extendedKey, keyPath, req.getPassword());
        }

        if(BIP44GenerateReq.COIN_TYPE_BTC == req.getCoinType()) {
            secretKey = addressIndexBTC(addressIndexDeterministicKey);
        }

        // 保存key信息
        secretKey.setMnemonicId(req.getMnemonicId());
        secretKey.setExtendedKeyId(extendedKey.getId());
        secretKey.setKeyPath(keyPath.getPath());
        secretKeyDAO.insert(secretKey);

        BIP44AddressIndexRsp rsp = new BIP44AddressIndexRsp();
        rsp.setId(secretKey.getId());
        rsp.setAddress(secretKey.getAddress());

        return rsp;
    }

}
