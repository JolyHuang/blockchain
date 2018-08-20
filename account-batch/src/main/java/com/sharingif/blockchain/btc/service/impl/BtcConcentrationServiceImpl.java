package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * btc归集
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/17 下午4:09
 */
@Service
public class BtcConcentrationServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private int concentrationTimeIntervalSeconds;
    private BigInteger btcConcentrationMinimumAmount;
    private AccountService accountService;
    private BtcService btcService;
    private AccountSysPrmService accountSysPrmService;
    private SecretKeyService secretKeyService;
    private WithdrawalService withdrawalService;


    @Value("${concentration.time.interval.seconds}")
    public void setConcentrationTimeIntervalSeconds(int concentrationTimeIntervalSeconds) {
        this.concentrationTimeIntervalSeconds = concentrationTimeIntervalSeconds;
    }
    @Value("${btc.concentration.minimum.amount}")
    public void setBtcConcentrationMinimumAmount(BigInteger btcConcentrationMinimumAmount) {
        this.btcConcentrationMinimumAmount = btcConcentrationMinimumAmount;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }
    @Resource
    public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
        this.accountSysPrmService = accountSysPrmService;
    }
    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    protected void concentration(Account account) {
        int bipCoinType = btcService.getBipCoinType();
        String secretKeyId = accountSysPrmService.getWithdrawalAccount(bipCoinType);
        SecretKey secretKey = secretKeyService.getById(secretKeyId);

        BigInteger fee = new BigInteger("20000");
        BigInteger amount = account.getBalance().subtract(fee);

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setCoinType(CoinType.BTC.name());
        withdrawal.setAddress(secretKey.getAddress());
        withdrawal.setFee(fee);
        withdrawal.setAmount(amount);
        withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
        withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);
        withdrawalService.add(withdrawal);

    }

    protected void concentration(PaginationRepertory<Account> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (Account account : paginationRepertory.getPageItems()) {
            concentration(account);
        }
    }

    protected void concentration() {
        PaginationCondition<Account> paginationCondition = new PaginationCondition<Account>();
        Account account = new Account();
        account.setCoinType(CoinType.BTC.name());
        account.setBalance(btcConcentrationMinimumAmount);
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(account);


        while (true){
            try {

                threadSleep();

                PaginationRepertory<Account> paginationRepertory = accountService.getPaginationListByStatusCoinTypeBalance(paginationCondition);

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    continue;
                }

                concentration(paginationRepertory);

                if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
                }

            } catch (Exception e) {
                logger.error("btc withdrawal untreated status error", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                concentration();

            }
        }).start();
    }

    protected void threadSleep() {
        try {
            TimeUnit.SECONDS.sleep(concentrationTimeIntervalSeconds);
        } catch (InterruptedException e) {
            logger.error("btc concentration error", e);
        }
    }

}
