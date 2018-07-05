package com.sharingif.blockchain.eth.service;

import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.Transaction;

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
    Optional<Transaction> getTransactionByHash(String txHash);

}
