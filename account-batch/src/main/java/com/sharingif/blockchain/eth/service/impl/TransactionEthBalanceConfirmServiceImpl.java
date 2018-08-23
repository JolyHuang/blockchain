package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.eth.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Service
public class TransactionEthBalanceConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionEthService transactionEthService;
    private EthereumService ethereumService;
    private AccountService accountService;
    private OleContract oleContract;
    private WithdrawalService withdrawalService;

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
    @Resource
    public void setOleContract(OleContract oleContract) {
        this.oleContract = oleContract;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    @Transactional
    protected void in(TransactionEth transactionEth) {
        if(TransactionEth.TX_RECEIPT_STATUS_FAIL.equals(transactionEth.getTxReceiptStatus())) {
            logger.info("eth in receipt status is valid, transactionEth:{}", transactionEth);

            transactionEthService.updateTxStatusToValid(transactionEth.getId());

            return;
        }

        String address = transactionEth.getTxTo();
        String coinType = transactionEth.getCoinType();
        Account account = accountService.getNormalAccountByAddress(address, coinType);

        if(account == null) {
            logger.error("eth in comfirm balance get mormal account error, transactionEth:{}", transactionEth);
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getId());
        }

        // 判断是ETH还是token,token暂时只支持ole
        BigInteger blockBalance;
        if(CoinType.ETH.name().equals(coinType)) {
            blockBalance = ethereumService.getBalance(address);
        } else {
            blockBalance = oleContract.balanceOf(address);
        }
        BigInteger txBalance = transactionEth.getTxValue();
        BigInteger currentBalance = account.getBalance().add(txBalance);
        if(currentBalance.compareTo(blockBalance) <= 0) {
            accountService.inBalance(
                    account.getId()
                    ,transactionEth.getTxFrom()
                    ,transactionEth.getTxTo()
                    ,coinType
                    ,transactionEth.getId()
                    ,transactionEth.getTxTime()
                    ,txBalance
            );
            transactionEthService.updateTxStatusToValid(transactionEth.getId());

        } else {
            logger.error("eth comfirm balance error, transactionEth:{},account:{},currentBalance:{},blockBalance:{}",transactionEth, account, currentBalance, blockBalance);
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getId());
        }

    }

    protected void ethOut(Account account, TransactionEth transactionEth) {
        BigInteger txBalance = transactionEth.getTxValue();
        BigInteger actualFee = transactionEth.getActualFee();

        Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionEth.getTxHash());
        if(withdrawal == null) {

            if(TransactionEth.TX_RECEIPT_STATUS_FAIL.equals(transactionEth.getTxReceiptStatus())) {
                accountService.outTotalOutAndBalance(
                        account.getId()
                        ,transactionEth.getTxFrom()
                        ,transactionEth.getTxTo()
                        ,transactionEth.getCoinType()
                        ,transactionEth.getId()
                        ,transactionEth.getTxTime()
                        ,actualFee
                );
            } else {
                accountService.outEthTotalOutAndBalance(
                        account.getId()
                        ,transactionEth.getTxFrom()
                        ,transactionEth.getTxTo()
                        ,transactionEth.getCoinType()
                        ,transactionEth.getId()
                        ,transactionEth.getTxTime()
                        ,txBalance
                        ,actualFee
                );
            }

            transactionEthService.updateTxStatusToValid(transactionEth.getId());
            return;
        }

        if(TransactionEth.TX_RECEIPT_STATUS_FAIL.equals(transactionEth.getTxReceiptStatus())) {
            logger.info("eth out receipt status is valid, transactionEth:{}", transactionEth);

            BigInteger withdrawalFee = withdrawal.getFee();
            withdrawalService.updateFee(withdrawal.getId(), actualFee);

            accountService.outBalanceReceiptStatusFail(
                    account.getId()
                    ,transactionEth.getTxFrom()
                    ,transactionEth.getTxTo()
                    ,transactionEth.getCoinType()
                    ,transactionEth.getId()
                    ,transactionEth.getTxTime()
                    ,txBalance
                    ,withdrawalFee
                    ,actualFee
            );

            transactionEthService.updateTxStatusToValid(transactionEth.getId());
            return;
        }

        accountService.outBalance(
                account.getId()
                ,transactionEth.getTxFrom()
                ,transactionEth.getTxTo()
                ,transactionEth.getCoinType()
                ,transactionEth.getId()
                ,transactionEth.getTxTime()
                ,txBalance
                ,actualFee
        );

        transactionEthService.updateTxStatusToValid(transactionEth.getId());
    }

    protected void contractOut(String address, Account account, TransactionEth transactionEth) {
        // 处理ETH手续费
        Account ethAccount = accountService.getNormalAccountByAddress(address, CoinType.ETH.name());
        if(ethAccount == null) {
            logger.error("eth in comfirm balance get mormal ethAccount error, address:{}", address);
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getId());
        }
        BigInteger actualFee = transactionEth.getActualFee();
        // 合约金额
        BigInteger txBalance = transactionEth.getTxValue();

        Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionEth.getTxHash());
        if(withdrawal == null) {
            if(TransactionEth.TX_RECEIPT_STATUS_FAIL.equals(transactionEth.getTxReceiptStatus())) {
                accountService.outTotalOutAndBalance(
                        account.getId()
                        ,transactionEth.getTxFrom()
                        ,transactionEth.getTxTo()
                        ,transactionEth.getCoinType()
                        ,transactionEth.getId()
                        ,transactionEth.getTxTime()
                        ,actualFee
                );
            } else {
                accountService.outContractTotalOutAndBalance(
                        ethAccount.getId()
                        ,account.getId()
                        ,transactionEth.getTxFrom()
                        ,transactionEth.getTxTo()
                        ,transactionEth.getCoinType()
                        ,transactionEth.getId()
                        ,transactionEth.getTxTime()
                        ,txBalance
                        ,actualFee
                );

                transactionEthService.updateTxStatusToValid(transactionEth.getId());
                return;
            }
        }
        BigInteger withdrawalFee = withdrawal.getFee();
        withdrawalService.updateFee(withdrawal.getId(), actualFee);

        if(TransactionEth.TX_RECEIPT_STATUS_FAIL.equals(transactionEth.getTxReceiptStatus())) {
            logger.info("contract out receipt status is valid, transactionEth:{}", transactionEth);

            accountService.outContractBalanceReceiptStatusFail(
                    ethAccount.getId()
                    ,account.getId()
                    ,transactionEth.getTxFrom()
                    ,transactionEth.getTxTo()
                    ,transactionEth.getCoinType()
                    ,transactionEth.getId()
                    ,transactionEth.getTxTime()
                    ,txBalance
                    ,withdrawalFee
                    ,actualFee
            );

            transactionEthService.updateTxStatusToValid(transactionEth.getId());
            return;
        }

        accountService.outContractBalance(
                ethAccount.getId()
                ,account.getId()
                ,transactionEth.getTxFrom()
                ,transactionEth.getTxTo()
                ,transactionEth.getCoinType()
                ,transactionEth.getId()
                ,transactionEth.getTxTime()
                ,txBalance
                ,withdrawalFee
                ,actualFee
        );

        transactionEthService.updateTxStatusToValid(transactionEth.getId());

    }

    @Transactional
    protected void out(TransactionEth transactionEth) {

        String address = transactionEth.getTxFrom();
        Account account = accountService.getNormalAccountByAddress(address, transactionEth.getCoinType());

        if(account == null) {
            logger.error("eth in comfirm balance get mormal account error, transactionEth:{}", transactionEth);
            transactionEthService.updateTxStatusToBalanceError(transactionEth.getId());
        }

        // 判断是ETH还是token,token暂时只支持ole
        // 如果是ETH需要处理转账值和手续费
        // 如果是token需要处理eth账户和token账户
        if(CoinType.ETH.name().equals(transactionEth.getCoinType())) {
            ethOut(account, transactionEth);
        } else {
            contractOut(address, account, transactionEth);
        }

    }

    protected void confirmTransactionEthBalance(TransactionEth transactionEth) {
        try {
            String txType = transactionEth.getTxType();
            if (TransactionEth.TX_TYPE_IN.equals(txType)) {
                in(transactionEth);
            } else {
                out(transactionEth);
            }
        }catch (Exception e) {
            logger.error("confirm transaction eth balance error, transactionEth:{}", transactionEth, e);
            throw e;
        }
    }

    protected void confirmTransactionEthBalance(PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            confirmTransactionEthBalance(transactionEth);
        }
    }

    protected void confirmTransactionEthBalance() {
        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            try {

                PaginationRepertory<TransactionEth> paginationRepertory = transactionEthService.getUnconfirmedBalance(paginationCondition);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get unconfirmed balance error", e);
                    }
                }

                confirmTransactionEthBalance(paginationRepertory);

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

                confirmTransactionEthBalance();

            }
        }).start();
    }

}
