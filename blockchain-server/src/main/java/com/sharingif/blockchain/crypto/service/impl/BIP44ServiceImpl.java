package com.sharingif.blockchain.crypto.service.impl;

import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexRsp;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeRsp;
import com.sharingif.blockchain.crypto.api.key.service.BIP44ApiService;
import com.sharingif.blockchain.crypto.dao.ExtendedKeyDAO;
import com.sharingif.blockchain.crypto.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.model.entity.ExtendedKey;
import com.sharingif.blockchain.crypto.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.BIP44Service;
import com.sharingif.blockchain.crypto.service.MnemonicService;
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

    @Override
    public BIP44ChangeRsp change(BIP44ChangeReq req) {

        // BIP44路径
        KeyPath keyPath = new KeyPath(req);

        // 获取助记词文件路径
        String mnemonicFilePath = mnemonicService.getFilePath(req.getMnemonicId());

        // 调用crypto-api生成change ExtendedKey
        com.sharingif.blockchain.crypto.api.key.entity.BIP44ChangeReq apiReq = new com.sharingif.blockchain.crypto.api.key.entity.BIP44ChangeReq();
        apiReq.setMnemonicId(req.getMnemonicId());
        apiReq.setCoinType(req.getCoinType());
        apiReq.setAccount(req.getAccount());
        apiReq.setChange(req.getChange());
        apiReq.setMnemonicPassword(req.getMnemonicPassword());
        apiReq.setMnemonicFilePath(mnemonicFilePath);
        apiReq.setPassword(req.getPassword());
        com.sharingif.blockchain.crypto.api.key.entity.BIP44ChangeRsp apiRsp = bip44ApiService.change(apiReq);

        // 保存助记词信息
        ExtendedKey extendedKey = new ExtendedKey();
        extendedKey.setMnemonicId(req.getMnemonicId());
        extendedKey.setExtendedKeyPath(keyPath.getPath());
        extendedKey.setFilePath(apiRsp.getFilePath());
        extendedKeyDAO.insert(extendedKey);

        // 返回助记词id
        BIP44ChangeRsp rsp = new BIP44ChangeRsp();
        rsp.setId(extendedKey.getId());

        return rsp;
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

        // 调用crypto-api生成key
        com.sharingif.blockchain.crypto.api.key.entity.BIP44AddressIndexReq apiReq = new com.sharingif.blockchain.crypto.api.key.entity.BIP44AddressIndexReq();
        apiReq.setMnemonicId(req.getMnemonicId());
        apiReq.setCoinType(req.getCoinType());
        apiReq.setAccount(req.getAccount());
        apiReq.setChange(req.getChange());
        apiReq.setAddressIndex(addressIndex);
        apiReq.setChangeExtendedKeyPassword(req.getChangeExtendedKeyPassword());
        apiReq.setChangeExtendedKeyFilePath(extendedKey.getFilePath());
        apiReq.setPassword(req.getPassword());
        com.sharingif.blockchain.crypto.api.key.entity.BIP44AddressIndexRsp apiRsp = bip44ApiService.addressIndex(apiReq);

        // 保存key信息
        SecretKey secretKey = new SecretKey();
        secretKey.setMnemonicId(req.getMnemonicId());
        secretKey.setExtendedKeyId(extendedKey.getId());
        secretKey.setAddress(apiRsp.getAddress());
        secretKey.setKeyPath(keyPath.getPath());
        secretKey.setFilePath(apiRsp.getFilePath());
        secretKeyDAO.insert(secretKey);

        BIP44AddressIndexRsp rsp = new BIP44AddressIndexRsp();
        rsp.setId(secretKey.getId());
        rsp.setAddress(secretKey.getAddress());

        return rsp;
    }

}
