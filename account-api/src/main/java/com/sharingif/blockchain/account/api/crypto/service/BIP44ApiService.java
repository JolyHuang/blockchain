package com.sharingif.blockchain.account.api.crypto.service;


import com.sharingif.blockchain.account.api.crypto.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44AddressIndexRsp;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.account.api.crypto.entity.BIP44ChangeRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Service;

/**
 * KeyService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:38
 */
@Service("bip44ApiService")
@RequestMapping(value="bip44")
public interface BIP44ApiService {

    /**
     * 生成change ExtendedKey
     * @param req
     * @return
     */
    @RequestMapping(value="change", method= RequestMethod.POST)
    BIP44ChangeRsp change(BIP44ChangeReq req);

    /**
     * 生成addressIndex key
     * @param req
     * @return
     */
    @RequestMapping(value="addressIndex", method= RequestMethod.POST)
    BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req);

}
