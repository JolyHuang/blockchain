package com.sharingif.blockchain.crypto.key.service;

import com.sharingif.blockchain.crypto.api.key.entity.*;

/**
 * KeyService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:38
 */
public interface BIP44Service {

    /**
     * 生成key(bip44标准)
     * @param req
     * @return
     */
    BIP44GenerateRsp generate(BIP44GenerateReq req);

    /**
     * 生成change ExtendedKey
     * @param req
     * @return
     */
    BIP44ChangeRsp change(BIP44ChangeReq req);

    /**
     * 生成addressIndex key
     * @param req
     * @return
     */
    BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req);

}
