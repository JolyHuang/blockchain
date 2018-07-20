package com.sharingif.blockchain.account.service.impl;


import javax.annotation.Resource;

import com.sharingif.blockchain.account.dao.AccountJnlDAO;
import com.sharingif.blockchain.account.model.entity.AccountJnl;
import org.springframework.stereotype.Service;

import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.sharingif.blockchain.account.service.AccountJnlService;

import java.math.BigInteger;
import java.util.Date;

@Service
public class AccountJnlServiceImpl extends BaseServiceImpl<AccountJnl, java.lang.String> implements AccountJnlService {
	
	private AccountJnlDAO accountJnlDAO;

	public AccountJnlDAO getAccountJnlDAO() {
		return accountJnlDAO;
	}
	@Resource
	public void setAccountJnlDAO(AccountJnlDAO accountJnlDAO) {
		super.setBaseDAO(accountJnlDAO);
		this.accountJnlDAO = accountJnlDAO;
	}


	@Override
	public void in(String from, String to, String coinType, String txHash, Date txTime, BigInteger balance) {
		AccountJnl accountJnl = new AccountJnl();
		accountJnl.setAccountFrom(from);
		accountJnl.setAccountTo(to);
		accountJnl.setCoinType(coinType);
		accountJnl.setBalance(balance);
		accountJnl.setType(AccountJnl.TYPE_IN);
		accountJnl.setTxHash(txHash);
		accountJnl.setTransTime(txTime);

		accountJnlDAO.insert(accountJnl);
	}

	@Override
	public void out(String from, String to, String coinType, String txHash, Date txTime, BigInteger balance) {
		AccountJnl accountJnl = new AccountJnl();
		accountJnl.setAccountFrom(from);
		accountJnl.setAccountTo(to);
		accountJnl.setCoinType(coinType);
		accountJnl.setBalance(balance);
		accountJnl.setType(AccountJnl.TYPE_OUT);
		accountJnl.setTxHash(txHash);
		accountJnl.setTransTime(txTime);

		accountJnlDAO.insert(accountJnl);
	}
}
