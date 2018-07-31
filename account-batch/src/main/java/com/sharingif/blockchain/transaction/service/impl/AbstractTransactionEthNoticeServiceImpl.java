package com.sharingif.blockchain.transaction.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressNoticeService;
import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import com.sharingif.cube.security.binary.Base64Coder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import sun.security.rsa.RSAPrivateCrtKeyImpl;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.TimeUnit;

/**
 * 通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:18
 */
public abstract class AbstractTransactionEthNoticeServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private AddressNoticeService addressNoticeService;
    private TransactionEthService transactionEthService;
    private TransactionEthApiService transactionEthApiService;
    private Signature signature;
    private Base64Coder base64Coder;

    @Resource
    public void setAddressNoticeService(AddressNoticeService addressNoticeService) {
        this.addressNoticeService = addressNoticeService;
    }
    public AddressNoticeService getAddressNoticeService() {
        return addressNoticeService;
    }
    public TransactionEthService getTransactionEthService() {
        return transactionEthService;
    }
    @Resource
    public void setTransactionEthService(TransactionEthService transactionEthService) {
        this.transactionEthService = transactionEthService;
    }
    @Resource
    public void setTransactionEthApiService(TransactionEthApiService transactionEthApiService) {
        this.transactionEthApiService = transactionEthApiService;
    }
    public TransactionEthApiService getTransactionEthApiService() {
        return transactionEthApiService;
    }
    public Signature getSignature() {
        return signature;
    }
    public Base64Coder getBase64Coder() {
        return base64Coder;
    }
    @Resource
    public void setBase64Coder(Base64Coder base64Coder) {
        this.base64Coder = base64Coder;
    }
    @Value("${deposit.callback.private}")
    public void setDepositCallbackPrivate(String depositCallbackPrivate) {
        try {
            RSAPrivateKey rsaPrivateKey = RSAPrivateCrtKeyImpl.newKey(base64Coder.decode(depositCallbackPrivate));

            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(rsaPrivateKey.getEncoded());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            signature = Signature.getInstance("MD5withRSA");
            signature.initSign(privateKey);
        } catch (Exception e) {
            logger.error("invalid key exception", e);
            throw new RuntimeException(e);
        }
    }

    protected String sign(byte[] signData) {
        try {
            getSignature().update(signData);
            byte[] result = getSignature().sign();
            return getBase64Coder().encode(result);
        } catch (SignatureException e) {
            logger.error("signature exception", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    protected void doTransactionEth(TransactionEth transactionEth) {
        try {
            sendNotice(transactionEth);

            // 修改交易状态
            updateTxStatus(transactionEth.getTxHash());
        } catch (Exception e) {
            transactionEthService.updateTaskStatusToFail(transactionEth.getTxHash());
            throw e;
        }
    }

    protected void doPaginationRepertory(PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for (TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            doTransactionEth(transactionEth);
        }
    }

    protected void depositNotice() {
        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            try {
                PaginationRepertory<TransactionEth> paginationRepertory = getPaginationRepertory(paginationCondition);

                writeLoadDataLogger(paginationRepertory);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        logger.error("get transaction error", e);
                    }
                }

                doPaginationRepertory(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }
            } catch (Exception e) {
                writeError(e);
            }
        }
    }

    abstract PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition);

    abstract void updateTxStatus(String txHash);

    abstract void sendNotice(TransactionEth transactionEth);

    abstract void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory);

    abstract void writeError(Exception e);

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                depositNotice();

            }
        }).start();
    }

}
