package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 取现失败通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午5:00
 */
@Service
public class TransactionBtcWithdrawalFailNoticeServiceImpl extends AbstractTransactionBtcWithdrawalNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getOutInvalid(paginationCondition);
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String txHash) {
        getTransactionBtcUtxoService().updateTxStatusToWithdrawalFailNotified(txHash);
        getWithdrawalService().updateStatusToFail(withdrawal.getId());
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc withdrawal fail notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("eth withdrawal fail notice error", e);
    }
}
