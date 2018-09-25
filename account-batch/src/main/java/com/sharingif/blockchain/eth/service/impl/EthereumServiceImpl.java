package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

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

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private Web3j web3j;

    @Override
    public Web3j getWeb3j() {
        return web3j;
    }

    @Override
    public BigInteger getBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber();
        } catch (IOException e) {
            logger.error("get block number error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public EthBlock.Block getBlock(BigInteger blockNumber, boolean returnFullTransactionObjects) {
        try {
            return web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(blockNumber), returnFullTransactionObjects).send().getBlock();
        } catch (IOException e) {
            logger.error("get block number error", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public TransactionReceipt getTransactionReceipt(String transactionHash) {
        try {
            return web3j.ethGetTransactionReceipt(transactionHash).send().getTransactionReceipt().get();
        } catch (IOException e) {
            logger.error("get transaction receipt error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Resource
    public void setWeb3j(Web3j web3j) {
        this.web3j = web3j;
    }

    @Override
    public BigInteger getBalance(String address) {
        EthGetBalance ethGetBalance;
        try {
            ethGetBalance = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).send();
        } catch (Exception e) {
            logger.error("get balance error", e);
            throw new CubeRuntimeException(e);
        }

        return ethGetBalance.getBalance();
    }

    @Override
    public Transaction getTransactionByHash(String txHash) {
        try {
            return web3j.ethGetTransactionByHash(txHash).send().getTransaction().get();
        } catch (IOException e) {
            logger.error("get transaction error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public BigInteger getGasPrice() {
        try {
            return web3j.ethGasPrice().send().getGasPrice();
        } catch (IOException e) {
            logger.error("get gas price error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public String ethSendRawTransaction(String hexValue) {
        try {
            return web3j.ethSendRawTransaction(hexValue).sendAsync().get().getTransactionHash();
        } catch (Exception e) {
            logger.error("eth sendRawTransaction error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public BigInteger ethGetTransactionCountPending(String address) {
        EthGetTransactionCount ethGetTransactionCountPending = null;
        EthGetTransactionCount ethGetTransactionCountLatest = null;
        try {
            ethGetTransactionCountPending = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).sendAsync().get();
            ethGetTransactionCountLatest = web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (Exception e) {
            logger.error("ethGetTransactionCountPending error", e);
            throw new CubeRuntimeException(e);
        }

        BigInteger noncePending = ethGetTransactionCountPending.getTransactionCount();
        BigInteger nonceLatest = ethGetTransactionCountLatest.getTransactionCount();

        if(noncePending.compareTo(nonceLatest) > 0) {
            return noncePending;
        } else {
            return nonceLatest;
        }
    }

}
