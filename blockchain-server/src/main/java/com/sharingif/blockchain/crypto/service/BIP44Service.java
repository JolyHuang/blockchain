package com.sharingif.blockchain.crypto.service;


import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexRsp;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeRsp;

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
