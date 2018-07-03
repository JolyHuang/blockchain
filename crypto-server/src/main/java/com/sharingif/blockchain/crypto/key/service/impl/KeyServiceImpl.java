package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.api.key.KeyGenerateReq;
import com.sharingif.blockchain.crypto.api.key.KeyGenerateRsp;
import com.sharingif.blockchain.crypto.key.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.key.service.KeyService;

/**
 * KeyServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:38
 */
public class KeyServiceImpl implements KeyService {

    @Override
    public KeyGenerateRsp bip44(KeyGenerateReq req) {

        KeyPath keyPath = new KeyPath(req);



        return null;
    }

}
