package com.sharingif.blockchain.btc.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import com.neemre.btcdcli4j.core.domain.RawOutput;
import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.core.exception.UnknownCubeException;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 提现通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午4:33
 */
public abstract class AbstractTransactionBtcWithdrawalNoticeServiceImpl extends AbstractTransactionBtcNoticeServiceImpl {

    private WithdrawalService withdrawalService;
    private BtcService btcService;

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }

    protected TransactionEthWithdrawalApiReq convertTransactionEthWithdrawalApiReq(String withdrawalId, TransactionBtcUtxo transactionBtcUtxo) {
        TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = new TransactionEthWithdrawalApiReq();

        RawTransaction rawTransaction = btcService.getRawTransaction(transactionBtcUtxo.getTxHash());
        List<RawOutput> vOut = rawTransaction.getVOut();
        for(RawOutput rawOutput : vOut) {
            List<String> addresses = rawOutput.getScriptPubKey().getAddresses();
            String addresse = addresses.get(0);
            if(addresse.equals(transactionBtcUtxo.getTxTo())) {
                BigDecimal value = rawOutput.getValue();
                transactionEthWithdrawalApiReq.setTxValue(value.multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
                break;
            }
        }

        transactionEthWithdrawalApiReq.setWithdrawalId(withdrawalId);
        transactionEthWithdrawalApiReq.setTxHash(transactionBtcUtxo.getTxHash());
        transactionEthWithdrawalApiReq.setBlockNumber(transactionBtcUtxo.getBlockNumber());
        transactionEthWithdrawalApiReq.setTxFrom(transactionBtcUtxo.getTxFrom());
        transactionEthWithdrawalApiReq.setTxTo(transactionBtcUtxo.getTxTo());
        transactionEthWithdrawalApiReq.setCoinType(CoinType.BTC.name());
        transactionEthWithdrawalApiReq.setTxIndex(transactionBtcUtxo.getTxIndex());
        transactionEthWithdrawalApiReq.setActualFee(transactionBtcUtxo.getActualFee());
        transactionEthWithdrawalApiReq.setTxTime(transactionBtcUtxo.getTxTime());
        transactionEthWithdrawalApiReq.setConfirmBlockNumber(transactionBtcUtxo.getConfirmBlockNumber());
        transactionEthWithdrawalApiReq.setTxStatus(transactionBtcUtxo.getTxStatus());
        transactionEthWithdrawalApiReq.setTxType(transactionBtcUtxo.getTxType());

        return transactionEthWithdrawalApiReq;
    }

    @Transactional
    protected void doTransaction(TransactionBtcUtxo transactionBtcUtxo) {
        try {
            // 获取通知地址
            AddressNotice addressNotice = getAddressNoticeService().getWithdrawalNoticeAddress(transactionBtcUtxo.getTxTo(), CoinType.BTC.name());
            // 获取取现id
            Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionBtcUtxo.getTxHash());
            if(withdrawal == null) {
                logger.error("getWithdrawalByTxHash error, transactionBtcUtxo:{}", transactionBtcUtxo);
                throw new UnknownCubeException();
            }

            sendNotice(addressNotice, withdrawal, transactionBtcUtxo);

            // 修改交易状态
            updateTxStatus(withdrawal, transactionBtcUtxo.getId());

        } catch (Exception e) {
            logger.error("transaction btc info:{}", transactionBtcUtxo, e);
            getTransactionBtcUtxoService().updateTaskStatusToFail(transactionBtcUtxo.getId());
            throw e;
        }
    }

    @Deprecated
    @Override
    void updateTxStatus(String txHash) {

    }

    @Deprecated
    @Override
    void sendNotice(TransactionBtcUtxo transactionBtcUtxo) {

    }

    protected void sendNotice(AddressNotice addressNotice, Withdrawal withdrawal, TransactionBtcUtxo transactionBtcUtxo) {
        if(addressNotice != null) {
            String noticeAddress = addressNotice.getNoticeAddress();
            // 发送通知
            TransactionEthWithdrawalApiReq transactionEthWithdrawalApiReq = convertTransactionEthWithdrawalApiReq(withdrawal.getWithdrawalId() ,transactionBtcUtxo);
            transactionEthWithdrawalApiReq.setSign(getAddressNoticeSignatureService().sign(transactionEthWithdrawalApiReq.getSignData()));
            UrlBody<TransactionEthWithdrawalApiReq> transactionEthWithdrawalApiReqUrlBody = new UrlBody<TransactionEthWithdrawalApiReq>();
            transactionEthWithdrawalApiReqUrlBody.setUrl(noticeAddress);
            transactionEthWithdrawalApiReqUrlBody.setBody(transactionEthWithdrawalApiReq);
            getTransactionEthApiService().withdrawal(transactionEthWithdrawalApiReqUrlBody);
        }
    }

    abstract void updateTxStatus(Withdrawal withdrawal, String id);

}
