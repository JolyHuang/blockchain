package com.sharingif.blockchain.crypto.eth.controller;

import com.sharingif.blockchain.crypto.api.key.entity.BIP44GenerateReq;
import com.sharingif.blockchain.crypto.api.key.entity.BIP44GenerateRsp;
import com.sharingif.blockchain.crypto.eth.service.EthService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * EthController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:58
 */
@Controller
@RequestMapping(value="eth")
public class EthController {

    private EthService ethService;

    @Resource
    public void setEthService(EthService ethService) {
        this.ethService = ethService;
    }

    /**
     * 生成transfer交易
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    public BIP44GenerateRsp transfer(BIP44GenerateReq req) {

        return null;
    }

}
