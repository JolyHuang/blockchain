package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.account.service.AddressNonceService;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.blockchain.common.constants.CoinTypeConvert;
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
public class WithdrawalUntreatedStatusEthServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private WithdrawalService withdrawalService;
    private AccountSysPrmService accountSysPrmService;
    private EthApiService ethApiService;
    private EthErc20ContractApiService ethErc20ContractApiService;
    private AddressNonceService addressNonceService;
    private SecretKeyService secretKeyService;
    private EthereumService ethereumService;
    private OleContract oleContract;

    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setAccountSysPrmService(AccountSysPrmService accountSysPrmService) {
        this.accountSysPrmService = accountSysPrmService;
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

    private String ethWithdrawal(String secretKeyId, BigInteger nonce, BigInteger gasPrice, String password, Withdrawal withdrawal) {
        EthTransferReq req = new EthTransferReq();
        req.setNonce(nonce);
        req.setToAddress(withdrawal.getAddress());
        req.setAmount(withdrawal.getAmount());
        req.setGasPrice(gasPrice);
        req.setSecretKeyId(secretKeyId);
        req.setPassword(password);

        EthTransferRsp rsp = ethApiService.transfer(req);
        String txHash = ethereumService.ethSendRawTransaction(rsp.getHexValue());

        return txHash;
    }

    private String ethContractWithdrawal(String secretKeyId, BigInteger nonce, BigInteger gasPrice, String password, Withdrawal withdrawal) {
        Erc20TransferReq req = new Erc20TransferReq();
        req.setNonce(nonce);
        req.setToAddress(withdrawal.getAddress());
        req.setContractAddress(oleContract.getContractAddress());
        req.setAmount(withdrawal.getAmount());
        req.setGasPrice(gasPrice);
        req.setGasLimit(new BigInteger("100000"));
        req.setSecretKeyId(secretKeyId);
        req.setPassword(password);

        Erc20TransferRsp rsp = ethErc20ContractApiService.transfer(req);
        String txHash = ethereumService.ethSendRawTransaction(rsp.getHexValue());

        return txHash;
    }

    protected void withdrawalUntreatedStatus(Withdrawal withdrawal) {
        String secretKeyId = accountSysPrmService.withdrawalAccount(CoinTypeConvert.convertToBipCoinType(withdrawal.getCoinType()));
        SecretKey secretKey = secretKeyService.getById(secretKeyId);
        BigInteger nonce =  addressNonceService.getNonce(secretKey.getAddress());
        BigInteger gasPrice = ethereumService.getGasPrice();
        String password = secretKeyService.decryptPassword(secretKey.getPassword());

        String txHash = null;
        try{
            if(StringUtils.isTrimEmpty(withdrawal.getSubCoinType())) {
                txHash = ethWithdrawal(secretKeyId, nonce, gasPrice, password, withdrawal);
            } else {
                txHash = ethContractWithdrawal(secretKeyId, nonce, gasPrice, password, withdrawal);
            }
        } catch (Exception e) {
            logger.error("eth withdrawa error", e);
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
        }
        if(StringUtils.isTrimEmpty(txHash)) {
            withdrawalService.updateTaskStatusToFail(withdrawal.getId());
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

                if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
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
                logger.error("withdrawal untreated status error", e);
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
