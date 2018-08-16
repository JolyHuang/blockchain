package com.sharingif.blockchain.account.service.impl;


import javax.annotation.Resource;

import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.common.constants.CoinTypeConvert;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import org.springframework.stereotype.Service;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.dao.WithdrawalDAO;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.sharingif.blockchain.account.service.WithdrawalService;

@Service
public class WithdrawalServiceImpl extends BaseServiceImpl<Withdrawal, java.lang.String> implements WithdrawalService {
	
	private WithdrawalDAO withdrawalDAO;
	private AddressNoticeService addressNoticeService;
	private AccountSysPrmService accountSysPrmService;
	private BtcService btcService;
	private SecretKeyService secretKeyService;
	private AccountService accountService;

	public WithdrawalDAO getWithdrawalDAO() {
		return withdrawalDAO;
	}
	@Resource
	public void setWithdrawalDAO(WithdrawalDAO withdrawalDAO) {
		super.setBaseDAO(withdrawalDAO);
		this.withdrawalDAO = withdrawalDAO;
	}
	@Resource
	public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
		this.addressNoticeService = addressNoticeService;
	}
	@Resource
	public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
		this.accountSysPrmService = accountSysPrmService;
	}
	@Resource
	public void setBtcService(BtcService btcService) {
		this.btcService = btcService;
	}
	@Resource
	public void setSecretKeyService(SecretKeyService secretKeyService) {
		this.secretKeyService = secretKeyService;
	}
	@Resource
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Override
	public void apply(WithdrawalApplyReq req) {
		validateWithdrawalAccountBalance(req);

		// 添加支付记录
		Withdrawal withdrawal = Withdrawal.convertWithdrawalReqToWithdrawal(req);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
		withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);
		if(CoinType.OLE.name().equals(req.getCoinType())) {
			withdrawal.setCoinType(CoinType.ETH.name());
			withdrawal.setSubCoinType(req.getCoinType());
		}
		withdrawalDAO.insert(withdrawal);

		// 添加通知记录
		addressNoticeService.registerWithdrawalAddressNotice(
				withdrawal.getId()
				,req.getNoticeAddress()
				,req.getAddress()
				,req.getCoinType()
		);

	}

	/**
	 * 验证验证余额是否足够
	 * @param req
	 */
	protected void validateWithdrawalAccountBalance(WithdrawalApplyReq req) {
		String coinType = req.getCoinType();
		if(CoinType.OLE.name().equals(req.getCoinType())) {
			coinType = CoinType.ETH.name();
		}
		int bipCoinType = CoinTypeConvert.convertToBipCoinType(coinType);
		if(CoinType.BTC.name().equals(coinType)) {
			bipCoinType = btcService.getBipCoinType();
		}

		String secretKeyId = accountSysPrmService.getWithdrawalAccount(bipCoinType);
		SecretKey secretKey = secretKeyService.getById(secretKeyId);
		Account account = accountService.getNormalAccountByAddress(secretKey.getAddress(), req.getCoinType());
		if(account.getBalance().compareTo(req.getAmount()) < 0) {
			throw new ValidationCubeException("insufficient balance");
		}
	}

}
