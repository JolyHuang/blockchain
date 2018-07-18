package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;


public interface TransactionEthService extends IBaseService<TransactionEth, String> {

    /**
     * 获取未确认交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getUnconfirmedBlockNumber(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取未确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getUnconfirmedBalance(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 修改交易状态为无效
     * @param txHash
     */
    void updateTxStatusToInvalid(String txHash);
    /**
     * 修改交易状态为有效
     * @param txHash
     */
    void updateTxStatusToValid(String txHash);

    /**
     * 修改交易状态为余额未确认、确认区块数
     * @param txHash
     * @param confirmBlockNumber
     */
    void updateTxStatusToBalanceUnconfirm(String txHash, int confirmBlockNumber);

    /**
     * 修改交易状态为余额确认异常
     * @param txHash
     */
    void updateTxStatusToBalanceError(String txHash);

    /**
     * 修改交易确认区块数
     * @param txHash
     * @param confirmBlockNumber
     */
    void updateConfirmBlockNumber(String txHash, int confirmBlockNumber);

}