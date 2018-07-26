package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

/**
 * 取现处理中通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:03
 */
public class TransactionEthWithdrawalProcessingNoticeServiceImpl extends AbstractTransactionEthWithdrawalNoticeServiceImpl {


    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getOutUntreated(paginationCondition);
    }

    @Override
    void updateTxStatus(String txHash) {
        getTransactionEthService().updateTxStatusToWithdrawalProcessingNotified(txHash);
    }

    @Override
    protected void sendNotice(TransactionEth transactionEth) {

    }
}
