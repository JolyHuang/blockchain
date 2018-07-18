package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.model.entity.AccountJnl;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;
import java.util.Date;


public interface AccountJnlService extends IBaseService<AccountJnl, java.lang.String> {

    /**
     * 入账流水
     * @param from
     * @param to
     * @param coinType
     * @param txHash
     * @param balance
     */
    void in(String from, String to, String coinType, String txHash, Date txTime, BigInteger balance);

    /**
     * 出账流水
     * @param from
     * @param to
     * @param coinType
     * @param txHash
     * @param balance
     */
    void out(String from, String to, String coinType, String txHash, Date txTime, BigInteger balance);

}
