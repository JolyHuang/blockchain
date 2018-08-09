package com.sharingif.blockchain.btc.service;

import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.RawTransaction;

import java.math.BigInteger;

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
     * 查询地址余额
     * @param address
     * @param confirmations
     * @return
     */
    BigInteger getReceivedByAddress(String address, Integer confirmations);

}
