package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;
import java.util.List;


public interface WithdrawalService extends IBaseService<Withdrawal, String> {

    /**
     * 获取未处理ETH交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getEthUntreated(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 获取未处理BTC交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getBtcUntreated(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 获取成功状态eth
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getEthStatusSuccess(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 获取成功状态btc
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getBtcStatusSuccess(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 获取失败状态eth
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getEthStatusFial(PaginationCondition<Withdrawal> paginationCondition);

    /**
     * 获取失败状态btc
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Withdrawal> getBtcStatusFial(PaginationCondition<Withdrawal> paginationCondition);

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
     * 修改任务状态为处理中
     * @param id
     */
    void updateTaskStatusToProcessing(String id);

    /**
     * 修改任务状态为失败
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
     * 修改状态为成功通知
     * @param id
     */
    void updateStatusToSuccessNotified(String id);

    /**
     * 修改状态为失败通知
     * @param id
     */
    void updateStatusToFailNotified(String id);

    /**
     * 根据txHash获取Withdrawal
     * @param txHash
     * @return
     */
    Withdrawal getWithdrawalByTxHash(String txHash);

    /**
     * 修改手续费
     * @param id
     * @param fee
     */
    void updateFee(String id, BigInteger fee);

    /**
     * 根据地址获取未处理取现列表
     * @param txFrom
     * @return
     */
    List<Withdrawal> getUntreatedStatusByTxFrom(String txFrom);

}
