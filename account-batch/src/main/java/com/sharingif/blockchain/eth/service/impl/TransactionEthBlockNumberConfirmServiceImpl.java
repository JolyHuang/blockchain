package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.eth.service.TransactionEthService;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.methods.response.Transaction;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.List;
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
    private OleContract oleContract;

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
    @Resource
    public void setOleContract(OleContract oleContract) {
        this.oleContract = oleContract;
    }

    protected boolean validateTransaction(TransactionEth transactionEth, Transaction transaction) {
        boolean isContractTrans = !StringUtils.isTrimEmpty(transactionEth.getContractAddress());
        List<Type> transferResponseList = null;
        if (isContractTrans) {

            transferResponseList = oleContract.getTransfer(transactionEth.getTxInput());
            if(transferResponseList == null || transferResponseList.isEmpty()) {
                return false;
            }
        }

        if(!transactionEth.getTxFrom().toLowerCase().equals(transaction.getFrom().toLowerCase())) {
            return false;
        }
        if(isContractTrans) {
            Address address = (Address)transferResponseList.get(0);
            if(!transactionEth.getTxTo().toLowerCase().equals(address.getValue().toLowerCase())) {
                return false;
            }
        } else {
            if(!transactionEth.getTxTo().toLowerCase().equals(transaction.getTo().toLowerCase())) {
                return false;
            }
        }
        if(!transactionEth.getTxIndex().equals(transaction.getTransactionIndex())) {
            return false;
        }
        if(!transactionEth.getTxInput().equals(transaction.getInput())) {
            return false;
        }
        if(isContractTrans) {
            Uint256 amount = (Uint256)transferResponseList.get(1);
            if(transactionEth.getTxValue().compareTo(amount.getValue()) != 0) {
                return false;
            }
        } else {
            if(transactionEth.getTxValue().compareTo(transaction.getValue()) != 0) {
                return false;
            }
        }

        return true;
    }

    protected void confirmTransactionEthBlockNumber(TransactionEth transactionEth) {
        BigInteger currentBlockNumber = ethereumService.getBlockNumber();
        BigInteger blockNumber = transactionEth.getBlockNumber();
        int confirmBlockNumber = transactionEth.getConfirmBlockNumber();
        BigInteger txConfirmBlockNumber = blockNumber.add(new BigInteger(String.valueOf(confirmBlockNumber)));

        if(currentBlockNumber.compareTo(txConfirmBlockNumber) <= 0) {
            return;
        }

        Transaction transaction = null;
        try {
            transaction = ethereumService.getTransactionByHash(transactionEth.getTxHash());
        } catch (Exception e) {
            logger.error("eth validate transaction confirm block number error, transactionEth:{}", transactionEth, e);
            transactionEthService.updateTxStatusToInvalid(transactionEth.getId());
            return;
        }

        boolean validateTransactionStatus = validateTransaction(transactionEth, transaction);
        if(!validateTransactionStatus) {
            this.logger.info("eth validate transaction content error, transactionEth:{}", transactionEth);
            transactionEthService.updateTxStatusToInvalid(transactionEth.getId());
            return;
        }

        if(currentBlockNumber.compareTo(blockNumber.add(new BigInteger(String.valueOf(validBlockNumber)))) > 0) {
            transactionEthService.updateTxStatusToBalanceUnconfirm(transactionEth.getId(), currentBlockNumber.subtract(transaction.getBlockNumber()).intValue());
            return;
        } else {
            transactionEthService.updateConfirmBlockNumber(transactionEth.getId(), currentBlockNumber.subtract(transaction.getBlockNumber()).intValue());
            return;
        }
    }

    protected void confirmTransactionEthBlockNumber(PaginationRepertory<TransactionEth> paginationRepertory) {
        if(paginationRepertory == null || paginationRepertory.getPageItems() == null) {
            return;
        }

        for(TransactionEth transactionEth : paginationRepertory.getPageItems()) {
            confirmTransactionEthBlockNumber(transactionEth);
        }
    }

    protected void confirmTransactionEthBlockNumber() {

        PaginationCondition<TransactionEth> paginationCondition = new PaginationCondition<TransactionEth>();
        TransactionEth queryTransactionEth = new TransactionEth();
        paginationCondition.setCurrentPage(1);
        paginationCondition.setPageSize(100);
        paginationCondition.setCondition(queryTransactionEth);

        while (true){
            try {
                PaginationRepertory<TransactionEth> paginationRepertory = transactionEthService.getUnconfirmedBlockNumber(paginationCondition);

                if (paginationRepertory == null || paginationRepertory.getPageItems() == null) {
                    try {
                        TimeUnit.SECONDS.sleep(5);
                    } catch (InterruptedException e) {
                        logger.error("get unconfirmed block number error", e);
                    }
                }

                confirmTransactionEthBlockNumber(paginationRepertory);

                if (paginationRepertory.getTotalCount() < (paginationRepertory.getCurrentIndex() + paginationCondition.getPageSize())) {
                    paginationCondition.setCurrentPage(1);
                } else {
                    paginationCondition.setCurrentPage(paginationCondition.getCurrentPage() + 1);
                }
            }catch (Exception e) {
                logger.error("confirm transaction eth block number error", e);
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
