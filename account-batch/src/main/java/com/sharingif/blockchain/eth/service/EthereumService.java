package com.sharingif.blockchain.eth.service;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

/**
 * Ethereum 服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/6 下午6:23
 */
public interface EthereumService {

    /**
     * Web3j
     * @return
     */
    Web3j getWeb3j();

    /**
     * 获得最新区块号
     * @return
     */
    BigInteger getBlockNumber();

    /**
     * 获取区块信息
     * @param blockNumber
     * @param returnFullTransactionObjects
     * @return
     */
    EthBlock.Block getBlock(BigInteger blockNumber, boolean returnFullTransactionObjects);

    /**
     * 获取交易收据
     * @param transactionHash
     * @return
     */
    TransactionReceipt getTransactionReceipt(String transactionHash) throws IOException;

    /**
     * 查询eth余额
     * @param address
     * @return
     */
    BigInteger getBalance(String address);

    /**
     * 根据交易hash查询交易详情
     * @param txHash
     * @return
     */
    Transaction getTransactionByHash(String txHash);

    /**
     * 获得gas价格
     * @return
     */
    BigInteger getGasPrice();

    /***
     * 发送raw交易
     * @param hexValue
     */
    String ethSendRawTransaction(String hexValue);

    /**
     * 获取交易唯一编号
     * @return
     */
    BigInteger ethGetTransactionCountPending(String address);

}
