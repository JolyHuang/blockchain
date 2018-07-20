package com.sharingif.blockchain.transaction.service.impl;


import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
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
    public PaginationRepertory<TransactionEth> getUnconfirmedBlockNumber(PaginationCondition<TransactionEth> paginationCondition) {

        paginationCondition.getCondition().setTxStatusArray(Arrays.asList(TransactionEth.TX_STATUS_UNTREATED, TransactionEth.TX_STATUS_BLOCK_CONFIRMING));

        return transactionEthDAO.queryPaginationTxStatusInList(paginationCondition);
    }

    @Override
    public PaginationRepertory<TransactionEth> getUnconfirmedBalance(PaginationCondition<TransactionEth> paginationCondition) {

        paginationCondition.getCondition().setTxStatus(TransactionEth.TX_STATUS_BALANCE_UNCONFIRM);

        return getPagination(paginationCondition);
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
}
