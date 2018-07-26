package com.sharingif.blockchain.account.service.impl;


import javax.annotation.Resource;

import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import org.springframework.stereotype.Service;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.dao.WithdrawalDAO;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import com.sharingif.blockchain.account.service.WithdrawalService;

@Service
public class WithdrawalServiceImpl extends BaseServiceImpl<Withdrawal, java.lang.String> implements WithdrawalService {
	
	private WithdrawalDAO withdrawalDAO;
	private AddressNoticeService addressNoticeService;

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

	@Override
	public void apply(WithdrawalApplyReq req) {
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

}
