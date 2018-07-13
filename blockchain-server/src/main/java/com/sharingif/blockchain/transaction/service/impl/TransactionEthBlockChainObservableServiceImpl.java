package com.sharingif.blockchain.transaction.service.impl;

import com.sharingif.blockchain.app.components.erc20.ole.OleContract;
import com.sharingif.blockchain.app.components.erc20.ole.TransferEventResponse;
import com.sharingif.blockchain.eth.service.Web3jService;
import com.sharingif.blockchain.transaction.dao.TransactionEthDAO;
import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.model.entity.CurrentBlockNumber;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.blockchain.transaction.service.BlockChainSyncService;
import com.sharingif.blockchain.transaction.service.TransactionEthBlockChainObservableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import rx.Subscription;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;

/**
 * TransactionEthBlockChainObservableServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/13 下午5:02
 */
@Service
public class TransactionEthBlockChainObservableServiceImpl implements TransactionEthBlockChainObservableService, InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private TransactionEthDAO transactionEthDAO;
    private Web3jService web3jService;
    private BlockChainSyncService blockChainSyncService;
    private AddressRegisterService addressRegisterService;
    private OleContract oleContract;

    @Resource
    public void setWeb3jService(Web3jService web3jService) {
        this.web3jService = web3jService;
    }
    @Resource
    public void setBlockChainSyncService(BlockChainSyncService blockChainSyncService) {
        this.blockChainSyncService = blockChainSyncService;
    }
    @Resource
    public void setAddressRegisterService(AddressRegisterService addressRegisterService) {
        this.addressRegisterService = addressRegisterService;
    }

    public TransactionEthDAO getTransactionEthDAO() {
        return transactionEthDAO;
    }
    @Resource
    public void setOleContract(OleContract oleContract) {
        this.oleContract = oleContract;
    }

    protected void transactionsObservable() {
        try {
            ethTransactionsObservable();
        } catch (IOException e) {
            logger.error("eth transactions observable error", e);
            throw new RuntimeException(e);
        }
    }

    protected void a() {
        TransactionEth transactionEth = new TransactionEth();
        transactionEth.setTxHash(txHash);
        transactionEth.setBlockNumber(txBlockNumber);
        transactionEth.setTxFrom(form);
        transactionEth.setTxTo(to);
        transactionEth.setContractAddress(contractAddress);
        transactionEth.setTxInput(tx.getInput());
        transactionEth.setTxValue(value);
        transactionEth.setTxIndex(tx.getTransactionIndex());
        transactionEth.setGasLimit(tx.getGas());
        transactionEth.setGasUsed(transactionReceipt.getGasUsed());
        transactionEth.setGasPrice(tx.getGasPrice());
        transactionEth.setActualFee(tx.getGas().multiply(tx.getGasPrice()));
        transactionEth.setNonce(tx.getNonce());
        transactionEth.setTxReceiptStatus(transactionReceipt.getStatus());
        transactionEth.setTxTime(currentBlockNumber.getTimestamp());
        transactionEth.setConfirmBlockNumber(0);
        transactionEth.setTxStatus(TransactionEth.TX_STATUS_UNTREATED);
        transactionEth.setTxType(txType);
    }

    public void ethTransactionsObservable() throws IOException {
        // 查看数据库是否有当前区块数据
        BlockChainSync blockChainSync = blockChainSyncService.getETHBlockChainSync();

        // 获取可以webj服务
        Web3j web3j = web3jService.getCurrentAvailableWeb3j();

        BigInteger blockNumber;
        // 如果没有区块数据获取当前区块数据插入数据库
        if(blockChainSync == null) {
            blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
            blockChainSyncService.addETHBlockChainSync(blockNumber);
        } else {
            blockNumber = blockChainSync.getCurrentSyncBlockNumber();
        }

        // 查询需要监听的数据
        ETHAddressRegister ethAddressRegister = addressRegisterService.getETHAddressRegister();
        final CurrentBlockNumber currentBlockNumber = new CurrentBlockNumber();

        // 监听地址k
        web3j.replayTransactionsObservable(new DefaultBlockParameterNumber(blockNumber), DefaultBlockParameterName.LATEST)
            .subscribe(tx -> {
                BigInteger txBlockNumber = tx.getBlockNumber();
                if(currentBlockNumber.getCurrentBlockNumber() == null || currentBlockNumber.getCurrentBlockNumber().compareTo(txBlockNumber) != 0) {
                    currentBlockNumber.setCurrentBlockNumber(txBlockNumber);

                    try {
                        EthBlock.Block block = web3j.ethGetBlockByNumber(new DefaultBlockParameterNumber(txBlockNumber), false).send().getBlock();

                        blockChainSyncService.setETHBlockChainCurrentNumber(txBlockNumber);
                        currentBlockNumber.setCurrentBlockNumber(block.getTimestamp());
                    } catch (IOException e) {
                        logger.error("get block number error", e);
                        throw new RuntimeException(e);
                    }

                }
                // 区块时间

                String txHash = tx.getHash();
                String contractAddress = null;
                String form = tx.getFrom();
                String to = tx.getTo();
                BigInteger value = tx.getValue();
                String txType;

                TransactionReceipt transactionReceipt;
                try {
                    transactionReceipt = web3j.ethGetTransactionReceipt(txHash).send().getTransactionReceipt().get();
                } catch (IOException e) {
                    logger.error("get transaction receipt error", e);
                    throw new RuntimeException(e);
                }

                Map<String, String> subCoinTypeMap = ethAddressRegister.getContractAddressMap().get(to);
                if(subCoinTypeMap != null) {
                    contractAddress = to;
                    txType = TransactionEth.TX_TYPE_IN;
                    List<TransferEventResponse> transferEventResponseList = oleContract.getTransferEvents(transactionReceipt);
                    TransferEventResponse transferEventResponse = transferEventResponseList.get(0);
                    to = transferEventResponse.to;
                    value = transferEventResponse.value;
                    BigInteger decimals = oleContract.decimals();
                    value = value.divide(decimals);
                }

                if(ethAddressRegister.getEthMap().get(form) != null) {
                    txType = TransactionEth.TX_TYPE_OUT;
                } else {
                    if(ethAddressRegister.getEthMap().get(to) != null)) {
                        txType = TransactionEth.TX_TYPE_IN;
                    }
                }


            });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                transactionsObservable();

            }
        }).start();
    }

}
