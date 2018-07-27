package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

/**
 * 充值失败通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午6:28
 */
@Service
public class TransactionEthDepositFailNoticeServiceImpl extends AbstractTransactionEthDepositNoticeServiceImpl {

    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getInInvalid(paginationCondition);
    }

    @Override
    void updateTxStatus(String txHash) {
        getTransactionEthService().updateTxStatusToDepositFailNotified(txHash);
    }

}
