package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 充值成功通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/9 下午2:49
 */
@Service
public class TransactionBtcDepositSuccessNoticeServiceImpl extends AbstractTransactionBtcDepositNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getInValid(paginationCondition);
    }

    @Override
    void updateTxStatus(String id) {
        getTransactionBtcUtxoService().updateTxStatusToDepositSuccessNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc deposit success notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("btc deposit success notice error", e);
    }

}
