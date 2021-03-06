package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 取现处理中通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:03
 */
@Service
public class TransactionEthWithdrawalProcessingNoticeServiceImpl extends AbstractTransactionEthWithdrawalNoticeServiceImpl {


    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getOutUntreated(paginationCondition);
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String id) {
        getTransactionEthService().updateTxStatusToWithdrawalProcessingNotified(id);
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory) {
        logger.info("eth withdrawal processing notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("eth withdrawal processing notice error", e);
    }
}
