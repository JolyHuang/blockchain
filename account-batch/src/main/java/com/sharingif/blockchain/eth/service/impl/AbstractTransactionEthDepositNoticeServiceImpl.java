package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthApiReq;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;

/**
 * 充值通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/20 下午6:28
 */
public abstract class AbstractTransactionEthDepositNoticeServiceImpl extends AbstractTransactionEthNoticeServiceImpl {

    protected TransactionEthApiReq convertTransactionEthToTransactionEthApiReq(TransactionEth transactionEth) {
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

    protected void sendNotice(TransactionEth transactionEth) {
        // 获取通知地址
        AddressNotice addressNotice = getAddressNoticeService().getDepositNoticeAddress(transactionEth.getTxTo(), CoinType.ETH.name());
        if(addressNotice != null) {
            String noticeAddress = addressNotice.getNoticeAddress();
            // 发送通知
            TransactionEthApiReq transactionEthApiReq = convertTransactionEthToTransactionEthApiReq(transactionEth);
            transactionEthApiReq.setSign(getAddressNoticeSignatureService().sign(transactionEthApiReq.getSignData()));
            UrlBody<TransactionEthApiReq> transactionEthApiReqUrlBody = new UrlBody<TransactionEthApiReq>();
            transactionEthApiReqUrlBody.setUrl(noticeAddress);
            transactionEthApiReqUrlBody.setBody(transactionEthApiReq);
            getTransactionEthApiService().eth(transactionEthApiReqUrlBody);
        }
    }

}
