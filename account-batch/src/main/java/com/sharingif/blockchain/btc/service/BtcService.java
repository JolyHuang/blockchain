package com.sharingif.blockchain.btc.service;

import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.RawTransaction;

import java.math.BigInteger;
import java.util.List;

/**
 * BtcService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/7 下午12:37
 */
public interface BtcService {

    /**
     * 查询bip币种类型
     * @return
     */
    int getBipCoinType();

    /**
     * 获取区块数
     * @return
     */
    Integer getBlockCount();

    /**
     * 根据区块数获取区块hash
     * @param blockCount
     * @return
     */
    String getBlockHash(Integer blockCount);

    /**
     * 根据区块数获取区块数据
     * @param blockCount
     * @return
     */
    Block getBlock(Integer blockCount);

    /**
     * 根据区块hash获取区块数据
     * @param blockhash
     * @return
     */
    Block getBlock(String blockhash);

    /**
     * 根据交易id获取交易信息
     * @param txId
     * @return
     */
    RawTransaction getRawTransaction(String txId);

    /**
     * 查询接收金额
     * @param address
     * @param confirmations
     * @return
     */
    BigInteger getReceivedByAddress(String address, Integer confirmations);

    /**
     * 根据地址查询余额
     * @param address
     * @return
     */
    BigInteger getBalanceByAddress(String address);

    /**
     * 签名交易
     * @param rawTransaction
     * @return
     */
    String signRawTransaction(String rawTransaction);

    /**
     * 发送签名交易
     * @param signRawTransaction
     * @return
     */
    String sendRawTransaction(String signRawTransaction);

    /**
     * 获取指定额度的output
     * @param address
     * @param fee
     * @param amount
     * @return
     */
    List<Output> getListUnspent(String address, BigInteger amount, BigInteger fee);

}
