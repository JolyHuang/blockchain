package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 取现成功通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午4:54
 */
@Service
public class TransactionBtcWithdrawalSuccessNoticeServiceImpl extends AbstractTransactionBtcWithdrawalNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getOutValid(paginationCondition);
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String id) {
        getTransactionBtcUtxoService().updateTxStatusToWithdrawalSuccessNotified(id);

        Withdrawal queryWithdrawal = getWithdrawalService().getById(id);
        if(queryWithdrawal !=null) {
            getWithdrawalService().updateStatusToSuccess(withdrawal.getId());
        }
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc withdrawal success notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("btc withdrawal success notice error", e);
    }
}
