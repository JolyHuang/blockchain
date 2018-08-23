package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.account.service.AddressNonceService;
import com.sharingif.blockchain.account.service.WithdrawalService;
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
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
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

    private String ethWithdrawal(SecretKey secretKey, BigInteger nonce, BigInteger gasPrice, String password, Withdrawal withdrawal) {
        EthTransferReq req = new EthTransferReq();
        req.setNonce(nonce);
        req.setToAddress(withdrawal.getTxTo());
        req.setAmount(withdrawal.getAmount());
        req.setGasPrice(gasPrice);
        req.setSecretKeyId(secretKey.getId());
        req.setPassword(password);


        BigInteger fee = req.getGasPrice().multiply(Transfer.GAS_LIMIT);
        accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), withdrawal.getAmount().add(fee));
        withdrawalService.updateFee(withdrawal.getId(), fee);

        withdrawalService.updateTaskStatusToProcessing(withdrawal.getId());
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

        // ETH手续费
        BigInteger fee = req.getGasPrice().multiply(req.getGasLimit());
        withdrawalService.updateFee(withdrawal.getId(), fee);
        accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getCoinType(), fee);

        // 合约账户
        accountService.freezingBalance(withdrawal.getTxFrom(), withdrawal.getSubCoinType(), withdrawal.getAmount());

        withdrawalService.updateTaskStatusToProcessing(withdrawal.getId());
        Erc20TransferRsp rsp = ethErc20ContractApiService.transfer(req);
        String txHash = ethereumService.ethSendRawTransaction(rsp.getHexValue());

        return txHash;
    }

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
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

        withdrawalService.updateTaskStatusToSuccessAndStatusToProcessingAndTxHash(withdrawal.getId(), txHash);
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
