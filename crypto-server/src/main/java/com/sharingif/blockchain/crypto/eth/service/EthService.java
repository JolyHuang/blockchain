package com.sharingif.blockchain.crypto.eth.service;

import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferRsp;

/**
 * EthService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:57
 */
public interface EthService {

    /**
     * 转账
     * @param req
     * @return
     */
    EthTransferRsp transfer(EthTransferReq req);

}
