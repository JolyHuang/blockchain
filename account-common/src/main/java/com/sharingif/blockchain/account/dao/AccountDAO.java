package com.sharingif.blockchain.account.dao;


import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.common.dao.BaseDAO;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;

import java.math.BigInteger;


public interface AccountDAO extends BaseDAO<Account,java.lang.String> {

    /**
     * 根据账户id减去账户余额，添加冻结余额
     * @param accountId
     * @param balance
     * @return
     */
    int updateFreezingBalanceById(String accountId, BigInteger balance);

    /**
     * 根据账户id减去冻结余额，添加账户余额
     * @param accountId
     * @param balance
     * @return
     */
    int updateUnfreezeBalanceById(String accountId, BigInteger balance);

    /**
     * 根据账户id添加入账总金额、余额
     * @param accountId
     * @param balance
     * @return
     */
    int updateTotalInAndBalanceById(String accountId, BigInteger balance);

    /**
     * 根据账户id添加出账总金额，减去余额
     * @param accountId
     * @param balance
     * @return
     */
    int updateTotalOutAndBalanceById(String accountId, BigInteger balance);

    /**
     * 根据账户id添加出账总金额，减去冻结余额
     * @param accountId
     * @param balance
     * @return
     */
    int updateTotalOutAndFrozenAmountById(String accountId, BigInteger balance);

    /**
     * 根据账户id添加出账总金额，减去冻结余额，添加余额
     * @param accountId
     * @param totalOut
     * @param frozenAmount
     * @param balance
     * @return
     */
    int updateTotalOutFrozenAmountBalanceById(String accountId, BigInteger totalOut, BigInteger frozenAmount, BigInteger balance);

    /**
     * 根据状态、币种、余额获取账号列表
     * @param paginationCondition
     * @return
     */
    PaginationRepertory<Account> queryPaginationListByStatusCoinTypeBalance(PaginationCondition<Account> paginationCondition);

}
