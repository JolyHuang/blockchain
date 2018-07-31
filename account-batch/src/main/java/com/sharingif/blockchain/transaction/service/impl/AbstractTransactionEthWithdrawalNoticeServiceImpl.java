package com.sharingif.blockchain.transaction.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import org.springframework.transaction.annotation.Transactional;

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

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
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

    @Transactional
    protected void doTransactionEth(TransactionEth transactionEth) {
        try {
            // 获取通知地址
            AddressNotice addressNotice = getAddressNoticeService().getDepositNoticeAddress(transactionEth.getTxFrom(), transactionEth.getCoinType());
            // 获取取现id
            Withdrawal withdrawal = withdrawalService.getById(addressNotice.getAddressRegisterId());

            sendNotice(addressNotice, withdrawal, transactionEth);

            // 修改交易状态
            updateTxStatus(withdrawal, transactionEth.getTxHash());

        } catch (Exception e) {
            getTransactionEthService().updateTaskStatusToFail(transactionEth.getTxHash());
            throw e;
        }
    }

    @Deprecated
    @Override
    void updateTxStatus(String txHash) {

    }

    @Deprecated
    @Override
    void sendNotice(TransactionEth transactionEth) {

    }

    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionEth transactionEth) {
        if(addressNotice != null) {
            String noticeAddress = addressNotice.getNoticeAddress();
            // 发送通知
            TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthToTransactionEthWithdrawalApiReq(withdrawal.getWithdrawalId() ,transactionEth);
            transactionEthWithdrawalApiReq.setSign(sign(transactionEthWithdrawalApiReq.getSignData()));
            UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
            transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
            transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
            getTransactionEthApiService().withdrawal(transactionEthWithdrawalApiReqUrlBody);
        }
    }

    abstract void updateTxStatus(Withdrawal withdrawal, String txHash);

}
