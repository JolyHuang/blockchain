package com.sharingif.blockchain.transaction.dao.impl;


import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.dao.TransactionBtcUtxoDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;


@Repository
public class TransactionBtcUtxoDAOImpl extends BaseDAOImpl<TransactionBtcUtxo,java.lang.String> implements TransactionBtcUtxoDAO {
	
}
