package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * WithdrawalBtcSuccessNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/9/3 下午1:14
 */
@Service
public class WithdrawalBtcSuccessNoticeServiceImpl extends WithdrawalBtcFailNoticeServiceImpl {

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
        return getWithdrawalService().getBtcStatusSuccess(paginationCondition);
    }

}
