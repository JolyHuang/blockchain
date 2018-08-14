package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.domain.Output;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.common.constants.CoinTypeConvert;
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
import org.springframework.beans.factory.annotation.Value;
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

    private String btcNetType;
    private WithdrawalService withdrawalService;
    private AccountSysPrmService accountSysPrmService;
    private SecretKeyService secretKeyService;
    private BtcService btcService;
    private BtcApiService btcApiService;

    @Value("${btc.net.type}")
    public void setBtcNetType(String btcNetType) {
        this.btcNetType = btcNetType;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
        this.accountSysPrmService = accountSysPrmService;
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

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
        String coinType = withdrawal.getCoinType();
        int bipCoinType = CoinTypeConvert.convertToBipCoinType(coinType);
        if(Constants.BTC_NET_TYPE_TEST.equals(btcNetType)) {
            bipCoinType = CoinType.BIP_BTC_TEST.getBipCoinType();
        }

        String secretKeyId = accountSysPrmService.withdrawalAccount(bipCoinType);
        SecretKey secretKey = secretKeyService.getById(secretKeyId);
        String password = secretKeyService.decryptPassword(secretKey.getPassword());

        BigInteger fess = new BigInteger("19200");

        List<Output> outputList = btcService.getListUnspent(secretKey.getAddress(), withdrawal.getAmount(), fess);

        BtcTransferReq req = new BtcTransferReq();
        req.setSecretKeyId(secretKey.getId());
        req.setPassword(secretKey.getPassword());
        req.setToAddress(password);
        req.setAmount(withdrawal.getAmount());
        req.setFee(fess);
        List<BtcTransferListReq> btcTransferListReqList = new ArrayList<BtcTransferListReq>(outputList.size());
        for(Output output : outputList) {
            BtcTransferListReq btcTransferListReq = new BtcTransferListReq();
            btcTransferListReq.setTxId(output.getTxId());
            btcTransferListReq.setVout(output.getVOut());
            btcTransferListReq.setScriptPubKey(output.getScriptPubKey());
            btcTransferListReq.setAmount(output.getAmount().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
            btcTransferListReqList.add(btcTransferListReq);
        }
        BtcTransferRsp rsp = btcApiService.transfer(req);

        String signRawTransaction = btcService.signRawTransaction(rsp.getRawTransaction());
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
