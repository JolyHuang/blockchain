package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

/**
 * TransactionBtcDepositProcessingNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:28
 */
public class TransactionBtcDepositProcessingNoticeServiceImpl extends AbstractTransactionBtcDepositNoticeServiceImpl {


    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getInUntreated(paginationCondition);
    }

    @Override
    void updateTxStatus(String id) {
        getTransactionBtcUtxoService().updateTxStatusToDepositProcessingNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc deposit processing notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("btc deposit processing notice error", e);
    }
}
