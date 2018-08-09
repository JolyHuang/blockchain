package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * TransactionBtcBalanceConfirmServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午7:35
 */
public class TransactionBtcBalanceConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionBtcUtxoService transactionBtcUtxoService;

    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }

    @Transactional
    protected void in(TransactionBtcUtxo transactionBtcUtxo) {

    }

    @Transactional
    protected void out(TransactionBtcUtxo transactionBtcUtxo) {

    }

    protected void confirmBalance(TransactionBtcUtxo transactionBtcUtxo) {
        try {
            String txType = transactionBtcUtxo.getTxType();
            if (TransactionEth.TX_TYPE_IN.equals(txType)) {
                in(transactionBtcUtxo);
            } else {
                out(transactionBtcUtxo);
            }
        }catch (Exception e) {
            logger.error("confirm transaction btc balance error, transactionEth:{}", transactionBtcUtxo, e);
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

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
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
