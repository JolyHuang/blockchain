package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.eth.EthereumService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * TransactionEthBalanceConfirmServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/18 下午4:24
 */
public class TransactionEthBalanceConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionEthService transactionEthService;
    private EthereumService ethereumService;
    private AccountService accountService;

    @Resource
    public void setTransactionEthService(TransactionEthService transactionEthService) {
        this.transactionEthService = transactionEthService;
    }
    @Resource
    public void setEthereumService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    protected void in(TransactionEth transactionEth) {
        String address= transactionEth.getTxTo();
        Account account = accountService.getNormalAccountByAddress(address);

        if(account == null) {
            return;
        }
        BigInteger blockBalance = ethereumService.getBalance(address);
        BigInteger currentBalance = account.getBalance().add(transactionEth.getTxValue());
        if(currentBalance.compareTo(blockBalance) == 0) {
            transactionEthService.updateTxStatusToValid(transactionEth.getTxHash());
            accountService.inBalance(
                    account.getId()
                    ,transactionEth.getTxFrom()
                    ,transactionEth.getTxTo()
                    ,transactionEth.getCoinType()
                    ,transactionEth.getTxHash()
                    ,transactionEth.getTxTime()
                    ,currentBalance
                    ,blockBalance
            );
        } else {
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getTxHash());
        }

    }

    protected void out(TransactionEth transactionEth) {
        String address = transactionEth.getTxFrom();
        Account account = accountService.getOutLockAccountByAddress(address);

        if(account == null) {
            return;
        }
        BigInteger blockBalance = ethereumService.getBalance(address);
        BigInteger currentBalance = account.getBalance().subtract(transactionEth.getTxValue());
        if(currentBalance.compareTo(blockBalance) == 0) {
            transactionEthService.updateTxStatusToValid(transactionEth.getTxHash());
            accountService.outBalance(
                    account.getId()
                    ,transactionEth.getTxFrom()
                    ,transactionEth.getTxTo()
                    ,transactionEth.getCoinType()
                    ,transactionEth.getTxHash()
                    ,transactionEth.getTxTime()
                    ,currentBalance
                    ,blockBalance
            );
        } else {
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getTxHash());
        }
    }

    protected void confirmTransactionEthBalance(PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            String txType = transactionEth.getTxType();
            if(TransactionEth.TX_TYPE_IN.equals(txType)) {
                in(transactionEth);
            } else {
                out(transactionEth);
            }

        }
    }

    protected void confirmTransactionEthBalance() {
        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            PaginationRepertory<TransactionEth> paginationRepertory = transactionEthService.getUnconfirmedBalance(paginationCondition);

            if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                try {
                    TimeUnit.SECONDS.sleep(120);
                } catch (InterruptedException e) {
                    logger.error("get unconfirmed balance error", e);
                }
            }

            confirmTransactionEthBalance(paginationRepertory);

            if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                paginationCondition.setCurrentPage(1);
            } else {
                paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
