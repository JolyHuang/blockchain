package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.dao.BlockChainSyncDAO;


@Repository
public class BlockChainSyncDAOImpl extends BaseDAOImpl<BlockChainSync,String> implements BlockChainSyncDAO {
	
}
