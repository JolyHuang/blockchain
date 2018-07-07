package com.sharingif.blockchain.account.dao.impl;


import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.dao.AccountDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;


@Repository
public class AccountDAOImpl extends BaseDAOImpl<Account,java.lang.String> implements AccountDAO {
	
}
