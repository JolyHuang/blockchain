package com.sharingif.blockchain.account.service.impl;


import com.sharingif.blockchain.account.dao.AccountDAO;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.service.AccountJnlService;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;

@Service
public class AccountServiceImpl extends BaseServiceImpl<Account, java.lang.String> implements AccountService {
	
	private AccountDAO accountDAO;
	private AccountJnlService accountJnlService;

	public AccountDAO getAccountDAO() {
		return accountDAO;
	}
	@Resource
	public void setAccountDAO(AccountDAO accountDAO) {
		super.setBaseDAO(accountDAO);
		this.accountDAO = accountDAO;
	}
	@Resource
	public void setAccountJnlService(AccountJnlService accountJnlService) {
		this.accountJnlService = accountJnlService;
	}

	@Override
	public void initAccount(String coinType, String address) {
		Account account = new Account();
		account.setAddress(address);
		account.setCoinType(coinType);
		account.setTotalIn(BigInteger.ZERO);
		account.setTotalOut(BigInteger.ZERO);
		account.setBalance(BigInteger.ZERO);
		account.setStatus(Account.STATUS_NORMAL);

		accountDAO.insert(account);
	}

	@Override
	public Account getNormalAccountByAddress(String address, String coinType) {
		Account queryAccount = new Account();
		queryAccount.setStatus(Account.STATUS_NORMAL);
		queryAccount.setCoinType(coinType);
		queryAccount.setAddress(address);

		return accountDAO.query(queryAccount);
	}

	@Override
	public Account getOutLockAccountByAddress(String address, String coinType) {
		Account queryAccount = new Account();
		queryAccount.setStatus(Account.STATUS_TRANSFOR_OUT_LOCK);
		queryAccount.setCoinType(coinType);
		queryAccount.setAddress(address);

		return accountDAO.query(queryAccount);
	}

	@Override
	public void inBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance) {
		// 添加账户流水
		accountJnlService.in(from, to, coinType, txHash, txTime, balance);

		// 修改账号表
		Account queryAccount = accountDAO.queryById(id);

		Account updateAccount = new Account();
		updateAccount.setId(id);
		updateAccount.setBalance(queryAccount.getBalance().add(balance));
		updateAccount.setTotalIn(queryAccount.getTotalIn().add(balance));

		accountDAO.updateById(updateAccount);
	}

	@Override
	public void outBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee) {
		// 添加账户流水
		accountJnlService.out(from, to, coinType, txHash, txTime, balance);
		accountJnlService.out(from, to, coinType, txHash, txTime, actualFee);

		// 修改账号表
		Account queryAccount = accountDAO.queryById(id);

		BigInteger totalBalance = balance.add(actualFee);

		Account updateAccount = new Account();
		updateAccount.setId(id);
		updateAccount.setBalance(queryAccount.getBalance().subtract(totalBalance));
		updateAccount.setTotalOut(queryAccount.getTotalOut().add(totalBalance));

		accountDAO.updateById(updateAccount);

	}

	@Override
	public void outBalance(String ethAccountId, String contractAccountId, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee) {
		// 添加账户流水
		accountJnlService.out(from, to, coinType, txHash, txTime, balance);
		accountJnlService.out(from, to, CoinType.ETH.name(), txHash, txTime, actualFee);

		// 修改ETH账号表
		Account queryETHAccount = accountDAO.queryById(ethAccountId);
		Account updateETHAccount = new Account();
		updateETHAccount.setId(ethAccountId);
		updateETHAccount.setBalance(queryETHAccount.getBalance().subtract(actualFee));
		updateETHAccount.setTotalOut(queryETHAccount.getTotalOut().add(actualFee));
		accountDAO.updateById(updateETHAccount);

		// 修改contract账号表
		Account queryContractAccount = accountDAO.queryById(contractAccountId);
		Account updateContractETHAccount = new Account();
		updateContractETHAccount.setId(contractAccountId);
		updateContractETHAccount.setBalance(queryContractAccount.getBalance().subtract(balance));
		updateContractETHAccount.setTotalOut(queryContractAccount.getTotalOut().add(balance));
		accountDAO.updateById(updateContractETHAccount);
	}


}
