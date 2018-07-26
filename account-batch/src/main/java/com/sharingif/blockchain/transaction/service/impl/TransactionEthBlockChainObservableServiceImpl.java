package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.common.components.ole.OleContract;
import com.sharingif.blockchain.common.components.ole.TransferEventResponse;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.eth.service.EthereumService;
import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;
import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.model.entity.CurrentBlockNumber;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.blockchain.transaction.service.BlockChainSyncService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import rx.functions.Action0;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * TransactionEthBlockChainObservableServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/13 下午5:02
 */
@Service
public class TransactionEthBlockChainObservableServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionEthDAO transactionEthDAO;
    private BlockChainSyncService blockChainSyncService;
    private AddressRegisterService addressRegisterService;
    private EthereumService ethereumService;
    private OleContract oleContract;
    private Boolean ethTransactionsObservableIsfinsh;

    @Resource
    public void setBlockChainSyncService(BlockChainSyncService blockChainSyncService) {
        this.blockChainSyncService = blockChainSyncService;
    }
    @Resource
    public void setAddressRegisterService(AddressRegisterService addressRegisterService) {
        this.addressRegisterService = addressRegisterService;
    }
    @Resource
    public void setTransactionEthDAO(TransactionEthDAO transactionEthDAO) {
        this.transactionEthDAO = transactionEthDAO;
    }
    @Resource
    public void setEthereumService(EthereumService ethereumService) {
        this.ethereumService = ethereumService;
    }

    @Resource
    public void setOleContract(OleContract oleContract) {
        this.oleContract = oleContract;
    }

    /**
     * 处理宕机重启
     */
    protected Boolean isDuplicationData(String txHash) {
        TransactionEth transactionEth =transactionEthDAO.queryById(txHash);

        if(transactionEth == null) {
            return false;
        }

        return true;
    }

    /**
     * 处理当前区块信息
     */
    protected void handleCurrentBlockNumber(Transaction tx, CurrentBlockNumber currentBlockNumber) {
        BigInteger txBlockNumber = tx.getBlockNumber();
        if(currentBlockNumber.getCurrentBlockNumber() == null || currentBlockNumber.getCurrentBlockNumber().compareTo(txBlockNumber) != 0) {

            logger.info("observable current block number,blockNumber:{}",txBlockNumber);

            currentBlockNumber.setCurrentBlockNumber(txBlockNumber);

            EthBlock.Block block = ethereumService.getBlock(txBlockNumber, false);

            currentBlockNumber.setTimestamp(block.getTimestamp());
            blockChainSyncService.setETHBlockChainCurrentNumber(txBlockNumber);

        }
    }

    protected boolean isListenETHTrans(TransactionEth transactionEth, ETHAddressRegister ethAddressRegister) {
        if(ethAddressRegister.isETHRegisterAddress(transactionEth.getTxFrom())) {
            transactionEth.setTxType(TransactionEth.TX_TYPE_OUT);

            return true;
        }

        if(transactionEth.getTxTo() != null && ethAddressRegister.isETHRegisterAddress(transactionEth.getTxTo())) {
            transactionEth.setTxType(TransactionEth.TX_TYPE_IN);

            return true;
        }

        return false;
    }

    protected boolean isListenContractTrans(TransactionEth transactionEth, ETHAddressRegister ethAddressRegister, TransactionReceipt transactionReceipt) {

        if(transactionEth.getTxTo() == null) {
            return false;
        }

        Map<String, String> subCoinTypeMap = ethAddressRegister.getSubCoinTypeMap(transactionEth.getTxTo());
        if(subCoinTypeMap == null) {
            return false;
        }

        List<TransferEventResponse> transferEventResponseList = oleContract.getTransferEvents(transactionReceipt);
        if(transferEventResponseList == null || transferEventResponseList.isEmpty()) {
            return false;
        }
        TransferEventResponse transferEventResponse = transferEventResponseList.get(0);

        transactionEth.setContractAddress(transactionEth.getTxTo());
        transactionEth.setTxTo(transferEventResponse.to);
        transactionEth.setTxValue(new BigInteger(transferEventResponse.value.toString()));
        transactionEth.setCoinType(oleContract.symbol());

        if(ethAddressRegister.isSubCoinType(subCoinTypeMap, transactionEth.getTxFrom())) {
            transactionEth.setTxType(TransactionEth.TX_TYPE_OUT);
            return true;
        }

        if(ethAddressRegister.isSubCoinType(subCoinTypeMap, transactionEth.getTxTo())) {
            transactionEth.setTxType(TransactionEth.TX_TYPE_IN);
            return true;
        }


        return false;
    }

    protected boolean isListenTrans(TransactionEth transactionEth, ETHAddressRegister ethAddressRegister, TransactionReceipt transactionReceipt) {

        boolean isListenContractTrans = isListenContractTrans(transactionEth, ethAddressRegister, transactionReceipt);

        if(isListenContractTrans) {
            return true;
        }

        return isListenETHTrans(transactionEth, ethAddressRegister);
    }

    protected TransactionEth convertBlockDateToTransactionEth(Transaction tx, CurrentBlockNumber currentBlockNumber, ETHAddressRegister ethAddressRegister) {

        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(tx.getHash());
        transactionEth.setBlockNumber(tx.getBlockNumber());
        transactionEth.setTxFrom(tx.getFrom());
        transactionEth.setTxTo(tx.getTo());
        transactionEth.setTxInput(tx.getInput());
        transactionEth.setTxIndex(tx.getTransactionIndex());
        transactionEth.setGasLimit(tx.getGas());
        transactionEth.setGasPrice(tx.getGasPrice());
        transactionEth.setNonce(tx.getNonce());
        transactionEth.setTxValue(new BigInteger(tx.getValue().toString()));
        transactionEth.setCoinType(CoinType.ETH.name());


        TransactionReceipt transactionReceipt;
        try {
            transactionReceipt = ethereumService.getTransactionReceipt(transactionEth.getTxHash());

            if(TransactionEth.RECEIPT_STATUS_FAIL.equals(transactionReceipt.getStatus())) {
                return null;
            }
        } catch (Throwable e) {
            logger.error("get transaction receipt error,trans info:{}", transactionEth, e);
            return null;
        }

        boolean isListenTrans = isListenTrans(transactionEth, ethAddressRegister, transactionReceipt);
        if(!isListenTrans) {
            return null;
        }

        // 区块时间
        Date timestamp = currentBlockNumber.getTimestamp();
        transactionEth.setGasUsed(transactionReceipt.getGasUsed());
        transactionEth.setTxReceiptStatus(TransactionEth.convertTxReceiptStatus(transactionReceipt.getStatus()));
        transactionEth.setTxTime(timestamp);
        transactionEth.setConfirmBlockNumber(0);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_UNTREATED);
        transactionEth.setTaskStatus(TransactionEth.TASK_STATUS_UNTREATED);
        transactionEth.setActualFee(transactionEth.getGasUsed().multiply(transactionEth.getGasPrice()));

        return transactionEth;
    }

    /**
     * 保存区块数据
     */
    @Transactional
    protected void persistenceAndNoticeTransactionEth(TransactionEth transactionEth) {
        transactionEthDAO.insert(transactionEth);
    }

    public void ethTransactionsObservable() {

        // 查看数据库是否有当前区块数据
        BlockChainSync blockChainSync = blockChainSyncService.getETHBlockChainSync();

        BigInteger startBlockNumber;
        // 如果没有区块数据获取当前区块数据插入数据库
        if(blockChainSync == null) {
            startBlockNumber = ethereumService.getBlockNumber();
            blockChainSyncService.addETHBlockChainSync(startBlockNumber);
        } else {
            startBlockNumber = blockChainSync.getCurrentSyncBlockNumber();
        }

        // 查询需要监听的数据
        ETHAddressRegister ethAddressRegister = addressRegisterService.getETHAddressRegister();
        CurrentBlockNumber currentBlockNumber = new CurrentBlockNumber();

        // 监听地址k
        ethereumService.getWeb3j().replayTransactionsObservable(new DefaultBlockParameterNumber(startBlockNumber), DefaultBlockParameterName.LATEST)
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    ethTransactionsObservableIsfinsh = true;
                }
            }).subscribe(tx -> {
            try {

                if(startBlockNumber.compareTo(tx.getBlockNumber()) ==0 && isDuplicationData(tx.getHash())) {
                    return;
                }

                handleCurrentBlockNumber(tx, currentBlockNumber);

                TransactionEth transactionEth = convertBlockDateToTransactionEth(tx, currentBlockNumber, ethAddressRegister);

                if(transactionEth != null) {
                    persistenceAndNoticeTransactionEth(transactionEth);
                }


            } catch (Throwable e) {
                logger.error("subscribe error", e);
                throw e;
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (ethTransactionsObservableIsfinsh == null || ethTransactionsObservableIsfinsh) {
                        ethTransactionsObservableIsfinsh = false;
                        ethTransactionsObservable();
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        logger.error("ethTransactionsObservable error", e);
                    }
                }

            }
        }).start();
    }

}