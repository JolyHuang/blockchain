package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * TransactionBtcBlockNumberConfirmServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:57
 */
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

    protected void confirmBlockNumber(TransactionBtcUtxo transactionBtcUtxo) {

        RawTransaction rawTransaction;
        try {
            rawTransaction = btcService.getRawTransaction(transactionBtcUtxo.getTxHash());
        } catch (Exception e) {
            logger.error("btc validate transaction confirm block number error", e);
            transactionBtcUtxoService.updateTxStatusToInvalid(transactionBtcUtxo.getId());
            return;
        }
        Integer confirmations = rawTransaction.getConfirmations();
        int confirmBlockNumber = transactionBtcUtxo.getConfirmBlockNumber();

        if(confirmations == confirmBlockNumber) {
            return;
        }

        if(confirmations > validBlockNumber) {
            transactionBtcUtxoService.updateConfirmBlockNumber(transactionBtcUtxo.getId(), confirmations);
            return;
        } else {
            transactionBtcUtxoService.updateTxStatusToBalanceUnconfirm(transactionBtcUtxo.getId(), confirmations);
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

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
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
