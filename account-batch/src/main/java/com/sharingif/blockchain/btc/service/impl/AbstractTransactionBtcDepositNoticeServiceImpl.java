package com.sharingif.blockchain.btc.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthApiReq;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;

/**
 * AbstractTransactionBtcDepositNoticeServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:38
 */
public abstract class AbstractTransactionBtcDepositNoticeServiceImpl extends AbstractTransactionBtcNoticeServiceImpl {

    protected TransactionEthApiReq convertTransactionEthToTransactionEthApiReq(TransactionBtcUtxo transactionBtcUtxo) {
        TransactionEthApiReq transactionEthApiReq = new TransactionEthApiReq();
        transactionEthApiReq.setTxHash(transactionBtcUtxo.getTxHash());
        transactionEthApiReq.setBlockNumber(transactionBtcUtxo.getBlockNumber());
        transactionEthApiReq.setTxFrom(transactionBtcUtxo.getTxFrom());
        transactionEthApiReq.setTxTo(transactionBtcUtxo.getTxTo());
        transactionEthApiReq.setCoinType(CoinType.BTC.name());
        transactionEthApiReq.setTxValue(transactionBtcUtxo.getTxValue());
        transactionEthApiReq.setTxIndex(transactionBtcUtxo.getTxIndex());
        transactionEthApiReq.setActualFee(transactionBtcUtxo.getActualFee());
        transactionEthApiReq.setTxTime(transactionBtcUtxo.getTxTime());
        transactionEthApiReq.setConfirmBlockNumber(transactionBtcUtxo.getConfirmBlockNumber());
        transactionEthApiReq.setTxStatus(transactionBtcUtxo.getTxStatus());
        transactionEthApiReq.setTxType(transactionBtcUtxo.getTxType());

        return transactionEthApiReq;
    }

    protected void sendNotice(TransactionBtcUtxo transactionBtcUtxo) {
        // 获取通知地址
        AddressNotice addressNotice = getAddressNoticeService().getDepositNoticeAddress(transactionBtcUtxo.getTxTo(), CoinType.BTC.name());
        if(addressNotice != null) {
            String noticeAddress = addressNotice.getNoticeAddress();
            // 发送通知
            TransactionEthApiReq transactionEthApiReq = convertTransactionEthToTransactionEthApiReq(transactionBtcUtxo);
            transactionEthApiReq.setSign(getAddressNoticeSignatureService().sign(transactionEthApiReq.getSignData()));
            UrlBody<TransactionEthApiReq> transactionEthApiReqUrlBody = new UrlBody<TransactionEthApiReq>();
            transactionEthApiReqUrlBody.setUrl(noticeAddress);
            transactionEthApiReqUrlBody.setBody(transactionEthApiReq);
            getTransactionEthApiService().eth(transactionEthApiReqUrlBody);
        }
    }

}
