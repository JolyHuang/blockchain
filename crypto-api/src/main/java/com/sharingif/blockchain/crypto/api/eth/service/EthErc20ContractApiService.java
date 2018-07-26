package com.sharingif.blockchain.crypto.api.eth.service;

import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

/**
 * EthErc20ContractService
 *
 * @author Joly
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/5 下午8:40
 */
@RequestMapping(value="erc20")
public interface EthErc20ContractApiService {

    /**
     * 转账
     * @param req
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    Erc20TransferRsp transfer(Erc20TransferReq req);

}
