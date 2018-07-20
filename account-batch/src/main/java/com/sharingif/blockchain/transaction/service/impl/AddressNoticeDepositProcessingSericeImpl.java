package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.AddressNoticeTask;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressNoticeTaskService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 充值处理中通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午6:28
 */
public class AddressNoticeDepositProcessingSericeImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private AddressNoticeTaskService addressNoticeTaskService;
    private AddressNoticeService addressNoticeService;

    @Resource
    public void setAddressNoticeTaskService(AddressNoticeTaskService addressNoticeTaskService) {
        this.addressNoticeTaskService = addressNoticeTaskService;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }

    protected void depositProcessingAddressNotice(PaginationRepertory<AddressNoticeTask> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (AddressNoticeTask addressNoticeTask : paginationRepertory.getPageItems()) {
            AddressNotice addressNotice = addressNoticeService.getById(addressNoticeTask.getAddressNoticeId());
            addressNotice.getNoticeAddress();
        }
    }

    protected void depositProcessingAddressNotice() {
        PaginationCondition<AddressNoticeTask> paginationCondition = new PaginationCondition<AddressNoticeTask>();
        AddressNoticeTask addressNoticeTask = new AddressNoticeTask();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(addressNoticeTask);

        while (true){
            PaginationRepertory<AddressNoticeTask> paginationRepertory = addressNoticeTaskService.getDepositProcessing(paginationCondition);

            if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("get unconfirmed balance error", e);
                }
            }

            depositProcessingAddressNotice(paginationRepertory);

            if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                paginationCondition.setCurrentPage(1);
            } else {
                paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                depositProcessingAddressNotice();

            }
        }).start();
    }

}
