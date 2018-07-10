package com.sharingif.blockchain.crypto.service.impl;

import com.sharingif.blockchain.account.api.crypto.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44AddressIndexRsp;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeRsp;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.api.crypto.service.BIP44ApiService;
import com.sharingif.blockchain.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.api.transaction.service.AddressRegisterApiService;
import com.sharingif.blockchain.crypto.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.model.entity.ExtendedKey;
import com.sharingif.blockchain.crypto.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.model.entity.Mnemonic;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.BIP44Service;
import com.sharingif.blockchain.crypto.service.MnemonicService;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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

    private ExtendedKeyDAO extendedKeyDAO;
    private SecretKeyDAO secretKeyDAO;
    private MnemonicService mnemonicService;
    private BIP44ApiService bip44ApiService;
    private AccountSysPrmService accountSysPrmService;
    private AddressRegisterApiService addressRegisterApiService;
    private TextEncryptor passwordTextEncryptor;

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
    @Resource
    public void setBip44ApiService(BIP44ApiService bip44ApiService) {
        this.bip44ApiService = bip44ApiService;
    }
    @Resource
    public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
        this.accountSysPrmService = accountSysPrmService;
    }
    @Resource
    public void setAddressRegisterApiService(AddressRegisterApiService addressRegisterApiService) {
        this.addressRegisterApiService = addressRegisterApiService;
    }
    @Resource
    public void setPasswordTextEncryptor(TextEncryptor passwordTextEncryptor) {
        this.passwordTextEncryptor = passwordTextEncryptor;
    }

    @Override
    public BIP44ChangeRsp change(BIP44ChangeReq req) {

        // 查询密码
        Mnemonic mnemonic = mnemonicService.getById(req.getMnemonicId());
        String mnemonicPassword = passwordTextEncryptor.decrypt(mnemonic.getPassword());

        // 调用crypto-api生成change ExtendedKey
        com.sharingif.blockchain.api.crypto.entity.BIP44ChangeReq apiReq = new com.sharingif.blockchain.api.crypto.entity.BIP44ChangeReq();
        apiReq.setMnemonicId(req.getMnemonicId());
        apiReq.setCoinType(req.getCoinType());
        apiReq.setAccount(req.getAccount());
        apiReq.setChange(req.getChange());
        apiReq.setMnemonicPassword(mnemonicPassword);
        apiReq.setPassword(req.getPassword());
        com.sharingif.blockchain.api.crypto.entity.BIP44ChangeRsp apiRsp = bip44ApiService.change(apiReq);

        // BIP44路径
        KeyPath keyPath = new KeyPath(apiReq);

        // 保存助记词信息
        ExtendedKey extendedKey = new ExtendedKey();
        extendedKey.setId(apiRsp.getId());
        extendedKey.setMnemonicId(req.getMnemonicId());
        extendedKey.setExtendedKeyPath(keyPath.getPath());
        extendedKey.setPassword(passwordTextEncryptor.encrypt(req.getPassword()));
        extendedKeyDAO.insert(extendedKey);

        // 返回助记词id
        BIP44ChangeRsp rsp = new BIP44ChangeRsp();
        rsp.setId(extendedKey.getId());

        return rsp;
    }

    @Override
    public BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req) {

        // 获取配置ExtendedKeyId
        String changeExtendedKeyId = accountSysPrmService.extendedKey(req.getCoinType());

        // 获取change ExtendedKey信息
        ExtendedKey extendedKey = extendedKeyDAO.queryById(changeExtendedKeyId);
        String extendedKeyPassword = passwordTextEncryptor.decrypt(extendedKey.getPassword());


        // BIP44路径
        KeyPath keyPath = new KeyPath(extendedKey.getExtendedKeyPath());

        // 调用crypto-api生成key
        com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexReq apiReq = new com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexReq();
        apiReq.setMnemonicId(extendedKey.getMnemonicId());
        apiReq.setCoinType(keyPath.getCoinType());
        apiReq.setAccount(keyPath.getAccount());
        apiReq.setChange(keyPath.getChange());
        apiReq.setChangeExtendedKeyId(extendedKey.getId());
        apiReq.setChangeExtendedKeyPassword(extendedKeyPassword);
        apiReq.setPassword(extendedKeyPassword);
        com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexRsp apiRsp = bip44ApiService.addressIndex(apiReq);

        // 保存key信息
        SecretKey secretKey = new SecretKey();
        secretKey.setId(apiRsp.getId());
        secretKey.setMnemonicId(extendedKey.getMnemonicId());
        secretKey.setExtendedKeyId(extendedKey.getId());
        secretKey.setAddress(apiRsp.getAddress());
        secretKey.setPassword(extendedKeyPassword);
        secretKeyDAO.insert(secretKey);

        // 返回值
        BIP44AddressIndexRsp rsp = new BIP44AddressIndexRsp();
        rsp.setId(secretKey.getId());
        rsp.setAddress(secretKey.getAddress());

        // 注册地址监听
        RegisterReq registerReq = new RegisterReq();
        registerReq.setCoinType(req.getCoinType());
        registerReq.setAddress(rsp.getAddress());
        registerReq.setNoticeList(req.getNoticeList());
        addressRegisterApiService.register(registerReq);
        if(BIP44AddressIndexReq.COIN_TYPE_ETH.equals(req.getCoinType())) {
            registerReq.setCoinType("OLE");
            addressRegisterApiService.register(registerReq);
        }

        return rsp;
    }

}
