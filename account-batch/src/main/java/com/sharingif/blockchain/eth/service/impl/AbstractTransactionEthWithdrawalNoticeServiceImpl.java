package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * 提现通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:06
 */
public abstract class AbstractTransactionEthWithdrawalNoticeServiceImpl extends AbstractTransactionEthNoticeServiceImpl {

    private WithdrawalService withdrawalService;
    private DataSourceTransactionManager dataSourceTransactionManager;

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthToTransactionEthWithdrawalApiReq(String withdrawalId, TransactionEth transactionEth) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();
        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawalId);
        transactionEthWithdrawalApiReq.setTxHash(transactionEth.getTxHash());
        transactionEthWithdrawalApiReq.setBlockNumber(transactionEth.getBlockNumber());
        transactionEthWithdrawalApiReq.setTxFrom(transactionEth.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(transactionEth.getTxTo());
        transactionEthWithdrawalApiReq.setContractAddress(transactionEth.getContractAddress());
        transactionEthWithdrawalApiReq.setCoinType(transactionEth.getCoinType());
        transactionEthWithdrawalApiReq.setTxInput(transactionEth.getTxInput());
        transactionEthWithdrawalApiReq.setTxValue(transactionEth.getTxValue());
        transactionEthWithdrawalApiReq.setTxIndex(transactionEth.getTxIndex());
        transactionEthWithdrawalApiReq.setGasLimit(transactionEth.getGasLimit());
        transactionEthWithdrawalApiReq.setGasUsed(transactionEth.getGasUsed());
        transactionEthWithdrawalApiReq.setGasPrice(transactionEth.getGasPrice());
        transactionEthWithdrawalApiReq.setActualFee(transactionEth.getActualFee());
        transactionEthWithdrawalApiReq.setNonce(transactionEth.getNonce());
        transactionEthWithdrawalApiReq.setTxReceiptStatus(transactionEth.getTxReceiptStatus());
        transactionEthWithdrawalApiReq.setTxTime(transactionEth.getTxTime());
        transactionEthWithdrawalApiReq.setConfirmBlockNumber(transactionEth.getConfirmBlockNumber());
        transactionEthWithdrawalApiReq.setTxStatus(transactionEth.getTxStatus());
        transactionEthWithdrawalApiReq.setTxType(transactionEth.getTxType());

        return transactionEthWithdrawalApiReq;
    }

    protected void doTransactionEth(TransactionEth transactionEth) {
        Withdrawal withdrawal = null;
        AddressNotice addressNotice = null;
        try {
            // 获取取现id
            withdrawal = withdrawalService.getWithdrawalByTxHash(transactionEth.getTxHash());
            if(withdrawal == null) {
                // 修改交易状态
                updateTxStatus(withdrawal, transactionEth.getId());
                return;
            }

            // 获取通知地址
            addressNotice = getAddressNoticeService().getWithdrawalNoticeAddress(transactionEth.getTxTo(), transactionEth.getCoinType());
        } catch (Exception e) {
            logger.error("transaction eth info:{}", transactionEth, e);
            getTransactionEthService().updateTaskStatusToFail(transactionEth.getId());
            throw e;
        }

        try {
            sendNotice(addressNotice, withdrawal, transactionEth);
        } catch (Exception e) {
            logger.error("withdrawal transaction eth send notice error, info:{}", transactionEth, e);
            return;
        }

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 修改交易状态
            updateTxStatus(withdrawal, transactionEth.getId());

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("eth withdrawal update txStatus error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }
    }

    @Deprecated
    @Override
    void updateTxStatus(String id) {

    }

    @Deprecated
    @Override
    void sendNotice(TransactionEth transactionEth) {

    }

    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionEth transactionEth) {
        if(addressNotice != null && withdrawal != null) {
            String noticeAddress = addressNotice.getNoticeAddress();
            // 发送通知
            TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthToTransactionEthWithdrawalApiReq(withdrawal.getWithdrawalId() ,transactionEth);
            transactionEthWithdrawalApiReq.setSign(getAddressNoticeSignatureService().sign(transactionEthWithdrawalApiReq.getSignData()));
            UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
            transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
            transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
            getTransactionEthApiService().withdrawal(transactionEthWithdrawalApiReqUrlBody);
        }
    }

    abstract void updateTxStatus(Withdrawal withdrawal, String id);

}
