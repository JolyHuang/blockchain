package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.eth.service.TransactionEthService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
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
 * 通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:18
 */
public abstract class AbstractTransactionEthNoticeServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private AddressNoticeService addressNoticeService;
    private TransactionEthService transactionEthService;
    private TransactionEthApiService transactionEthApiService;
    private AddressNoticeSignatureService addressNoticeSignatureService;

    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    public AddressNoticeService getAddressNoticeService() {
        return addressNoticeService;
    }
    public TransactionEthService getTransactionEthService() {
        return transactionEthService;
    }
    @Resource
    public void setTransactionEthService(TransactionEthService transactionEthService) {
        this.transactionEthService = transactionEthService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }
    public TransactionEthApiService getTransactionEthApiService() {
        return transactionEthApiService;
    }
    public AddressNoticeSignatureService getAddressNoticeSignatureService() {
        return addressNoticeSignatureService;
    }
    @Resource
    public void setAddressNoticeSignatureService(AddressNoticeSignatureService addressNoticeSignatureService) {
        this.addressNoticeSignatureService = addressNoticeSignatureService;
    }

    @Transactional
    protected void doTransactionEth(TransactionEth transactionEth) {
        try {
            sendNotice(transactionEth);
        } catch (Exception e) {
            logger.error("transaction eth info:{}", transactionEth, e);
            return;
        }

        // 修改交易状态
        updateTxStatus(transactionEth.getId());
    }

    protected void doPaginationRepertory(PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            doTransactionEth(transactionEth);
        }
    }

    protected void depositNotice() {
        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            try {
                PaginationRepertory<TransactionEth> paginationRepertory = getPaginationRepertory(paginationCondition);

                writeLoadDataLogger(paginationRepertory);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
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

    abstract PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition);

    abstract void updateTxStatus(String id);

    abstract void sendNotice(TransactionEth transactionEth);

    abstract void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory);

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
