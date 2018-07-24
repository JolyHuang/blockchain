package com.sharingif.blockchain.transaction.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthApiReq;
import co.olivecoin.wallet.api.blockchain.service.TransactionEthApiService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.security.rsa.RSAPrivateCrtKeyImpl;
import sun.security.rsa.RSAPublicKeyImpl;

import javax.annotation.Resource;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.concurrent.TimeUnit;

/**
 * 充值通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午6:28
 */
@Service
public abstract class AbstractTransactionEthDepositNoticeServiceImpl implements InitializingBean {

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

    protected String sign(TransactionEthApiReq transactionEthApiReq) {
        try {
            signature.update(transactionEthApiReq.getSignData());
            byte[] result = signature.sign();
            return base64Coder.encode(result);
        } catch (SignatureException e) {
            logger.error("signature exception", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    protected void doTransactionEth(TransactionEth transactionEth) {

        if(TransactionEth.TX_TYPE_OUT.equals(transactionEth.getTxType())) {
            // 非充值交易，交易状态直接状态
            updateTxStatus(transactionEth.getTxHash());
            return;
        }

        if(TransactionEth.TX_TYPE_IN.equals(transactionEth.getTxType())) {
            // 获取通知地址
            AddressNotice addressNotice = addressNoticeService.getDepositNoticeAddress(transactionEth.getTxTo(), transactionEth.getCoinType());
            String noticeAddress = addressNotice.getNoticeAddress();

            // 发送通知
            TransactionEthApiReq transactionEthApiReq = convertTransactionEthToTransactionEthApiReq(transactionEth);
            transactionEthApiReq.setSign(sign(transactionEthApiReq));
            UrlBody<TransactionEthApiReq> transactionEthApiReqUrlBody = new UrlBody<TransactionEthApiReq>();
            transactionEthApiReqUrlBody.setUrl(noticeAddress);
            transactionEthApiReqUrlBody.setBody(transactionEthApiReq);
            transactionEthApiService.eth(transactionEthApiReqUrlBody);

            // 修改交易状态
            updateTxStatus(transactionEth.getTxHash());
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

    public TransactionEthApiReq convertTransactionEthToTransactionEthApiReq(TransactionEth transactionEth) {
        TransactionEthApiReq transactionEthApiReq = new TransactionEthApiReq();
        transactionEthApiReq.setTxHash(transactionEth.getTxHash());
        transactionEthApiReq.setBlockNumber(transactionEth.getBlockNumber());
        transactionEthApiReq.setTxFrom(transactionEth.getTxFrom());
        transactionEthApiReq.setTxTo(transactionEth.getTxTo());
        transactionEthApiReq.setContractAddress(transactionEth.getContractAddress());
        transactionEthApiReq.setCoinType(transactionEth.getCoinType());
        transactionEthApiReq.setTxInput(transactionEth.getTxInput());
        transactionEthApiReq.setTxValue(transactionEth.getTxValue());
        transactionEthApiReq.setTxIndex(transactionEth.getTxIndex());
        transactionEthApiReq.setGasLimit(transactionEth.getGasLimit());
        transactionEthApiReq.setGasUsed(transactionEth.getGasUsed());
        transactionEthApiReq.setGasPrice(transactionEth.getGasPrice());
        transactionEthApiReq.setActualFee(transactionEth.getActualFee());
        transactionEthApiReq.setNonce(transactionEth.getNonce());
        transactionEthApiReq.setTxReceiptStatus(transactionEth.getTxReceiptStatus());
        transactionEthApiReq.setTxTime(transactionEth.getTxTime());
        transactionEthApiReq.setConfirmBlockNumber(transactionEth.getConfirmBlockNumber());
        transactionEthApiReq.setTxStatus(transactionEth.getTxStatus());
        transactionEthApiReq.setTxType(transactionEth.getTxType());

        return transactionEthApiReq;
    }

    protected void depositNotice() {
        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            PaginationRepertory<TransactionEth> paginationRepertory = getPaginationRepertory(paginationCondition);

            if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("get transaction error", e);
                }
            }

            doPaginationRepertory(paginationRepertory);

            if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                paginationCondition.setCurrentPage(1);
            } else {
                paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
            }
        }
    }

    abstract PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition);

    abstract void updateTxStatus(String txHash);

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
