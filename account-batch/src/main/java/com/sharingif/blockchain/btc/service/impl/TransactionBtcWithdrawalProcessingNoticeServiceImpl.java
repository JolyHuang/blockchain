package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

/**
 * 取现处理中通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午4:32
 */
public class TransactionBtcWithdrawalProcessingNoticeServiceImpl extends AbstractTransactionBtcWithdrawalNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getOutUntreated(paginationCondition);
    }

    @Override
    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionBtcUtxo transactionBtcUtxo) {
        // 处理中不需要发送通知
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String id) {
        getTransactionBtcUtxoService().updateTxStatusToWithdrawalProcessingNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc withdrawal processing notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("btc withdrawal processing notice error", e);
    }
}
