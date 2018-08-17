package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;
import java.util.Date;


public interface AccountService extends IBaseService<Account, java.lang.String> {

    /**
     * 初始化账户
     * @param coinType
     * @param address
     */
    void initAccount(String coinType, String address);

    /**
     * 查询正常状态账户信息
     * @param address
     * @return
     */
    Account getNormalAccountByAddress(String address, String coinType);

    /**
     * 查询转账锁定账户信息
     * @param address
     * @return
     */
    Account getOutLockAccountByAddress(String address, String coinType);

    /**
     * 冻结余额
     * @param accountId
     * @param balance
     */
    void freezingBalance(String accountId, BigInteger balance);

    /**
     * 冻结余额
     * @param address
     * @param coinType
     * @param balance
     */
    void freezingBalance(String address, String coinType, BigInteger balance);

    /**
     * 解冻余额
     * @param accountId
     * @param balance
     */
    void unfreezeBalance(String accountId, BigInteger balance);

    /**
     * 解冻余额
     * @param address
     * @param coinType
     * @param balance
     */
    void unfreezeBalance(String address, String coinType, BigInteger balance);

    /**
     * 入账金额
     * @param id
     * @param balance
     */
    void inBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance);

    /**
     * 出账金额
     * @param id
     * @param balance
     */
    void outBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee);

    /**
     * 出账金额
     * @param ethAccountId
     * @param balance
     */
    void outContractBalance(String ethAccountId, String contractAccountId, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger withdrawalFee, BigInteger actualFee);


	
}
