package com.sharingif.blockchain.transaction.service;

import com.sharingif.blockchain.transaction.model.entity.AddressNoticeTask;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;

/**
 * AddressNoticeTaskService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午2:07
 */
public interface AddressNoticeTaskService extends IBaseService<AddressNoticeTask, String> {

    /**
     * 添加交易处理中任务
     * @param address
     * @param coinType
     */
    void depositProcessing(String address, String coinType);

    /**
     * 获取充值处理中任务
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<AddressNoticeTask> getDepositProcessing(PaginationCondition<AddressNoticeTask> paginationCondition);

}
