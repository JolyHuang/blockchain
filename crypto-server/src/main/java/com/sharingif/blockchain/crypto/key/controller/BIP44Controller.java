package com.sharingif.blockchain.crypto.key.controller;

import com.sharingif.blockchain.crypto.api.key.*;
import com.sharingif.blockchain.crypto.key.service.BIP44Service;
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
     * 生成key
     * @return
     */
    @RequestMapping(value="generate", method= RequestMethod.POST)
    public BIP44GenerateRsp generate(BIP44GenerateReq req) {

        return null;
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
