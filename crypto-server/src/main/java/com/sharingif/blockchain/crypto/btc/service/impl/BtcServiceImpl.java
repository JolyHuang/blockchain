package com.sharingif.blockchain.crypto.btc.service.impl;

import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferListReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferReq;
import com.sharingif.blockchain.crypto.api.btc.entity.BtcTransferRsp;
import com.sharingif.blockchain.crypto.app.constants.CoinType;
import com.sharingif.blockchain.crypto.btc.service.BtcService;
import com.sharingif.blockchain.crypto.key.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.key.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.key.service.SecretKeyService;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import sd.fomin.gerbera.transaction.TransactionBuilder;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * BtcServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午2:29
 */
@Service
public class BtcServiceImpl implements BtcService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SecretKeyService secretKeyService;

    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }

    @Override
    public NetworkParameters getNetworkParameters(int coinType) {
        if(coinType == CoinType.BTC_TEST.getBipCoinType()) {
            return TestNet3Params.get();
        }

        return MainNetParams.get();
    }

    @Override
    public BtcTransferRsp transfer(BtcTransferReq req) {
        SecretKey secretKey = secretKeyService.getById(req.getSecretKeyId());
        Credentials credentials = secretKeyService.getCredentials(secretKey, req.getPassword());
        BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
        KeyPath keyPath = new KeyPath(secretKey.getKeyPath());

        boolean mainNet = keyPath.getCoinType() == CoinType.BTC.getBipCoinType() ? true : false;

        TransactionBuilder builder = TransactionBuilder.create(mainNet);
        for(BtcTransferListReq btcTransferListReq : req.getOutputList()) {
            builder.from(
                btcTransferListReq.getTxId(),
                btcTransferListReq.getVout(),
                btcTransferListReq.getScriptPubKey(),
                btcTransferListReq.getAmount().longValue(),
                ECKey.fromPrivate(privateKey).getPrivateKeyAsWiF(getNetworkParameters(keyPath.getCoinType()))
            );
        }
        builder.to(req.getToAddress(), req.getAmount().longValue());
        builder.withFee(req.getFee().longValue());
        builder.changeTo(secretKey.getAddress());

        BtcTransferRsp btcTransferRsp = new BtcTransferRsp();
        btcTransferRsp.setRawTransaction(builder.build().getRawTransaction());

        return btcTransferRsp;
    }

}
