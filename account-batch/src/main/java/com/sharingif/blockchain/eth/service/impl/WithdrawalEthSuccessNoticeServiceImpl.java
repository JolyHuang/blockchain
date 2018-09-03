package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

/**
 * WithdrawalEthSuccessNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/9/3 上午10:52
 */
public class WithdrawalEthSuccessNoticeServiceImpl extends WithdrawalEthFailNoticeServiceImpl {

    @Override
    protected String getTxStatus() {
        return TransactionEth.TX_STATUS_VALID;
    }

    @Override
    protected void updateTxStatus(Withdrawal withdrawal) {
        getWithdrawalService().updateStatusToSuccessNotified(withdrawal.getId());
    }

    @Override
    protected PaginationRepertory<Withdrawal> getPaginationRepertory(PaginationCondition<Withdrawal> paginationCondition) {
        return getWithdrawalService().getEthStatusSuccess(paginationCondition);
    }
}
