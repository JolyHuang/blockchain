package com.sharingif.blockchain.btc.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.neemre.btcdcli4j.core.domain.Output;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferListReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferRsp;
import com.sharingif.blockchain.crypto.api.btc.service.BtcApiService;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * WithdrawalUntreatedStatusBtcServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 上午11:03
 */
@Service
public class WithdrawalUntreatedStatusBtcServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private WithdrawalService withdrawalService;
    private SecretKeyService secretKeyService;
    private BtcService btcService;
    private BtcApiService btcApiService;
    private AccountService accountService;
    private DataSourceTransactionManager dataSourceTransactionManager;
    private AddressNoticeService addressNoticeService;
    private TransactionEthApiService transactionEthApiService;
    private AddressNoticeSignatureService addressNoticeSignatureService;

    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }
    @Resource
    public void setBtcApiService(BtcApiService btcApiService) {
        this.btcApiService = btcApiService;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }
    @Resource
    public void setAddressNoticeSignatureService(AddressNoticeSignatureService addressNoticeSignatureService) {
        this.addressNoticeSignatureService = addressNoticeSignatureService;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthWithdrawalApiReq(Withdrawal withdrawal) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();
        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawal.getWithdrawalId());
        transactionEthWithdrawalApiReq.setTxStatus(TransactionBtcUtxo.TX_STATUS_UNTREATED);
        transactionEthWithdrawalApiReq.setTxFrom(withdrawal.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(withdrawal.getTxTo());
        transactionEthWithdrawalApiReq.setCoinType(CoinType.BTC.name());
        transactionEthWithdrawalApiReq.setTxValue(withdrawal.getAmount());
        transactionEthWithdrawalApiReq.setTxHash(withdrawal.getTxHash());
        transactionEthWithdrawalApiReq.setTxType(TransactionBtcUtxo.TX_TYPE_OUT);

        return transactionEthWithdrawalApiReq;
    }

    protected void sendNotice(Withdrawal withdrawal) {
        AddressNotice addressNotice = addressNoticeService.getWithdrawalNoticeAddress(withdrawal.getTxTo(), CoinType.BTC.name());

        if(addressNotice == null) {
            return;
        }

        String noticeAddress = addressNotice.getNoticeAddress();
        // 发送通知
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthWithdrawalApiReq(withdrawal);
        transactionEthWithdrawalApiReq.setSign(addressNoticeSignatureService.sign(transactionEthWithdrawalApiReq.getSignData()));
        UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
        transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
        transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
        transactionEthApiService.withdrawal(transactionEthWithdrawalApiReqUrlBody);
    }

    protected BigInteger getTotalBalance(List<Output> outputList) {
        BigInteger balance = BigInteger.ZERO;
        for(Output output : outputList) {
            balance = balance.add(output.getAmount().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
        }
        return balance;
    }

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
        withdrawalService.updateTaskStatusToProcessing(withdrawal.getId());

        SecretKey secretKey = secretKeyService.getSecretKeyByAddress(withdrawal.getTxFrom());
        String password = secretKeyService.decryptPassword(secretKey.getPassword());

        BigInteger fee = withdrawal.getFee();
        if(fee == null || fee.compareTo(BigInteger.ZERO) == 0)  {
            fee = new BigInteger("30000");
        }

        List<Output> outputList = btcService.getListUnspent(secretKey.getAddress(), withdrawal.getAmount(), fee);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            BigInteger frozenAmount = getTotalBalance(outputList);
            accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), frozenAmount);
            withdrawalService.updateFeeAndFrozenAmount(withdrawal.getId(), fee, frozenAmount);

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("btcWithdrawal error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }

        BtcTransferReq req = new BtcTransferReq();
        req.setSecretKeyId(secretKey.getId());
        req.setPassword(password);
        req.setToAddress(withdrawal.getTxTo());
        req.setAmount(withdrawal.getAmount());
        req.setFee(fee);
        List<BtcTransferListReq> btcTransferListReqList = new ArrayList<BtcTransferListReq>(outputList.size());
        for(Output output : outputList) {
            BtcTransferListReq btcTransferListReq = new BtcTransferListReq();
            btcTransferListReq.setTxId(output.getTxId());
            btcTransferListReq.setVout(output.getVOut());
            btcTransferListReq.setScriptPubKey(output.getScriptPubKey());
            btcTransferListReq.setAmount(output.getAmount().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
            btcTransferListReqList.add(btcTransferListReq);
        }
        req.setOutputList(btcTransferListReqList);
        BtcTransferRsp rsp = btcApiService.transfer(req);

        String signRawTransaction = btcService.signRawTransaction(rsp.getRawTransaction());
        String txHash = btcService.sendRawTransaction(signRawTransaction);
        if(StringUtils.isTrimEmpty(txHash)) {
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
            return;
        }

        withdrawalService.updateStatusToProcessingAndTaskStatusToUntreatedAndTxHash(withdrawal.getId(), txHash);

        try {
            withdrawal.setTxHash(txHash);
            sendNotice(withdrawal);
        } catch (Exception e) {
            logger.error("withdrawal btc send processing notice error", e);
        }
    }

    protected void withdrawalUntreatedStatus(PaginationRepertory<Withdrawal> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (Withdrawal withdrawal : paginationRepertory.getPageItems()) {
            withdrawalUntreatedStatus(withdrawal);
        }
    }

    protected void withdrawalUntreatedStatus() {
        PaginationCondition<Withdrawal> paginationCondition = new PaginationCondition<Withdrawal>();
        Withdrawal withdrawal = new Withdrawal();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(withdrawal);

        while (true){
            try {

                PaginationRepertory<Withdrawal> paginationRepertory = withdrawalService.getBtcUntreated(paginationCondition);

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get transaction error", e);
                    }
                }

                withdrawalUntreatedStatus(paginationRepertory);

                if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
                }

            } catch (Exception e) {
                logger.error("btc withdrawal untreated status error", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                withdrawalUntreatedStatus();

            }
        }).start();
    }

}
