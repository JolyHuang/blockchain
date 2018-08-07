package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.transaction.dao.TransactionBtcUtxoDAO;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.service.TransactionBtcUtxoService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;

import javax.annotation.Resource;
import java.math.BigInteger;

/**
 * TransactionBtcUtxoServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/7 下午4:34
 */
public class TransactionBtcUtxoServiceImpl extends BaseServiceImpl<TransactionBtcUtxo, String> implements TransactionBtcUtxoService {

    private TransactionBtcUtxoDAO transactionBtcUtxoDAO;

    @Resource
    public void setTransactionBtcUtxoDAO(TransactionBtcUtxoDAO transactionBtcUtxoDAO) {
        super.setBaseDAO(transactionBtcUtxoDAO);
        this.transactionBtcUtxoDAO = transactionBtcUtxoDAO;
    }


    @Override
    public TransactionBtcUtxo getTransactionBtcUtxo(String txHash, BigInteger blockNumber) {
        TransactionBtcUtxo transactionBtcUtxo = new TransactionBtcUtxo();
        transactionBtcUtxo.setTxHash(txHash);
        transactionBtcUtxo.setBlockNumber(blockNumber);

        return transactionBtcUtxoDAO.query(transactionBtcUtxo);
    }
}
