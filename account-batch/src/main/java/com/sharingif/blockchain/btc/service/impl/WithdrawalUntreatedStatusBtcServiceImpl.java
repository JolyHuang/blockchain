package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.domain.Output;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferListReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferRsp;
import com.sharingif.blockchain.crypto.api.btc.service.BtcApiService;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

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

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
        SecretKey secretKey = secretKeyService.getSecretKeyByAddress(withdrawal.getTxFrom());
        String password = secretKeyService.decryptPassword(secretKey.getPassword());

        BigInteger fee = withdrawal.getFee();
        if(fee == null || fee.compareTo(BigInteger.ZERO) == 0)  {
            fee = new BigInteger("30000");
        }
        accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), withdrawal.getAmount().add(fee));
        withdrawalService.updateFee(withdrawal.getId(), fee);

        List<Output> outputList = btcService.getListUnspent(secretKey.getAddress(), withdrawal.getAmount(), fee);

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
        withdrawalService.updateTaskStatusToProcessing(withdrawal.getId());
        String txHash = btcService.sendRawTransaction(signRawTransaction);
        if(StringUtils.isTrimEmpty(txHash)) {
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
            return;
        }

        withdrawalService.updateTaskStatusToSuccessAndStatusToProcessingAndTxHash(withdrawal.getId(), txHash);
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

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
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
