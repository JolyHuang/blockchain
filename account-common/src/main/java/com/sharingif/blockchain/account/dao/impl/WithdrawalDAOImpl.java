package com.sharingif.blockchain.account.dao.impl;


import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.dao.WithdrawalDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;


@Repository
public class WithdrawalDAOImpl extends BaseDAOImpl<Withdrawal,java.lang.String> implements WithdrawalDAO {
	
}
