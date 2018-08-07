package com.sharingif.blockchain.transaction.service;

import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;

/**
 * TransactionBtcUtxoService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/6 下午6:16
 */
public interface TransactionBtcUtxoService extends IBaseService<TransactionBtcUtxo, String> {

    /**
     * 根据交易hash、区块号查询
     * @param txHash
     * @param blockNumber
     * @return
     */
    TransactionBtcUtxo getTransactionBtcUtxo(String txHash, BigInteger blockNumber);

}
