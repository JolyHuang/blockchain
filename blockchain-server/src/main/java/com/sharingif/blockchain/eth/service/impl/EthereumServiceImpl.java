package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.eth.service.Web3jService;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Ethereum 服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/6 下午6:26
 */
@Service
public class EthereumServiceImpl implements EthereumService {

    private Web3jService web3jService;

    @Resource
    public void setWeb3jService(Web3jService web3jService) {
        this.web3jService = web3jService;
    }

    @Override
    public BigInteger getBalance(String address) {
        EthGetBalance ethGetBalance;
        try {
            ethGetBalance = web3jService.getCurrentAvailableWeb3j().ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        } catch (Exception e) {
            throw new CubeRuntimeException("get balance error", e);
        }

        return ethGetBalance.getBalance();
    }

    @Override
    public Optional<Transaction> getTransactionByHash(String txHash) {
        EthTransaction ethTransaction;
        try {
            ethTransaction = web3jService.getCurrentAvailableWeb3j().ethGetTransactionByHash("0x28d71467b2697ff1b7f628af231fd0992b25b5cf0c24060d54bb60306555f07b").send();
        } catch (IOException e) {
            throw new CubeRuntimeException("get transaction error", e);
        }
        return ethTransaction.getTransaction();
    }

}
