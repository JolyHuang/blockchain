package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.cube.core.util.UUIDUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.tx.Transfer;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * eth归集
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/17 下午5:24
 */
@Service
public class EthConcentrationServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final BigInteger QUERY_ETH_BALANCE = new BigDecimal("0.1").multiply(new BigDecimal(String.valueOf(Math.pow(10,18)))).toBigInteger();

    private int concentrationTimeIntervalSeconds;
    private BigInteger ethConcentrationMinimumAmount;
    private BigInteger oleConcentrationMinimumAmount;
    private AccountService accountService;
    private AccountSysPrmService accountSysPrmService;
    private SecretKeyService secretKeyService;
    private WithdrawalService withdrawalService;
    private EthereumService ethereumService;

    @Value("${concentration.time.interval.seconds}")
    public void setConcentrationTimeIntervalSeconds(int concentrationTimeIntervalSeconds) {
        this.concentrationTimeIntervalSeconds = concentrationTimeIntervalSeconds;
    }
    @Value("${eth.concentration.minimum.amount}")
    public void setEthConcentrationMinimumAmount(BigInteger ethConcentrationMinimumAmount) {
        this.ethConcentrationMinimumAmount = ethConcentrationMinimumAmount;
    }
    @Value("${ole.concentration.minimum.amount}")
    public void setOleConcentrationMinimumAmount(BigInteger oleConcentrationMinimumAmount) {
        this.oleConcentrationMinimumAmount = oleConcentrationMinimumAmount;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
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
    @Resource
    public void setEthereumService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }

    protected boolean oleConcentration(SecretKey secretKey, Account account, BigInteger gasPrice, BigInteger oleFee) {
        Account oleAccount = accountService.getNormalAccountByAddress(account.getAddress(), CoinType.OLE.name());
        if(oleAccount.getBalance().compareTo(oleConcentrationMinimumAmount) <0 || account.getBalance().compareTo(oleFee) <0 ) {
            return false;
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setWithdrawalId(UUIDUtils.generateUUID());
        withdrawal.setCoinType(CoinType.ETH.name());
        withdrawal.setSubCoinType(CoinType.OLE.name());
        withdrawal.setTxFrom(account.getAddress());
        withdrawal.setTxTo(secretKey.getAddress());
        withdrawal.setFee(gasPrice);
        withdrawal.setAmount(oleAccount.getBalance());
        withdrawal.setFrozenAmount(BigInteger.ZERO);
        withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
        withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);
        withdrawalService.add(withdrawal);

        return true;
    }

    protected void ethConcentration(SecretKey secretKey, Account account, BigInteger gasPrice) {
        if(account.getBalance().compareTo(ethConcentrationMinimumAmount.add(gasPrice.multiply(Transfer.GAS_LIMIT))) <0 ) {
            return;
        }

        Withdrawal withdrawal = new Withdrawal();
        withdrawal.setWithdrawalId(UUIDUtils.generateUUID());
        withdrawal.setCoinType(CoinType.ETH.name());
        withdrawal.setTxFrom(account.getAddress());
        withdrawal.setTxTo(secretKey.getAddress());
        withdrawal.setFee(gasPrice);
        withdrawal.setAmount(account.getBalance().subtract(gasPrice.multiply(Transfer.GAS_LIMIT)));
        withdrawal.setFrozenAmount(BigInteger.ZERO);
        withdrawal.setStatus(Withdrawal.STATUS_WITHDRAWAL_UNTREATED);
        withdrawal.setTaskStatus(Withdrawal.TASK_STATUS_UNTREATED);
        withdrawalService.add(withdrawal);
    }

    protected void concentration(Account account) {
        int bipCoinType = CoinType.ETH.getBipCoinType();
        String secretKeyId = accountSysPrmService.getWithdrawalAccount(bipCoinType);
        SecretKey secretKey = secretKeyService.getById(secretKeyId);

        if(account.getAddress().equals(secretKey.getAddress())) {
            return;
        }

        List<Withdrawal> untreatedWithdrawalList = withdrawalService.getUntreatedStatusByTxFrom(account.getAddress());
        if(untreatedWithdrawalList != null && untreatedWithdrawalList.size()>0){
            return;
        }

        BigInteger gasPrice = ethereumService.getGasPrice().add(new BigInteger("1000000000"));
        BigInteger gasLimit = new BigInteger(Constants.ETH_TRANSFOR_GAS_LIMIT);
        BigInteger oleFee = gasPrice.multiply(gasLimit);

        boolean isOleConcentration = oleConcentration(secretKey, account, gasPrice, oleFee);
        if(isOleConcentration) {
            account.setBalance(account.getBalance().subtract(oleFee));
        }

        ethConcentration(secretKey, account, gasPrice);

    }

    protected void concentration(PaginationRepertory<Account> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (Account account : paginationRepertory.getPageItems()) {
            concentration(account);
        }
    }

    public void concentration() {
        PaginationCondition<Account> paginationCondition = new PaginationCondition<Account>();
        Account account = new Account();
        account.setCoinType(CoinType.ETH.name());
        account.setBalance(QUERY_ETH_BALANCE);
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(account);


        while (true){
            try {

                threadSleep();

                PaginationRepertory<Account> paginationRepertory = accountService.getPaginationListByStatusCoinTypeBalance(paginationCondition);

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
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
