package com.sharingif.blockchain.transaction.service.impl;


import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.dao.BlockChainSyncDAO;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.sharingif.blockchain.transaction.service.BlockChainSyncService;

import java.math.BigInteger;

@Service
public class BlockChainSyncServiceImpl extends BaseServiceImpl<BlockChainSync, java.lang.String> implements BlockChainSyncService {
	
	private BlockChainSyncDAO blockChainSyncDAO;

	public BlockChainSyncDAO getBlockChainSyncDAO() {
		return blockChainSyncDAO;
	}
	@Resource
	public void setBlockChainSyncDAO(BlockChainSyncDAO blockChainSyncDAO) {
		super.setBaseDAO(blockChainSyncDAO);
		this.blockChainSyncDAO = blockChainSyncDAO;
	}


	@Override
	public BlockChainSync getETHBlockChainSync() {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_ETH);

		return blockChainSyncDAO.query(blockChainSync);
	}

	@Override
	public void setETHBlockChainCurrentNumber(BigInteger currentBlockNumber) {
		BlockChainSync queryBlockChainSync = getETHBlockChainSync();

		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setId(queryBlockChainSync.getId());
		blockChainSync.setCurrentSyncBlockNumber(currentBlockNumber);

		blockChainSyncDAO.updateById(blockChainSync);
	}

	@Override
	public void addETHBlockChainSync(BigInteger blockNumber) {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_ETH);

		blockChainSyncDAO.insert(blockChainSync);
	}
}
