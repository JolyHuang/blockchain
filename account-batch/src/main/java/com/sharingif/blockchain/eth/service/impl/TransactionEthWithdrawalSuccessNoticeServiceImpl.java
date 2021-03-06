package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 取现成功通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:03
 */
@Service
public class TransactionEthWithdrawalSuccessNoticeServiceImpl extends AbstractTransactionEthWithdrawalNoticeServiceImpl {


    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getOutValid(paginationCondition);
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String id) {
        getTransactionEthService().updateTxStatusToWithdrawalSuccessNotified(id);

        if(withdrawal != null) {
            getWithdrawalService().updateStatusToSuccess(withdrawal.getId());
        }
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory) {
        logger.info("eth withdrawal success notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("eth withdrawal success notice error", e);
    }

}
