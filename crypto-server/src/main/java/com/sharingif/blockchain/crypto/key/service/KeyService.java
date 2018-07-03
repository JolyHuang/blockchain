package com.sharingif.blockchain.crypto.key.service;

import com.sharingif.blockchain.crypto.api.key.KeyGenerateReq;
import com.sharingif.blockchain.crypto.api.key.KeyGenerateRsp;

/**
 * KeyService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:38
 */
public interface KeyService {

    /**
     * 生成key(bip44标准)
     * @param req
     * @return
     */
    KeyGenerateRsp bip44(KeyGenerateReq req);

}
