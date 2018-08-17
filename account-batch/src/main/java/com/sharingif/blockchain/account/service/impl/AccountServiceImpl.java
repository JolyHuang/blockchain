package com.sharingif.blockchain.account.service.impl;


import com.sharingif.blockchain.account.dao.AccountDAO;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.service.AccountJnlService;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
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
	public void freezingBalance(String accountId, BigInteger balance) {
		int number = accountDAO.updateFreezingBalanceById(accountId, balance);

		if(number == 0) {
			logger.error("insufficient balance, accountId:{},balance:{}", accountId, balance);
			throw new ValidationCubeException("insufficient balance");
		}
	}

	@Override
	public void freezingBalance(String address, String coinType, BigInteger balance) {
		Account account =  getNormalAccountByAddress(address, coinType);
		freezingBalance(account.getId(), balance);
	}

	@Override
	public void unfreezeBalance(String accountId, BigInteger balance) {
		int number = accountDAO.updateUnfreezeBalanceById(accountId, balance);

		if(number == 0) {
			logger.error("insufficient freeze balance, accountId:{},balance:{}", accountId, balance);
			throw new ValidationCubeException("insufficient freeze balance");
		}
	}

	@Override
	public void unfreezeBalance(String address, String coinType, BigInteger balance) {
		Account account =  getNormalAccountByAddress(address, coinType);
		unfreezeBalance(account.getId(), balance);
	}

	@Override
	public void inBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance) {
		// 添加账户流水
		accountJnlService.in(from, to, coinType, txHash, txTime, balance);

		// 修改账号表
		accountDAO.updateTotalInAndBalanceById(id, balance);
	}

	@Override
	public void outBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee) {
		// 添加账户流水
		accountJnlService.out(from, to, coinType, txHash, txTime, balance);
		accountJnlService.out(from, to, coinType, txHash, txTime, actualFee);

		// 修改账号表
		BigInteger totalBalance = balance.add(actualFee);
		int number = accountDAO.updateTotalOutAndFrozenAmountById(id, totalBalance);
		if(number == 0) {
			logger.error("insufficient freeze balance, accountId:{},balance:{}", id, balance);
			throw new ValidationCubeException("insufficient freeze balance");
		}
	}

	@Override
	public void outContractBalance(String ethAccountId, String contractAccountId, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger withdrawalFee, BigInteger actualFee) {
		// 添加账户流水
		accountJnlService.out(from, to, coinType, txHash, txTime, balance);
		accountJnlService.out(from, to, CoinType.ETH.name(), txHash, txTime, actualFee);

		// 修改ETH账号表
		int number = accountDAO.updateTotalOutFrozenAmountBalanceById(ethAccountId, actualFee, withdrawalFee, withdrawalFee.subtract(actualFee));
		if(number == 0) {
			logger.error("insufficient freeze balance, accountId:{},balance:{}", ethAccountId, balance);
			throw new ValidationCubeException("insufficient freeze balance");
		}

		// 修改contract账号表
		number = accountDAO.updateTotalOutAndFrozenAmountById(contractAccountId, balance);
		if(number == 0) {
			logger.error("insufficient freeze balance, accountId:{},balance:{}", contractAccountId, balance);
			throw new ValidationCubeException("insufficient freeze balance");
		}
	}


}
