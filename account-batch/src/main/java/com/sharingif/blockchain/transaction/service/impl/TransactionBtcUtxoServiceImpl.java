package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.transaction.dao.TransactionBtcUtxoDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * TransactionBtcUtxoServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/7 下午4:34
 */
@Service
public class TransactionBtcUtxoServiceImpl extends BaseServiceImpl<TransactionBtcUtxo, String> implements TransactionBtcUtxoService {

    private TransactionBtcUtxoDAO transactionBtcUtxoDAO;

    @Resource
    public void setTransactionBtcUtxoDAO(TransactionBtcUtxoDAO transactionBtcUtxoDAO) {
        super.setBaseDAO(transactionBtcUtxoDAO);
        this.transactionBtcUtxoDAO = transactionBtcUtxoDAO;
    }


    @Override
    public TransactionBtcUtxo getTransactionBtcUtxo(String txHash, BigInteger blockNumber) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setTxHash(txHash);
        transactionBtcUtxo.setBlockNumber(blockNumber);

        return transactionBtcUtxoDAO.query(transactionBtcUtxo);
    }

    @Override
    public PaginationRepertory<TransactionBtcUtxo> getInUntreated(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionBtcUtxo.TX_STATUS_UNTREATED);
        paginationCondition.getCondition().setTxType(TransactionBtcUtxo.TX_TYPE_IN);
        paginationCondition.getCondition().setTaskStatus(TransactionBtcUtxo.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionBtcUtxo> getUnconfirmedBlockNumber(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        paginationCondition.getCondition().setTxStatusArray(Arrays.asList(
                TransactionBtcUtxo.TX_STATUS_DEPOSIT_PROCESSING_NOTIFIED
                ,TransactionBtcUtxo.TX_STATUS_WITHDRAWAL_PROCESSING_NOTIFIED
                ,TransactionBtcUtxo.TX_STATUS_BLOCK_CONFIRMING
        ));
        paginationCondition.getCondition().setTaskStatus(TransactionBtcUtxo.TASK_STATUS_UNTREATED);

        return transactionBtcUtxoDAO.queryPaginationTxStatusInList(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionBtcUtxo> getUnconfirmedBalance(PaginationCondition<TransactionBtcUtxo> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionBtcUtxo.TX_STATUS_BALANCE_UNCONFIRM);
        paginationCondition.getCondition().setTaskStatus(TransactionBtcUtxo.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public void updateTxStatusToDepositProcessingNotified(String id) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setId(id);
        transactionBtcUtxo.setTxStatus(TransactionBtcUtxo.TX_STATUS_DEPOSIT_PROCESSING_NOTIFIED);

        updateById(transactionBtcUtxo);
    }

    @Override
    public void updateConfirmBlockNumber(String id, int confirmBlockNumber) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setId(id);
        transactionBtcUtxo.setConfirmBlockNumber(confirmBlockNumber);
        transactionBtcUtxo.setTxStatus(TransactionBtcUtxo.TX_STATUS_BLOCK_CONFIRMING);

        updateById(transactionBtcUtxo);
    }

    @Override
    public void updateTxStatusToBalanceUnconfirm(String id, int confirmBlockNumber) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setId(id);
        transactionBtcUtxo.setConfirmBlockNumber(confirmBlockNumber);
        transactionBtcUtxo.setTxStatus(TransactionBtcUtxo.TX_STATUS_BALANCE_UNCONFIRM);

        updateById(transactionBtcUtxo);
    }

    @Override
    public void updateTxStatusToInvalid(String id) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setId(id);
        transactionBtcUtxo.setTxStatus(TransactionBtcUtxo.TX_STATUS_INVALID);

        updateById(transactionBtcUtxo);
    }

    @Override
    public void updateTaskStatusToFail(String id) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setId(id);
        transactionBtcUtxo.setTaskStatus(TransactionBtcUtxo.TASK_STATUS_FAIL);

        transactionBtcUtxoDAO.updateById(transactionBtcUtxo);
    }
}
