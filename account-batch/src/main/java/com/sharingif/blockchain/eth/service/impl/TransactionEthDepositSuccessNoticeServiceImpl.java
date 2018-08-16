package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 充值成功通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午6:28
 */
@Service
public class TransactionEthDepositSuccessNoticeServiceImpl extends AbstractTransactionEthDepositNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getInValid(paginationCondition);
    }

    @Override
    void updateTxStatus(String id) {
        getTransactionEthService().updateTxStatusToDepositSuccessNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory) {
        logger.info("eth deposit success notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("eth deposit success notice error", e);
    }

}
