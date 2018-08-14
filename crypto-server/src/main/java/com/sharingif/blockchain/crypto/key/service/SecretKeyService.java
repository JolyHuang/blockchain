package com.sharingif.blockchain.crypto.key.service;

import com.sharingif.blockchain.crypto.key.model.entity.SecretKey;
import com.sharingif.cube.support.service.base.IBaseService;
import org.web3j.crypto.Credentials;

/**
 * SecretKeyService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:40
 */
public interface SecretKeyService extends IBaseService<SecretKey, String> {

    /**
     * 获取Credentials
     * @param secretKeyId
     * @param password
     * @return
     */
    Credentials getCredentials(String secretKeyId, String password);

    /**
     * 获取Credentials
     * @param secretKey
     * @param password
     * @return
     */
    Credentials getCredentials(SecretKey secretKey, String password);

}
