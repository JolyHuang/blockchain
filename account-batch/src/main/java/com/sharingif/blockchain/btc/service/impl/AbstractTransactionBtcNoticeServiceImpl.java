package com.sharingif.blockchain.btc.service.impl;

import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressNoticeSignatureService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * AbstractTransactionBtcNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:29
 */
public abstract class AbstractTransactionBtcNoticeServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionBtcUtxoService transactionBtcUtxoService;
    private AddressNoticeService addressNoticeService;
    private TransactionEthApiService transactionEthApiService;
    private AddressNoticeSignatureService addressNoticeSignatureService;

    public TransactionBtcUtxoService getTransactionBtcUtxoService() {
        return transactionBtcUtxoService;
    }
    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }
    public AddressNoticeService getAddressNoticeService() {
        return addressNoticeService;
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

    @Transactional
    protected void doTransaction(TransactionBtcUtxo transactionBtcUtxo) {
        try {
            sendNotice(transactionBtcUtxo);
        } catch (Exception e) {
            logger.error("transaction btc info:{}", transactionBtcUtxo, e);
            return;
        }

        // 修改交易状态
        updateTxStatus(transactionBtcUtxo.getId());
    }

    protected void doPaginationRepertory(PaginationRepertory<TransactionBtcUtxo> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (TransactionBtcUtxo transactionBtcUtxo : paginationRepertory.getPageItems()) {
            doTransaction(transactionBtcUtxo);
        }
    }

    protected void depositNotice() {
        PaginationCondition<TransactionBtcUtxo> paginationCondition = new PaginationCondition<TransactionBtcUtxo>();
        TransactionBtcUtxo queryTransactionBtcUtxo = new TransactionBtcUtxo();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionBtcUtxo);

        while (true) {
            try {
                PaginationRepertory<TransactionBtcUtxo> paginationRepertory = getPaginationRepertory(paginationCondition);

                writeLoadDataLogger(paginationRepertory);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        logger.error("get transaction error", e);
                    }
                }

                doPaginationRepertory(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }
            } catch (Exception e) {
                writeError(e);
            }
        }
    }

    abstract PaginationRepertory<TransactionBtcUtxo> getPaginationRepertory(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    abstract void updateTxStatus(String id);

    abstract void sendNotice(TransactionBtcUtxo transactionBtcUtxo);

    abstract void writeLoadDataLogger(PaginationRepertory<TransactionBtcUtxo> paginationRepertory);

    abstract void writeError(Exception e);

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                depositNotice();

            }
        }).start();
    }

}
