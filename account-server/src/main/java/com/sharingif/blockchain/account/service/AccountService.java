package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;
import java.util.Date;


public interface AccountService extends IBaseService<Account, java.lang.String> {

    /**
     * 查询正常状态账户信息
     * @param address
     * @return
     */
    Account getNormalAccountByAddress(String address);

    /**
     * 查询转账锁定账户信息
     * @param address
     * @return
     */
    Account getOutLockAccountByAddress(String address);

    /**
     * 修改账户余额
     * @param id
     * @param balance
     */
    void updateBalance(String id, BigInteger balance);

    /**
     * 入账金额
     * @param id
     * @param balance
     * @param inBalance
     */
    void inBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger inBalance);

    /**
     * 入账金额
     * @param id
     * @param balance
     * @param outBalance
     */
    void outBalance(String id, String from, String to, String coinType, String txHash, Date txTime, BigInteger balance, BigInteger outBalance);


	
}
