package com.sharingif.blockchain.crypto.btc.controller;

import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferRsp;
import com.sharingif.blockchain.crypto.btc.service.BtcService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * BtcController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午3:56
 */
@Controller
@RequestMapping(value="btc")
public class BtcController {

    private BtcService btcService;

    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }

    /**
     * 生成transfer交易
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    public BtcTransferRsp transfer(BtcTransferReq req) {

        return btcService.transfer(req);
    }
}
