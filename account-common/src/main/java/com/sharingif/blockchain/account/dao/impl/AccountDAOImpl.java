package com.sharingif.blockchain.account.dao.impl;


import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Repository;


import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.dao.AccountDAO;
import com.sharingif.blockchain.common.dao.impl.BaseDAOImpl;

import java.math.BigInteger;


@Repository
public class AccountDAOImpl extends BaseDAOImpl<Account,java.lang.String> implements AccountDAO {

    @Override
    public int updateFreezingBalanceById(String accountId, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);

        return update("updateFreezingBalanceById", account);
    }

    @Override
    public int updateUnfreezeBalanceById(String accountId, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);

        return update("updateUnfreezeBalanceById", account);
    }

    @Override
    public int updateTotalInAndBalanceById(String accountId, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);

        return update("updateTotalInAndBalanceById", account);
    }

    @Override
    public int updateTotalOutAndBalanceById(String accountId, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);

        return update("updateTotalOutAndBalanceById", account);
    }

    @Override
    public int updateTotalOutAndFrozenAmountById(String accountId, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setBalance(balance);

        return update("updateTotalOutAndFrozenAmountById", account);
    }

    @Override
    public int updateTotalOutFrozenAmountBalanceById(String accountId, BigInteger totalOut, BigInteger frozenAmount, BigInteger balance) {
        Account account = new Account();
        account.setId(accountId);
        account.setTotalOut(totalOut);
        account.setFrozenAmount(frozenAmount);
        account.setBalance(balance);

        return update("updateTotalOutFrozenAmountBalanceById", account);
    }

    @Override
    public PaginationRepertory<Account> queryPaginationListByStatusCoinTypeBalance(PaginationCondition<Account> paginationCondition) {
        return this.queryPagination("queryPaginationListByStatusCoinTypeBalance",  paginationCondition);
    }
}
