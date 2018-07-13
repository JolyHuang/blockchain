package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;


public interface BlockChainSyncService extends IBaseService<BlockChainSync, java.lang.String> {

    /**
     * 查询eth区块同步信息
     * @return
     */
    BlockChainSync getETHBlockChainSync();

    /**
     * 设置当前区块数
     * @param currentBlockNumber
     */
    void setETHBlockChainCurrentNumber(BigInteger currentBlockNumber);

    /**
     * 查询eth区块同步信息
     * @param blockNumber
     */
    void addETHBlockChainSync(BigInteger blockNumber);

}
