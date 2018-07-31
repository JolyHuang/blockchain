package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

@Service
public class TransactionEthServiceImpl extends BaseServiceImpl<TransactionEth, String> implements TransactionEthService {

    private TransactionEthDAO transactionEthDAO;

    public TransactionEthDAO getTransactionEthDAO() {
        return transactionEthDAO;
    }
    @Resource
    public void setTransactionEthDAO(TransactionEthDAO transactionEthDAO) {
        super.setBaseDAO(transactionEthDAO);
        this.transactionEthDAO = transactionEthDAO;
    }

    @Override
    public PaginationRepertory<TransactionEth> getInUntreated(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_UNTREATED);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_IN);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getOutUntreated(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_UNTREATED);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_OUT);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getUnconfirmedBlockNumber(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatusArray(Arrays.asList(
                TransactionEth.TX_STATUS_DEPOSIT_PROCESSING_NOTIFIED
                ,TransactionEth.TX_STATUS_WITHDRAWAL_PROCESSING_NOTIFIED
                ,TransactionEth.TX_STATUS_BLOCK_CONFIRMING
        ));
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return transactionEthDAO.queryPaginationTxStatusInList(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getUnconfirmedBalance(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_BALANCE_UNCONFIRM);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getInInvalid(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_INVALID);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_IN);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getOutInvalid(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_INVALID);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_OUT);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getInValid(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_VALID);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_IN);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getOutValid(PaginationCondition<TransactionEth> paginationCondition) {
        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_VALID);
        paginationCondition.getCondition().setTxType(TransactionEth.TX_TYPE_OUT);
        paginationCondition.getCondition().setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);

        return getPagination(paginationCondition);
    }

    @Override
    public void updateTxStatusToDepositProcessingNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_DEPOSIT_PROCESSING_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToWithdrawalProcessingNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_WITHDRAWAL_PROCESSING_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToBalanceUnconfirm(String txHash, int confirmBlockNumber) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setConfirmBlockNumber(confirmBlockNumber);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_BALANCE_UNCONFIRM);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToBalanceError(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_BALANCE_ERROR);

        updateById(transactionEth);
    }

    @Override
    public void updateConfirmBlockNumber(String txHash, int confirmBlockNumber) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setConfirmBlockNumber(confirmBlockNumber);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_BLOCK_CONFIRMING);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToInvalid(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_INVALID);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToValid(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_VALID);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToDepositSuccessNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_DEPOSIT_SUCCESS_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToWithdrawalSuccessNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_WITHDRAWAL_SUCCESS_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToDepositFailNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_DEPOSIT_FAIL_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTxStatusToWithdrawalFailNotified(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_WITHDRAWAL_FAIL_NOTIFIED);

        updateById(transactionEth);
    }

    @Override
    public void updateTaskStatusToFail(String txHash) {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setTaskStatus(TransactionEth.TASK_STATUS_FAIL);

        updateById(transactionEth);
    }
}
