package com.sharingif.blockchain.account.service.impl;


import com.sharingif.blockchain.account.dao.AccountDAO;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, java.lang.String> implements AccountService {
	
	private AccountDAO accountDAO;

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}
	@Resource
	public void setAccountDAO(AccountDAO accountDAO) {
		super.setBaseDAO(accountDAO);
		this.accountDAO = accountDAO;
	}


	@Override
	public Account getNormalAccountByAddress(String address) {
		Account queryAccount = new Account();
		queryAccount.setStatus(Account.STATUS_NORMAL);
		queryAccount.setAddress(address);

		return accountDAO.query(queryAccount);
	}

	@Override
	public Account getOutLockAccountByAddress(String address) {
		Account queryAccount = new Account();
		queryAccount.setStatus(Account.STATUS_NORMAL);
		queryAccount.setAddress(address);

		return accountDAO.query(queryAccount);
	}

	@Override
	public void updateBalance(String id, BigInteger balance) {
		Account updateAccount = new Account();
		updateAccount.setId(id);
		updateAccount.setStatus(Account.STATUS_NORMAL);

		accountDAO.updateById(updateAccount);
	}

	@Override
	public void inBalance(String id, String from, String to, String coinType, String txHash, BigInteger balance, BigInteger inBalance) {

	}

	@Override
	public void outBalance(String id, String from, String to, String coinType, String txHash, BigInteger balance, BigInteger outBalance) {

	}

}
