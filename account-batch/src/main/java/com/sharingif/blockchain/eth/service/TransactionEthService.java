package com.sharingif.blockchain.eth.service;


import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;


public interface TransactionEthService extends IBaseService<TransactionEth, String> {

    /**
     * 查询交易数据
     * @param txHash
     * @param blockNumber
     * @param from
     * @param to
     * @param txType
     * @return
     */
    TransactionEth getTransactionEth(String txHash, BigInteger blockNumber, String from, String to, String txType);

    /**
     * 获取充值未处理交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getInUntreated(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取取现未处理交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getOutUntreated(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取未确认交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getUnconfirmedBlockNumber(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取充值未确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getUnconfirmedBalance(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取充值充值无效交易
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getInInvalid(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取取现充值无效交易
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getOutInvalid(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取充值确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getInValid(PaginationCondition<TransactionEth> paginationCondition);

    /**
     * 获取取现确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionEth> getOutValid(PaginationCondition<TransactionEth> paginationCondition);


    /**
     * 修改交易状态充值处理中已通知
     * @param id
     */
    void updateTxStatusToDepositProcessingNotified(String id);

    /**
     * 修改交易状态取现处理中已通知
     * @param id
     */
    void updateTxStatusToWithdrawalProcessingNotified(String id);

    /**
     * 修改交易状态为余额未确认、确认区块数
     * @param id
     * @param confirmBlockNumber
     */
    void updateTxStatusToBalanceUnconfirm(String id, int confirmBlockNumber);

    /**
     * 修改交易状态为余额确认异常
     * @param id
     */
    void updateTxStatusToBalanceError(String id);

    /**
     * 修改交易确认区块数
     * @param id
     * @param confirmBlockNumber
     */
    void updateConfirmBlockNumber(String id, int confirmBlockNumber);

    /**
     * 修改交易状态为无效
     * @param id
     */
    void updateTxStatusToInvalid(String id);

    /**
     * 修改交易状态为有效
     * @param id
     */
    void updateTxStatusToValid(String id);

    /**
     * 修改交易状态为充值成功已通知
     * @param id
     */
    void updateTxStatusToDepositSuccessNotified(String id);

    /**
     * 修改交易状态为取现成功已通知
     * @param id
     */
    void updateTxStatusToWithdrawalSuccessNotified(String id);

    /**
     * 修改交易状态为充值失败已通知
     * @param id
     */
    void updateTxStatusToDepositFailNotified(String id);

    /**
     * 修改交易状态为取现失败已通知
     * @param id
     */
    void updateTxStatusToWithdrawalFailNotified(String id);

    /**
     * 修改处理状态为处理失败
     * @param id
     */
    void updateTaskStatusToFail(String id);

    /**
     * 根据txTo、txHash查询TransactionEth
     * @param txTo
     * @param txHash
     * @return
     */
    TransactionEth getTransactionEth(String txTo, String txHash);

}