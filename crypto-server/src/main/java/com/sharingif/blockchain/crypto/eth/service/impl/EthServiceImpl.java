package com.sharingif.blockchain.crypto.eth.service.impl;

import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferReq;
import com.sharingif.blockchain.crypto.api.eth.entity.EthTransferRsp;
import com.sharingif.blockchain.crypto.eth.service.EthService;
import com.sharingif.blockchain.crypto.key.service.SecretKeyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.tx.Transfer;
import org.web3j.utils.Numeric;

import javax.annotation.Resource;

/**
 * EthServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:58
 */
public class EthServiceImpl implements EthService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private SecretKeyService secretKeyService;

    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }

    @Override
    public EthTransferRsp transfer(EthTransferReq req) {

        Credentials credentials = secretKeyService.getCredentials(req.getSecretKeyId(), req.getPassword());

        RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
                req.getNonce()
                ,req.getGasPrice()
                , Transfer.GAS_LIMIT
                ,req.getToAddress()
                ,req.getAmount()
        );

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);

        EthTransferRsp rsp = new EthTransferRsp();
        rsp.setHexValue(hexValue);

        return rsp;
    }

}
