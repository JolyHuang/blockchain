package com.sharingif.blockchain.crypto.service;


import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.cube.support.service.base.IBaseService;

/**
 * SecretKeyService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午7:28
 */
public interface SecretKeyService extends IBaseService<SecretKey, String> {

    /**
     * 密码解密
     * @param password
     * @return
     */
    String decryptPassword(String password);

    /**
     * 根据地址查询SecretKey
     * @param address
     * @return
     */
    SecretKey getSecretKeyByAddress(String address);

}
