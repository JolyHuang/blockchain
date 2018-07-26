package com.sharingif.blockchain.crypto.api.eth.service;

import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

/**
 * EthService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:57
 */
@RequestMapping(value="eth")
public interface EthApiService {

    /**
     * 转账
     * @param req
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    EthTransferRsp transfer(EthTransferReq req);

}
