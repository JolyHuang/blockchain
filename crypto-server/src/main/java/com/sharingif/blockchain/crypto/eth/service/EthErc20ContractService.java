package com.sharingif.blockchain.crypto.eth.service;

import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferRsp;

/**
 * EthErc20ContractService
 *
 * @author Joly
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/5 下午8:40
 */
public interface EthErc20ContractService {

    /**
     * 转账
     * @param req
     * @return
     */
    Erc20TransferRsp transfer(Erc20TransferReq req);

}
