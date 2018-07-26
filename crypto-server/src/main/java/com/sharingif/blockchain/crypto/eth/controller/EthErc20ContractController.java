package com.sharingif.blockchain.crypto.eth.controller;

import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferRsp;
import com.sharingif.blockchain.crypto.eth.service.EthErc20ContractService;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 * EthErc20ContractController
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 上午11:52
 */
@Controller
@RequestMapping(value="erc20")
public class EthErc20ContractController {

    private EthErc20ContractService ethErc20ContractService;

    @Resource
    public void setEthErc20ContractService(EthErc20ContractService ethErc20ContractService) {
        this.ethErc20ContractService = ethErc20ContractService;
    }

    /**
     * 生成transfer交易
     * @return
     */
    @RequestMapping(value="transfer", method= RequestMethod.POST)
    public Erc20TransferRsp transfer(Erc20TransferReq req) {

        return ethErc20ContractService.transfer(req);
    }

}
