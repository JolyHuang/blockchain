package com.sharingif.blockchain.account.service.impl;


import com.sharingif.blockchain.account.dao.WithdrawalDAO;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;

@Service
public class WithdrawalServiceImpl extends BaseServiceImpl<Withdrawal, String> implements WithdrawalService {
	
	private WithdrawalDAO withdrawalDAO;

	public WithdrawalDAO getWithdrawalDAO() {
		return withdrawalDAO;
	}
	@Resource
	public void setWithdrawalDAO(WithdrawalDAO withdrawalDAO) {
		super.setBaseDAO(withdrawalDAO);
		this.withdrawalDAO = withdrawalDAO;
	}

	@Override
	public PaginationRepertory<Withdrawal> getEthUntreated(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.ETH.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public PaginationRepertory<Withdrawal> getBtcUntreated(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.BTC.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public PaginationRepertory<Withdrawal> getEthStatusSuccess(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.ETH.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public PaginationRepertory<Withdrawal> getBtcStatusSuccess(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.BTC.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public PaginationRepertory<Withdrawal> getEthStatusFial(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.ETH.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public PaginationRepertory<Withdrawal> getBtcStatusFial(PaginationCondition<Withdrawal> paginationCondition) {
		paginationCondition.getCondition().setCoinType(CoinType.BTC.name());
		paginationCondition.getCondition().setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL);
		paginationCondition.getCondition().setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		return getPagination(paginationCondition);
	}

	@Override
	public void updateStatusToProcessing(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_PROCESSING);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateStatusToSuccess(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateStatusToFail(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateTaskStatusToProcessing(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_PROCESSING);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateTaskStatusToFail(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_FAIL);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateStatusToProcessingAndTaskStatusToUntreatedAndTxHash(String id, String txHash) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setTxHash(txHash);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_PROCESSING);
		withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateStatusToSuccessNotified(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_SUCCESS_NOTIFIED);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateStatusToFailNotified(String id) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_FAIL_NOTIFIED);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public Withdrawal getWithdrawalByTxHash(String txHash) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setTxHash(txHash);

		return withdrawalDAO.query(withdrawal);
	}

	@Override
	public void updateFee(String id, BigInteger fee) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setFee(fee);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public void updateFeeAndFrozenAmount(String id, BigInteger fee, BigInteger frozenAmount) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setId(id);
		withdrawal.setFee(fee);
		withdrawal.setFrozenAmount(frozenAmount);

		withdrawalDAO.updateById(withdrawal);
	}

	@Override
	public List<Withdrawal> getUntreatedStatusByTxFrom(String txFrom) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setTxFrom(txFrom);
		withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);

		return withdrawalDAO.queryList(withdrawal);
	}

}
