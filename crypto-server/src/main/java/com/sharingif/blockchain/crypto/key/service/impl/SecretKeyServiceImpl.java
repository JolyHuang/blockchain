package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.app.constants.ErrorConstants;
import com.sharingif.blockchain.crypto.key.dao.SecretKeyDAO;
import com.sharingif.blockchain.crypto.key.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.key.service.SecretKeyService;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;

import javax.annotation.Resource;

/**
 * SecretKeyServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:41
 */
@Service
public class SecretKeyServiceImpl extends BaseServiceImpl<SecretKey, String> implements SecretKeyService {

    private SecretKeyDAO secretKeyDAO;

    @Resource
    public void setSecretKeyDAO(SecretKeyDAO secretKeyDAO) {
        super.setBaseDAO(secretKeyDAO);
        this.secretKeyDAO = secretKeyDAO;
    }

    @Override
    public Credentials getCredentials(String secretKeyId, String password) {
        SecretKey secretKey = secretKeyDAO.queryById(secretKeyId);

        return getCredentials(secretKey, password);
    }

    @Override
    public Credentials getCredentials(SecretKey secretKey, String password) {
        try {
            Credentials credentials = WalletUtils.loadCredentials(
                    password
                    ,secretKey.getFilePath()
            );

            return credentials;
        } catch (Exception e) {
            logger.error("load credentials error", e);
            throw new ValidationCubeException(ErrorConstants.PASSWORD_ERROR);
        }
    }
}
