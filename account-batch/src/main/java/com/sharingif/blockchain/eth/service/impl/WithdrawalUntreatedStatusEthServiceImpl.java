package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.AddressNonceService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.Erc20TransferRsp;
import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferRsp;
import com.sharingif.blockchain.crypto.api.eth.service.EthApiService;
import com.sharingif.blockchain.crypto.api.eth.service.EthErc20ContractApiService;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.AddressNoticeSignatureService;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.web3j.tx.Transfer;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * WithdrawalUntreatedStatusServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午6:01
 */
@Service
public class WithdrawalUntreatedStatusEthServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private WithdrawalService withdrawalService;
    private EthApiService ethApiService;
    private EthErc20ContractApiService ethErc20ContractApiService;
    private AddressNonceService addressNonceService;
    private SecretKeyService secretKeyService;
    private EthereumService ethereumService;
    private OleContract oleContract;
    private AccountService accountService;
    private DataSourceTransactionManager dataSourceTransactionManager;
    private AddressNoticeService addressNoticeService;
    private AddressNoticeSignatureService addressNoticeSignatureService;
    private TransactionEthApiService transactionEthApiService;

    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setEthApiService(EthApiService ethApiService) {
        this.ethApiService = ethApiService;
    }
    @Resource
    public void setEthErc20ContractApiService(EthErc20ContractApiService ethErc20ContractApiService) {
        this.ethErc20ContractApiService = ethErc20ContractApiService;
    }
    @Resource
    public void setAddressNonceService(AddressNonceService addressNonceService) {
        this.addressNonceService = addressNonceService;
    }
    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }
    @Resource
    public void setEthereumService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }
    @Resource
    public void setOleContract(OleContract oleContract) {
        this.oleContract = oleContract;
    }
    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }
    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    @Resource
    public void setAddressNoticeSignatureService(AddressNoticeSignatureService addressNoticeSignatureService) {
        this.addressNoticeSignatureService = addressNoticeSignatureService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthToTransactionEthWithdrawalApiReq(Withdrawal withdrawal) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();
        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawal.getWithdrawalId());
        transactionEthWithdrawalApiReq.setTxStatus(TransactionEth.TX_STATUS_UNTREATED);
        transactionEthWithdrawalApiReq.setTxFrom(withdrawal.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(withdrawal.getTxTo());
        String coinType = withdrawal.getSubCoinType() == null ? withdrawal.getCoinType() : withdrawal.getSubCoinType();
        transactionEthWithdrawalApiReq.setCoinType(coinType);
        transactionEthWithdrawalApiReq.setTxValue(withdrawal.getAmount());
        transactionEthWithdrawalApiReq.setTxHash(withdrawal.getTxHash());
        transactionEthWithdrawalApiReq.setTxType(TransactionEth.TX_TYPE_OUT);

        return transactionEthWithdrawalApiReq;
    }

    protected void sendNotice(Withdrawal withdrawal) {
        String coinType = withdrawal.getSubCoinType() == null ? withdrawal.getCoinType() : withdrawal.getSubCoinType();
        // 获取通知地址
        AddressNotice addressNotice = addressNoticeService.getWithdrawalNoticeAddress(withdrawal.getTxTo(), coinType);

        if(addressNotice == null) {
            return;
        }

        String noticeAddress = addressNotice.getNoticeAddress();
        // 发送通知
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthToTransactionEthWithdrawalApiReq(withdrawal);
        transactionEthWithdrawalApiReq.setSign(addressNoticeSignatureService.sign(transactionEthWithdrawalApiReq.getSignData()));
        UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
        transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
        transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
        transactionEthApiService.withdrawal(transactionEthWithdrawalApiReqUrlBody);
    }

    private String ethWithdrawal(SecretKey secretKey, BigInteger nonce, BigInteger gasPrice, String password, Withdrawal withdrawal) {
        EthTransferReq req = new EthTransferReq();
        req.setNonce(nonce);
        req.setToAddress(withdrawal.getTxTo());
        req.setAmount(withdrawal.getAmount());
        req.setGasPrice(gasPrice);
        req.setSecretKeyId(secretKey.getId());
        req.setPassword(password);


        BigInteger fee = req.getGasPrice().multiply(Transfer.GAS_LIMIT);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), withdrawal.getAmount().add(fee));
            withdrawalService.updateFee(withdrawal.getId(), fee);

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("ethWithdrawal error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }

        EthTransferRsp rsp = ethApiService.transfer(req);
        String txHash = ethereumService.ethSendRawTransaction(rsp.getHexValue());

        return txHash;
    }

    private String ethContractWithdrawal(SecretKey secretKey, BigInteger nonce, BigInteger gasPrice, String password, Withdrawal withdrawal) {
        Erc20TransferReq req = new Erc20TransferReq();
        req.setNonce(nonce);
        req.setToAddress(withdrawal.getTxTo());
        req.setContractAddress(oleContract.getContractAddress());
        req.setAmount(withdrawal.getAmount());
        req.setGasPrice(gasPrice);
        req.setGasLimit(new BigInteger(Constants.ETH_TRANSFOR_GAS_LIMIT));
        req.setSecretKeyId(secretKey.getId());
        req.setPassword(password);

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // ETH手续费
            BigInteger fee = req.getGasPrice().multiply(req.getGasLimit());
            withdrawalService.updateFee(withdrawal.getId(), fee);
            accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), fee);

            // 合约账户
            accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getSubCoinType(), withdrawal.getAmount());

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("ethContractWithdrawal error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }

        Erc20TransferRsp rsp = ethErc20ContractApiService.transfer(req);
        String txHash = ethereumService.ethSendRawTransaction(rsp.getHexValue());

        return txHash;
    }

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
        withdrawalService.updateTaskStatusToProcessing(withdrawal.getId());

        SecretKey secretKey = secretKeyService.getSecretKeyByAddress(withdrawal.getTxFrom());

        BigInteger nonce =  ethereumService.ethGetTransactionCountPending(secretKey.getAddress());
        BigInteger gasPrice = withdrawal.getFee();
        if(gasPrice == null || gasPrice.compareTo(BigInteger.ZERO) == 0) {
            gasPrice = ethereumService.getGasPrice().add(new BigInteger("3000000000"));
        }
        String password = secretKeyService.decryptPassword(secretKey.getPassword());

        String txHash = null;
        try{
            if(StringUtils.isTrimEmpty(withdrawal.getSubCoinType())) {
                txHash = ethWithdrawal(secretKey, nonce, gasPrice, password, withdrawal);
            } else {
                txHash = ethContractWithdrawal(secretKey, nonce, gasPrice, password, withdrawal);
            }
        } catch (Exception e) {
            logger.error("eth withdrawa error, withdrawal:{}", withdrawal, e);
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
            return;
        }
        if(StringUtils.isTrimEmpty(txHash)) {
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
            return;
        }

        withdrawalService.updateStatusToProcessingAndTaskStatusToUntreatedAndTxHash(withdrawal.getId(), txHash);

        try {
            withdrawal.setTxHash(txHash);
            sendNotice(withdrawal);
        } catch (Exception e) {
            logger.error("withdrawal eth send processing notice error", e);
        }
    }

    protected void withdrawalUntreatedStatus(PaginationRepertory<Withdrawal> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (Withdrawal withdrawal : paginationRepertory.getPageItems()) {
            withdrawalUntreatedStatus(withdrawal);
        }
    }

    protected void withdrawalUntreatedStatus() {
        PaginationCondition<Withdrawal> paginationCondition = new PaginationCondition<Withdrawal>();
        Withdrawal withdrawal = new Withdrawal();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(withdrawal);

        while (true){
            try {

                PaginationRepertory<Withdrawal> paginationRepertory = withdrawalService.getEthUntreated(paginationCondition);

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null || paginationRepertory.getPageItems().size() == 0) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get transaction error", e);
                    }
                }

                withdrawalUntreatedStatus(paginationRepertory);

                if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
                }

            } catch (Exception e) {
                logger.error("eth withdrawal untreated status error", e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                withdrawalUntreatedStatus();

            }
        }).start();
    }

}
