package com.sharingif.blockchain.btc.service;

import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;
import java.util.List;

/**
 * TransactionBtcUtxoService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/6 下午6:16
 */
public interface TransactionBtcUtxoService extends IBaseService<TransactionBtcUtxo, String> {

    /**
     * 查询交易数据
     * @param txHash
     * @param blockNumber
     * @param from
     * @param to
     * @param txType
     * @return
     */
    TransactionBtcUtxo getTransactionBtcUtxo(String txHash, BigInteger blockNumber, String from, String to, String txType);

    /**
     * 获取充值未处理交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getInUntreated(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取取现未处理交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getOutUntreated(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取未确认交易数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getUnconfirmedBlockNumber(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取充值未确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getUnconfirmedBalance(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取充值确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getInValid(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取取现确认余额数据
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getOutValid(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取充值充值无效交易
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getInInvalid(PaginationCondition<TransactionBtcUtxo> paginationCondition);

    /**
     * 获取取现充值无效交易
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<TransactionBtcUtxo> getOutInvalid(PaginationCondition<TransactionBtcUtxo> paginationCondition);

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
     * 修改交易确认区块数
     * @param id
     * @param confirmBlockNumber
     */
    void updateConfirmBlockNumber(String id, int confirmBlockNumber);

    /**
     * 修改交易状态为余额未确认、确认区块数
     * @param id
     * @param confirmBlockNumber
     */
    void updateTxStatusToBalanceUnconfirm(String id, int confirmBlockNumber);

    /**
     * 修改交易状态为有效
     * @param id
     */
    void updateTxStatusToValid(String id);

    /**
     * 修改交易状态为余额确认异常
     * @param id
     */
    void updateTxStatusToBalanceError(String id);

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
     * 修改交易状态为无效
     * @param id
     */
    void updateTxStatusToInvalid(String id);

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
     * 根据地址查询取现交易
     * @param address
     * @return
     */
    List<TransactionBtcUtxo> getWithdrawalTransactionBtcUtxo(String address);

}
