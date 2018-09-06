package com.sharingif.blockchain.btc.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
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
 * WithdrawalBtcFailNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/9/3 下午12:50
 */
@Service
public class WithdrawalBtcFailNoticeServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private WithdrawalService withdrawalService;
    private DataSourceTransactionManager dataSourceTransactionManager;
    private AddressNoticeService addressNoticeService;
    private TransactionEthApiService transactionEthApiService;
    private AddressNoticeSignatureService addressNoticeSignatureService;
    private TransactionBtcUtxoService transactionBtcUtxoService;
    private AccountService accountService;

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    public TransactionEthApiService getTransactionEthApiService() {
        return transactionEthApiService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }
    public AddressNoticeSignatureService getAddressNoticeSignatureService() {
        return addressNoticeSignatureService;
    }
    @Resource
    public void setAddressNoticeSignatureService(AddressNoticeSignatureService addressNoticeSignatureService) {
        this.addressNoticeSignatureService = addressNoticeSignatureService;
    }
    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }


    protected String getTxStatus() {
        return TransactionEth.TX_STATUS_INVALID;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthWithdrawalApiReq(Withdrawal withdrawal, TransactionBtcUtxo transactionBtcUtxo) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();
        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawal.getWithdrawalId());
        transactionEthWithdrawalApiReq.setTxStatus(getTxStatus());
        transactionEthWithdrawalApiReq.setTxFrom(withdrawal.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(withdrawal.getTxTo());
        transactionEthWithdrawalApiReq.setCoinType(CoinType.BTC.name());
        transactionEthWithdrawalApiReq.setTxValue(withdrawal.getAmount());
        if(transactionBtcUtxo != null) {
            transactionEthWithdrawalApiReq.setTxHash(transactionBtcUtxo.getTxHash());
            transactionEthWithdrawalApiReq.setBlockNumber(transactionBtcUtxo.getBlockNumber());
            transactionEthWithdrawalApiReq.setTxIndex(transactionBtcUtxo.getTxIndex());
            transactionEthWithdrawalApiReq.setActualFee(transactionBtcUtxo.getActualFee());
            transactionEthWithdrawalApiReq.setTxTime(transactionBtcUtxo.getTxTime());
            transactionEthWithdrawalApiReq.setConfirmBlockNumber(transactionBtcUtxo.getConfirmBlockNumber());
            transactionEthWithdrawalApiReq.setTxType(transactionBtcUtxo.getTxType());
        }


        return transactionEthWithdrawalApiReq;
    }

    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionBtcUtxo transactionBtcUtxo) {
        if(addressNotice == null) {
            return;
        }

        String noticeAddress = addressNotice.getNoticeAddress();
        // 发送通知
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthWithdrawalApiReq(withdrawal, transactionBtcUtxo);
        transactionEthWithdrawalApiReq.setSign(getAddressNoticeSignatureService().sign(transactionEthWithdrawalApiReq.getSignData()));
        UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
        transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
        transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
        getTransactionEthApiService().withdrawal(transactionEthWithdrawalApiReqUrlBody);
    }

    void updateTxStatus(Withdrawal withdrawal) {
        Account account = accountService.getNormalAccountByAddress(withdrawal.getTxFrom(), CoinType.BTC.name());
        accountService.unfreezeBalance(account.getId(), withdrawal.getFrozenAmount());

        getWithdrawalService().updateStatusToFailNotified(withdrawal.getId());
    }

    protected void withdrawalNotice(Withdrawal withdrawal) {
        // 获取通知地址
        AddressNotice addressNotice = addressNoticeService.getWithdrawalNoticeAddress(withdrawal.getTxTo(), CoinType.BTC.name());

        TransactionBtcUtxo transactionBtcUtxo = null;
        if(!StringUtils.isTrimEmpty(withdrawal.getTxHash())) {
            transactionBtcUtxo = transactionBtcUtxoService.getTransactionBtcUtxo(withdrawal.getTxTo(), withdrawal.getTxHash());
        }
        try {
            sendNotice(addressNotice, withdrawal, transactionBtcUtxo);
        } catch (Exception e) {
            logger.error("withdrawal transaction btc send notice error, withdrawal:{}", withdrawal, e);
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
            logger.error("btc withdrawal update txStatus error, withdrawal:{}, exception:{}", withdrawal, e);
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

        while (true) {
            try {
                PaginationRepertory<Withdrawal> paginationRepertory = getPaginationRepertory(paginationCondition);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get transaction error", e);
                    }
                }

                withdrawalNotice(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }
            } catch (Exception e) {
                logger.error("btc withdrawal fail notice error", e);
            }
        }
    }

    protected PaginationRepertory<Withdrawal> getPaginationRepertory(PaginationCondition<Withdrawal> paginationCondition) {
        return withdrawalService.getBtcStatusFial(paginationCondition);
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
