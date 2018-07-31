package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;


public interface WithdrawalService extends IBaseService<Withdrawal, String> {

    /**
     * 获取未处理交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getEthUntreated(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 修改状态为处理中
     * @param id
     */
    void updateStatusToProcessing(String id);

    /**
     * 修改状态为成功
     * @param id
     */
    void updateStatusToSuccess(String id);

    /**
     * 修改状态为失败
     * @param id
     */
    void updateStatusToFail(String id);

    /**
     * 修改处理状态为失败
     * @param id
     */
    void updateTaskStatusToFail(String id);

    /**
     * 修改区块交易hash
     * @param id
     * @param txHash
     */
    void updateTaskStatusToSuccessAndStatusToProcessingAndTxHash(String id, String txHash);

    /**
     * 根据txHash获取Withdrawal
     * @param txHash
     * @return
     */
    Withdrawal getWithdrawalByTxHash(String txHash);

}
