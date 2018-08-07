package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;


public interface BlockChainSyncService extends IBaseService<BlockChainSync, String> {

    /**
     * 查询BTC区块同步信息
     * @return
     */
    BlockChainSync getBTCBlockChainSync();

    /**
     * 查询ETH区块同步信息
     * @return
     */
    BlockChainSync getETHBlockChainSync();

    /**
     * 设置BTC当前区块数
     * @param currentBlockNumber
     */
    void setBTCBlockChainCurrentNumber(BigInteger currentBlockNumber);

    /**
     * 设置ETH当前区块数
     * @param currentBlockNumber
     */
    void setETHBlockChainCurrentNumber(BigInteger currentBlockNumber);

    /**
     * 添加BTC区块同步信息
     * @param blockNumber
     */
    void addBTCBlockChainSync(BigInteger blockNumber);

    /**
     * 添加ETH区块同步信息
     * @param blockNumber
     */
    void addETHBlockChainSync(BigInteger blockNumber);

}
