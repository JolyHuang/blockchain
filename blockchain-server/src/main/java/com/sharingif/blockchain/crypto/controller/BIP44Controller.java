package com.sharingif.blockchain.crypto.controller;

import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44AddressIndexRsp;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeReq;
import com.sharingif.blockchain.api.crypto.entity.BIP44ChangeRsp;
import com.sharingif.blockchain.crypto.service.BIP44Service;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * KeyController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:37
 */
@Controller
@RequestMapping(value="bip44")
public class BIP44Controller {

    private BIP44Service bip44Service;

    @Resource
    public void setBip44Service(BIP44Service bip44Service) {
        this.bip44Service = bip44Service;
    }

    /**
     * 生成change ExtendedKey
     * @return
     */
    @RequestMapping(value="change", method= RequestMethod.POST)
    public BIP44ChangeRsp change(BIP44ChangeReq req) {

        return bip44Service.change(req);
    }

    /**
     * 生成addressIndex key
     * @return
     */
    @RequestMapping(value="addressIndex", method= RequestMethod.POST)
    public BIP44AddressIndexRsp addressIndex(BIP44AddressIndexReq req) {

        return bip44Service.addressIndex(req);
    }

}
