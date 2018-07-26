package com.sharingif.blockchain.crypto.service.impl;

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

    private TextEncryptor passwordTextEncryptor;

    @Resource
    public void setPasswordTextEncryptor(TextEncryptor passwordTextEncryptor) {
        this.passwordTextEncryptor = passwordTextEncryptor;
    }

    @Override
    public String decryptPassword(String password) {
        return passwordTextEncryptor.decrypt(password);
    }

}
