package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.domain.RawInput;
import com.neemre.btcdcli4j.core.domain.RawOutput;
import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 交易区块数确认
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:57
 */
@Service
public class TransactionBtcBlockNumberConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private int validBlockNumber;
    private TransactionBtcUtxoService transactionBtcUtxoService;
    private BtcService btcService;

    @Value("${btc.valid.block.number}")
    public void setValidBlockNumber(int validBlockNumber) {
        this.validBlockNumber = validBlockNumber;
    }
    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }

    protected boolean validateTransaction(TransactionBtcUtxo transactionBtcUtxo, RawTransaction rawTransaction) {

        if(TransactionBtcUtxo.TX_TYPE_IN.equals(transactionBtcUtxo.getTxType())){
            List<RawOutput> rawOutputList = rawTransaction.getVOut();

            RawOutput rawOutput = rawOutputList.get(transactionBtcUtxo.getTxIndex().intValue());

            BigInteger value = rawOutput.getValue().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger();
            String address = rawOutput.getScriptPubKey().getAddresses().get(0);

            if(transactionBtcUtxo.getTxValue().compareTo(value) !=0) {
                return false;
            }

            if(!transactionBtcUtxo.getTxTo().equals(address)) {
                return false;
            }
        }

        if(TransactionBtcUtxo.TX_TYPE_OUT.equals(transactionBtcUtxo.getTxType())) {
            RawInput rawInput = rawTransaction.getVIn().get(transactionBtcUtxo.getVinTxIndex());

            if(!transactionBtcUtxo.getVinTxHash().equals(rawInput.getTxId())) {
                return false;
            }
        }


        return true;
    }

    protected void confirmBlockNumber(TransactionBtcUtxo transactionBtcUtxo) {

        RawTransaction rawTransaction;
        try {
            rawTransaction = btcService.getRawTransaction(transactionBtcUtxo.getTxHash());
        } catch (Exception e) {
            logger.error("btc validate transaction confirm block number error, transactionBtcUtxo:{}", transactionBtcUtxo, e);
            Integer currentBlockCount = btcService.getBlockCount();
            if(currentBlockCount > (transactionBtcUtxo.getBlockNumber().intValue()+validBlockNumber)) {
                transactionBtcUtxoService.updateTxStatusToInvalid(transactionBtcUtxo.getId());
            }
            return;
        }
        Integer confirmations = rawTransaction.getConfirmations();
        int confirmBlockNumber = transactionBtcUtxo.getConfirmBlockNumber();

        if(confirmations == confirmBlockNumber && confirmations <= validBlockNumber) {
            return;
        }

        if(confirmations > validBlockNumber) {
            if(!validateTransaction(transactionBtcUtxo, rawTransaction)) {
                logger.error("btc validate transaction content error, transactionBtcUtxo:{}", transactionBtcUtxo);
                transactionBtcUtxoService.updateTxStatusToInvalid(transactionBtcUtxo.getId());
            }

            transactionBtcUtxoService.updateTxStatusToBalanceUnconfirm(transactionBtcUtxo.getId(), confirmations);
            return;
        } else {
            transactionBtcUtxoService.updateConfirmBlockNumber(transactionBtcUtxo.getId(), confirmations);
            return;
        }

    }

    protected void confirmBlockNumber(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionBtcUtxo transactionBtcUtxo : paginationRepertory.getPageItems()) {
            confirmBlockNumber(transactionBtcUtxo);
        }
    }

    protected void confirmBlockNumber() {

        PaginationCondition<TransactionBtcUtxo> paginationCondition = new PaginationCondition<TransactionBtcUtxo>();
        TransactionBtcUtxo queryTransactionBtcUtxo = new TransactionBtcUtxo();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionBtcUtxo);

        while (true){
            try {
                PaginationRepertory<TransactionBtcUtxo> paginationRepertory = transactionBtcUtxoService.getUnconfirmedBlockNumber(paginationCondition);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get unconfirmed block number error", e);
                    }
                }

                confirmBlockNumber(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }
            }catch (Exception e) {
                logger.error("confirm transaction eth block number error", e);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                confirmBlockNumber();

            }
        }).start();
    }

}
