package com.sharingif.blockchain.transaction.dao.impl;


import com.sharingif.blockchain.app.dao.impl.BaseDAOImpl;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;


@Repository
public class TransactionEthDAOImpl extends BaseDAOImpl<TransactionEth,String> implements TransactionEthDAO {
	
}
