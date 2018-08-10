package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 充值失败通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/9 下午2:54
 */
@Service
public class TransactionBtcDepositFailNoticeServiceImpl extends AbstractTransactionBtcDepositNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        return getTransactionBtcUtxoService().getInInvalid(paginationCondition);
    }

    @Override
    void updateTxStatus(String id) {
        getTransactionBtcUtxoService().updateTxStatusToDepositFailNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        logger.info("btc deposit fail notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("btc deposit fail notice error", e);
    }

}
