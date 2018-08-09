package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import com.neemre.btcdcli4j.core.client.BtcdClient;
import com.neemre.btcdcli4j.core.domain.Block;
import com.neemre.btcdcli4j.core.domain.RawTransaction;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.core.exception.CubeRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigInteger;

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

    private BtcdClient btcdClient;

    @Resource
    public void setBtcdClient(BtcdClient btcdClient) {
        this.btcdClient = btcdClient;
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
            return (RawTransaction)btcdClient.getRawTransaction(txId, true);
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

}
