package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.Output;
import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.neemre.btcdcli4j.core.domain.SignatureResult;
import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import com.sharingif.cube.core.exception.UnknownCubeException;
import com.sharingif.cube.core.exception.validation.ValidationCubeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * BtcServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/7 下午12:37
 */
@Service
public class BtcServiceImpl implements BtcService {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private String btcNetType;
    private BtcdClient btcdClient;

    @Value("${btc.net.type}")
    public void setBtcNetType(String btcNetType) {
        this.btcNetType = btcNetType;
    }
    @Resource
    public void setBtcdClient(BtcdClient btcdClient) {
        this.btcdClient = btcdClient;
    }

    @Override
    public int getBipCoinType() {
        if(Constants.BTC_NET_TYPE_TEST.equals(btcNetType)) {
            return CoinType.BIP_BTC_TEST.getBipCoinType();
        }

        return CoinType.BTC.getBipCoinType();
    }

    @Override
    public Integer getBlockCount() {
        try {
            return btcdClient.getBlockCount();
        } catch (Exception e) {
            logger.error("get block number error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public String getBlockHash(Integer blockCount) {
        try {
            return btcdClient.getBlockHash(blockCount);
        } catch (Exception e) {
            logger.error("get block hash error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public Block getBlock(Integer blockCount) {
        String blockhash = getBlockHash(blockCount);

        return getBlock(blockhash);
    }

    @Override
    public Block getBlock(String blockhash) {
        try {
            return btcdClient.getBlock(blockhash);
        } catch (Exception e) {
            logger.error("get block error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public RawTransaction getRawTransaction(String txId) {
        try {
            return (RawTransaction)btcdClient.getRawTransaction(txId, 1);
        } catch (Exception e) {
            logger.error("get raw transaction error", e);
            throw new CubeRuntimeException(e);
        }
    }

    public BigInteger getReceivedByAddress(String address, Integer confirmations) {
        try {
            return btcdClient.getReceivedByAddress(address, confirmations).multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger();
        } catch (Exception e) {
            logger.error("get received by address error", e);
            throw new CubeRuntimeException(e);
        }
    }

    @Override
    public BigInteger getBalanceByAddress(String address) {
        BigInteger balance = BigInteger.ZERO;

        List<Output> outputs = null;
        try {
            outputs = btcdClient.listUnspent(2,9999999, Arrays.asList(address));
        } catch (Exception e) {
            logger.error("get getlistUnspent error", e);
            throw new CubeRuntimeException(e);
        }
        if(outputs == null) {
            return balance;
        }
        for(Output output : outputs) {
            balance = balance.add(output.getAmount().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
        }
        return balance;
    }

    public String signRawTransaction(String rawTransaction) {
        SignatureResult signatureResult;
        try {
            signatureResult = btcdClient.signRawTransaction(rawTransaction);
        } catch (Exception e) {
            logger.error("sign raw transaction error", e);
            throw new CubeRuntimeException(e);
        }

        if(!signatureResult.getComplete()) {
            logger.error("signature result error, signatureResult:{}", signatureResult);
            throw new UnknownCubeException();
        }

        return signatureResult.getHex();
    }

    public String sendRawTransaction(String signRawTransaction) {
        try {
            return btcdClient.sendRawTransaction(signRawTransaction);
        } catch (Exception e) {
            logger.error("send raw transaction error", e);
            throw new CubeRuntimeException(e);
        }
    }

    public List<Output> getListUnspent(String address, BigInteger amount, BigInteger fee) {
        BigInteger totalAmount = amount.add(fee);

        List<Output> outputs = null;
        try {
            outputs = btcdClient.listUnspent(2,9999999, Arrays.asList(address));
        } catch (Exception e) {
            logger.error("get getlistUnspent error", e);
            throw new CubeRuntimeException(e);
        }

        BigInteger outputAmount = BigInteger.ZERO;
        List<Output> amountOutputs = new ArrayList<Output>();
        for(Output output : outputs) {
            amountOutputs.add(output);
            outputAmount = outputAmount.add(output.getAmount().multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
            if(outputAmount.compareTo(totalAmount) >= 0) {
                return amountOutputs;
            }
        }

        logger.error("insufficient balance, address:{},amount:{},transfer amount:{}", address, outputAmount, totalAmount);
        throw new ValidationCubeException("insufficient balance");
    }

}
