package com.sharingif.blockchain.crypto.service.impl;

import com.sharingif.blockchain.crypto.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.cube.security.confidentiality.encrypt.TextEncryptor;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * SecretKeyServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午7:28
 */
@Service
public class SecretKeyServiceImpl extends BaseServiceImpl<SecretKey, String> implements SecretKeyService {

    private SecretKeyDAO secretKeyDAO;
    private TextEncryptor passwordTextEncryptor;

    @Resource
    public void setSecretKeyDAO(SecretKeyDAO secretKeyDAO) {
        this.setBaseDAO(secretKeyDAO);
        this.secretKeyDAO = secretKeyDAO;
    }
    @Resource
    public void setPasswordTextEncryptor(TextEncryptor passwordTextEncryptor) {
        this.passwordTextEncryptor = passwordTextEncryptor;
    }

    @Override
    public String decryptPassword(String password) {
        return passwordTextEncryptor.decrypt(password);
    }

    @Override
    public SecretKey getSecretKeyByAddress(String address) {
        SecretKey secretKey = new SecretKey();
        secretKey.setAddress(address);

        return secretKeyDAO.query(secretKey);
    }

}
