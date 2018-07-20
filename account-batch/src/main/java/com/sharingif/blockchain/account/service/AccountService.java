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
     * 入账金额
     * @param id
     * @param balance
     */
    void inBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance);

    /**
     * 入账金额
     * @param id
     * @param balance
     */
    void outBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee);

    /**
     * 入账金额
     * @param ethAccountId
     * @param balance
     */
    void outBalance(String ethAccountId, String contractAccountId, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger actualFee);


	
}
