package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.eth.service.TransactionEthService;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressNoticeSignatureService;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * WithdrawalEthFailNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/9/3 上午10:52
 */
@Service
public class WithdrawalEthFailNoticeServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private WithdrawalService withdrawalService;
    private AddressNoticeService addressNoticeService;
    private TransactionEthService transactionEthService;
    private TransactionEthApiService transactionEthApiService;
    private AddressNoticeSignatureService addressNoticeSignatureService;
    private DataSourceTransactionManager dataSourceTransactionManager;
    private AccountService accountService;

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    @Resource
    public void setTransactionEthService(TransactionEthService transactionEthService) {
        this.transactionEthService = transactionEthService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }
    @Resource
    public void setAddressNoticeSignatureService(AddressNoticeSignatureService addressNoticeSignatureService) {
        this.addressNoticeSignatureService = addressNoticeSignatureService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    protected String getTxStatus() {
        return TransactionEth.TX_STATUS_INVALID;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthToTransactionEthWithdrawalApiReq(Withdrawal withdrawal, TransactionEth transactionEth) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();
        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawal.getWithdrawalId());
        transactionEthWithdrawalApiReq.setTxStatus(getTxStatus());
        transactionEthWithdrawalApiReq.setTxFrom(withdrawal.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(withdrawal.getTxTo());
        String coinType = withdrawal.getSubCoinType() == null ? withdrawal.getCoinType() : withdrawal.getSubCoinType();
        transactionEthWithdrawalApiReq.setCoinType(coinType);
        transactionEthWithdrawalApiReq.setTxValue(withdrawal.getAmount());
        if(transactionEth != null) {
            transactionEthWithdrawalApiReq.setTxHash(transactionEth.getTxHash());
            transactionEthWithdrawalApiReq.setBlockNumber(transactionEth.getBlockNumber());
            transactionEthWithdrawalApiReq.setContractAddress(transactionEth.getContractAddress());
            transactionEthWithdrawalApiReq.setTxInput(transactionEth.getTxInput());
            transactionEthWithdrawalApiReq.setTxIndex(transactionEth.getTxIndex());
            transactionEthWithdrawalApiReq.setGasLimit(transactionEth.getGasLimit());
            transactionEthWithdrawalApiReq.setGasUsed(transactionEth.getGasUsed());
            transactionEthWithdrawalApiReq.setGasPrice(transactionEth.getGasPrice());
            transactionEthWithdrawalApiReq.setActualFee(transactionEth.getActualFee());
            transactionEthWithdrawalApiReq.setNonce(transactionEth.getNonce());
            transactionEthWithdrawalApiReq.setTxReceiptStatus(transactionEth.getTxReceiptStatus());
            transactionEthWithdrawalApiReq.setTxTime(transactionEth.getTxTime());
            transactionEthWithdrawalApiReq.setConfirmBlockNumber(transactionEth.getConfirmBlockNumber());
            transactionEthWithdrawalApiReq.setTxType(transactionEth.getTxType());
        }

        return transactionEthWithdrawalApiReq;
    }

    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionEth transactionEth) {
        if(addressNotice == null) {
            return;
        }

        String noticeAddress = addressNotice.getNoticeAddress();
        // 发送通知
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthToTransactionEthWithdrawalApiReq(withdrawal ,transactionEth);
        transactionEthWithdrawalApiReq.setSign(addressNoticeSignatureService.sign(transactionEthWithdrawalApiReq.getSignData()));
        UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
        transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
        transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
        transactionEthApiService.withdrawal(transactionEthWithdrawalApiReqUrlBody);
    }

    protected void updateTxStatus(Withdrawal withdrawal) {
        Account ethAccount = accountService.getNormalAccountByAddress(withdrawal.getTxFrom(), CoinType.ETH.name());
        if(StringUtils.isTrimEmpty(withdrawal.getSubCoinType())) {
            accountService.unfreezeBalance(ethAccount.getId(), withdrawal.getAmount().add(withdrawal.getFee()));
        } else {
            accountService.unfreezeBalance(ethAccount.getId(), withdrawal.getFee());
            Account contractAccount = accountService.getNormalAccountByAddress(withdrawal.getTxFrom(), withdrawal.getSubCoinType());
            accountService.unfreezeBalance(contractAccount.getId(), withdrawal.getAmount());
        }

        withdrawalService.updateStatusToFailNotified(withdrawal.getId());
    }

    protected void withdrawalNotice(Withdrawal withdrawal) {
        String coinType = withdrawal.getSubCoinType() == null ? withdrawal.getCoinType() : withdrawal.getSubCoinType();
        // 获取通知地址
        AddressNotice addressNotice = addressNoticeService.getWithdrawalNoticeAddress(withdrawal.getTxTo(), coinType);

        TransactionEth transactionEth = null;
        if(!StringUtils.isTrimEmpty(withdrawal.getTxHash())) {
            transactionEth = transactionEthService.getTransactionEth(withdrawal.getTxTo(), withdrawal.getTxHash());
        }
        try {
            sendNotice(addressNotice, withdrawal, transactionEth);
        } catch (Exception e) {
            logger.error("withdrawal transaction eth send notice error, withdrawal:{},transactionEth:{}", withdrawal, transactionEth, e);
            return;
        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 修改交易状态
            updateTxStatus(withdrawal);

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("eth withdrawal fail notice error, withdrawal:{},transactionEth:{},exception:{}", withdrawal, transactionEth, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }


    }

    protected void withdrawalNotice(PaginationRepertory<Withdrawal> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (Withdrawal withdrawal : paginationRepertory.getPageItems()) {
            withdrawalNotice(withdrawal);
        }
    }

    protected void withdrawalNotice() {
        PaginationCondition<Withdrawal> paginationCondition = new PaginationCondition<Withdrawal>();
        Withdrawal withdrawal = new Withdrawal();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(withdrawal);

        while (true){
            try {

                PaginationRepertory<Withdrawal> paginationRepertory = getPaginationRepertory(paginationCondition);

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("thread sleep error", e);
                    }
                }

                withdrawalNotice(paginationRepertory);

                if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
                }

            } catch (Exception e) {
                logger.error("eth withdrawal fail notice error", e);
            }
        }
    }

    protected PaginationRepertory<Withdrawal> getPaginationRepertory(PaginationCondition<Withdrawal> paginationCondition) {
        return withdrawalService.getEthStatusFial(paginationCondition);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                withdrawalNotice();
            }
        }).start();
    }

}
