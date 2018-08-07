package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.BlockChainSyncDAO;
import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.service.BlockChainSyncService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class BlockChainSyncServiceImpl extends BaseServiceImpl<BlockChainSync, String> implements BlockChainSyncService {
	
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
	public BlockChainSync getBTCBlockChainSync() {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_BTC);

		return blockChainSyncDAO.query(blockChainSync);
	}

	@Override
	public BlockChainSync getETHBlockChainSync() {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_ETH);

		return blockChainSyncDAO.query(blockChainSync);
	}

	@Override
	public void setBTCBlockChainCurrentNumber(BigInteger currentBlockNumber) {
		BlockChainSync queryBlockChainSync = getBTCBlockChainSync();

		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setId(queryBlockChainSync.getId());
		blockChainSync.setCurrentSyncBlockNumber(currentBlockNumber);

		blockChainSyncDAO.updateById(blockChainSync);
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
	public void addBTCBlockChainSync(BigInteger blockNumber) {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_BTC);

		blockChainSyncDAO.insert(blockChainSync);
	}

	@Override
	public void addETHBlockChainSync(BigInteger blockNumber) {
		BlockChainSync blockChainSync = new BlockChainSync();
		blockChainSync.setBlockChainType(BlockChainSync.BLOCK_CHAIN_TYPE_ETH);

		blockChainSyncDAO.insert(blockChainSync);
	}
}
