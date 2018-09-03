package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TransactionBtcBalanceConfirmServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午7:35
 */
@Service
public class TransactionBtcBalanceConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionBtcUtxoService transactionBtcUtxoService;
    private AccountService accountService;
    private BtcService btcService;
    private WithdrawalService withdrawalService;
    private DataSourceTransactionManager dataSourceTransactionManager;

    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    protected void in(TransactionBtcUtxo transactionBtcUtxo) {
        // 如果入账前还有未处理的出账入账暂不处理
        List<TransactionBtcUtxo> withdrawalTransactionBtcUtxoList = transactionBtcUtxoService.getWithdrawalTransactionBtcUtxo(transactionBtcUtxo.getTxTo());
        if(withdrawalTransactionBtcUtxoList != null) {
            for(TransactionBtcUtxo withdrawalTransactionBtcUtxo : withdrawalTransactionBtcUtxoList) {
                String withdrawalTxStatus = withdrawalTransactionBtcUtxo.getTxStatus();
                if(TransactionBtcUtxo.TX_STATUS_INVALID.equals(withdrawalTxStatus)
                        &&TransactionBtcUtxo.TX_STATUS_VALID.equals(withdrawalTxStatus)
                        &&TransactionBtcUtxo.TX_STATUS_WITHDRAWAL_SUCCESS_NOTIFIED.equals(withdrawalTxStatus)
                        &&TransactionBtcUtxo.TX_STATUS_WITHDRAWAL_FAIL_NOTIFIED.equals(withdrawalTxStatus)
                ) {
                    return;
                }
            }
        }

        String address = transactionBtcUtxo.getTxTo();
        Account account = accountService.getNormalAccountByAddress(address, CoinType.BTC.name());

        if(account == null) {
            logger.error("btc in comfirm balance get mormal account error, transactionBtcUtxo:{}", transactionBtcUtxo);
            transactionBtcUtxoService.updateTxStatusToBalanceError(transactionBtcUtxo.getId());
        }

        BigInteger blockBalance = btcService.getReceivedByAddress(address, transactionBtcUtxo.getConfirmBlockNumber());
        BigInteger txBalance = transactionBtcUtxo.getTxValue();
        BigInteger currentBalance = account.getBalance().add(txBalance);

        if(currentBalance.compareTo(blockBalance) <= 0) {
            accountService.inBalance(
                    account.getId()
                    ,transactionBtcUtxo.getTxFrom()
                    ,transactionBtcUtxo.getTxTo()
                    ,CoinType.BTC.name()
                    ,transactionBtcUtxo.getId()
                    ,transactionBtcUtxo.getTxTime()
                    ,txBalance
            );
            transactionBtcUtxoService.updateTxStatusToValid(transactionBtcUtxo.getId());

        } else {
            logger.error("btc comfirm balance error, transactionBtcUtxo:{},account:{},currentBalance:{},blockBalance:{}",transactionBtcUtxo, account, currentBalance, blockBalance);
            transactionBtcUtxoService.updateTxStatusToBalanceError(transactionBtcUtxo.getId());
        }

    }

    protected void out(TransactionBtcUtxo transactionBtcUtxo) {
        String address = transactionBtcUtxo.getTxFrom();
        Account account = accountService.getNormalAccountByAddress(address, CoinType.BTC.name());

        if(account == null) {
            logger.error("btc out comfirm balance get mormal account error, transactionBtcUtxo:{}", transactionBtcUtxo);
            transactionBtcUtxoService.updateTxStatusToBalanceError(transactionBtcUtxo.getId());
        }

        BigInteger txBalance = transactionBtcUtxo.getTxValue();

        Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionBtcUtxo.getTxHash());
        if(withdrawal == null) {
            accountService.outTotalOutAndBalance(
                    account.getId()
                    ,transactionBtcUtxo.getTxFrom()
                    ,transactionBtcUtxo.getTxTo()
                    ,CoinType.BTC.name()
                    ,transactionBtcUtxo.getId()
                    ,transactionBtcUtxo.getTxTime()
                    ,txBalance
            );

            transactionBtcUtxoService.updateTxStatusToValid(transactionBtcUtxo.getId());
            return;
        }

        accountService.outBalance(
                account.getId()
                ,transactionBtcUtxo.getTxFrom()
                ,transactionBtcUtxo.getTxTo()
                ,CoinType.BTC.name()
                ,transactionBtcUtxo.getId()
                ,transactionBtcUtxo.getTxTime()
                ,txBalance
        );

        transactionBtcUtxoService.updateTxStatusToValid(transactionBtcUtxo.getId());
    }

    protected void confirmBalance(TransactionBtcUtxo transactionBtcUtxo) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            String txType = transactionBtcUtxo.getTxType();
            if (TransactionEth.TX_TYPE_IN.equals(txType)) {
                in(transactionBtcUtxo);
            } else {
                out(transactionBtcUtxo);
            }

            dataSourceTransactionManager.commit(status);
        }catch (Exception e) {
            logger.error("confirm transaction btc balance error, transactionEth:{}", transactionBtcUtxo, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }
    }

    protected void confirmBalance(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionBtcUtxo transactionBtcUtxo : paginationRepertory.getPageItems()) {
            confirmBalance(transactionBtcUtxo);
        }
    }

    protected void confirmBalance() {
        PaginationCondition<TransactionBtcUtxo> paginationCondition = new PaginationCondition<TransactionBtcUtxo>();
        TransactionBtcUtxo queryTransactionBtcUtxo = new TransactionBtcUtxo();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionBtcUtxo);

        while (true){
            try {

                PaginationRepertory<TransactionBtcUtxo> paginationRepertory = transactionBtcUtxoService.getUnconfirmedBalance(paginationCondition);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get unconfirmed balance error", e);
                    }
                }

                confirmBalance(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }

            }catch (Exception e) {
                logger.error("confirm transaction eth balance error", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                confirmBalance();

            }
        }).start();
    }

}
