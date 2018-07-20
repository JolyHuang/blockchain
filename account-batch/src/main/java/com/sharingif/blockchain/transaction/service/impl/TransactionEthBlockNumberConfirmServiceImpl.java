package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.TransactionEthService;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

/**
 * 交易区块数确认
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/17 下午4:32
 */
@Service
public class TransactionEthBlockNumberConfirmServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private int validBlockNumber;
    private TransactionEthService transactionEthService;
    private EthereumService ethereumService;

    @Value("${eth.valid.block.number}")
    public void setValidBlockNumber(int validBlockNumber) {
        this.validBlockNumber = validBlockNumber;
    }
    @Resource
    public void setTransactionEthService(TransactionEthService transactionEthService) {
        this.transactionEthService = transactionEthService;
    }
    @Resource
    public void setEthereumService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }


    protected void confirmTransactionEthBlockNumber(BigInteger currentBlockNumber, PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            BigInteger blockNumber = transactionEth.getBlockNumber();
            int confirmBlockNumber = transactionEth.getConfirmBlockNumber();
            BigInteger txConfirmBlockNumber = blockNumber.add(new BigInteger(String.valueOf(confirmBlockNumber)));

            if(currentBlockNumber.compareTo(txConfirmBlockNumber) <= 0) {
                continue;
            }

            Transaction transaction = null;
            try {
                transaction = ethereumService.getTransactionByHash(transactionEth.getTxHash());
            } catch (Exception e) {
                logger.error("validate transaction confirm block number error", e);
                transactionEthService.updateTxStatusToInvalid(transactionEth.getTxHash());
            }
            if(!transactionEth.getTxFrom().equals(transaction.getFrom())) {
                transactionEthService.updateTxStatusToInvalid(transactionEth.getTxHash());
                continue;
            }
            if(!transactionEth.getTxIndex().equals(transaction.getTransactionIndex())) {
                transactionEthService.updateTxStatusToInvalid(transactionEth.getTxHash());
                continue;
            }
            if(!transactionEth.getTxInput().equals(transaction.getInput())) {
                transactionEthService.updateTxStatusToInvalid(transactionEth.getTxHash());
                continue;
            }

            if(currentBlockNumber.compareTo(blockNumber.add(new BigInteger(String.valueOf(validBlockNumber)))) > 0) {
                transactionEthService.updateTxStatusToBalanceUnconfirm(transactionEth.getTxHash(), currentBlockNumber.subtract(transaction.getBlockNumber()).intValue());
                continue;
            } else {
                transactionEthService.updateConfirmBlockNumber(transactionEth.getTxHash(), currentBlockNumber.subtract(transaction.getBlockNumber()).intValue());
            }
        }
    }

    protected void confirmTransactionEthBlockNumber() {

        BigInteger currentBlockNumber = ethereumService.getBlockNumber();

        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            PaginationRepertory<TransactionEth> paginationRepertory = transactionEthService.getUnconfirmedBlockNumber(paginationCondition);

            if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    logger.error("get unconfirmed block number error", e);
                }
            }

            confirmTransactionEthBlockNumber(currentBlockNumber, paginationRepertory);

            if(paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex()+paginationCondition.getPageSize())) {
                paginationCondition.setCurrentPage(1);
            } else {
                paginationCondition.setCurrentPage(paginationCondition.getCurrentPage()+1);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                confirmTransactionEthBlockNumber();

            }
        }).start();
    }
}
