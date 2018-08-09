package com.sharingif.blockchain.btc.service.impl;

import com.neemre.btcdcli4j.core.domain.*;
import com.neemre.btcdcli4j.core.domain.enums.ScriptTypes;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.transaction.model.entity.BlockChainSync;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import com.sharingif.blockchain.transaction.service.AddressRegisterService;
import com.sharingif.blockchain.transaction.service.BlockChainSyncService;
import com.sharingif.blockchain.btc.service.TransactionBtcUtxoService;
import com.sharingif.cube.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * btc 交易转换为utxo处理
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/6 下午6:12
 */
@Service
public class TransactionBtcBlockChainObservableServiceImpl implements InitializingBean {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final int FIXED_THREAD_POOL_NUMBER = 20;
    private ExecutorService pool = Executors.newFixedThreadPool(FIXED_THREAD_POOL_NUMBER);
    private AtomicInteger workThreadNumber = new AtomicInteger(0);

    private BlockChainSyncService blockChainSyncService;
    private BtcService btcService;
    private AddressRegisterService addressRegisterService;
    private TransactionBtcUtxoService transactionBtcUtxoService;

    @Resource
    public void setBlockChainSyncService(BlockChainSyncService blockChainSyncService) {
        this.blockChainSyncService = blockChainSyncService;
    }
    @Resource
    public void setBtcService(BtcService btcService) {
        this.btcService = btcService;
    }
    @Resource
    public void setAddressRegisterService(AddressRegisterService addressRegisterService) {
        this.addressRegisterService = addressRegisterService;
    }
    @Resource
    public void setTransactionBtcUtxoService(TransactionBtcUtxoService transactionBtcUtxoService) {
        this.transactionBtcUtxoService = transactionBtcUtxoService;
    }

    private void workFinish() {
        workThreadNumber.decrementAndGet();
    }

    protected void addTransactionBtcUtxo(String txHash, BigInteger blockNumber, Long time, BigInteger actualFee, TransactionBtcUtxo utxo) {
        TransactionBtcUtxo queryTransactionBtcUtxo = transactionBtcUtxoService.getTransactionBtcUtxo(txHash, blockNumber);
        if(queryTransactionBtcUtxo != null) {
            return;
        }

        utxo.setTxHash(txHash);
        utxo.setBlockNumber(blockNumber);
        utxo.setActualFee(actualFee);
        utxo.setTxTime(new Date(time));
        utxo.setConfirmBlockNumber(0);
        utxo.setTxStatus(TransactionBtcUtxo.TX_STATUS_UNTREATED);
        utxo.setTaskStatus(TransactionBtcUtxo.TASK_STATUS_UNTREATED);

        transactionBtcUtxoService.add(utxo);
    }

    protected TransactionBtcUtxo handleUTXO(RawOutput rawOutput, Map<String, String> ethAddressRegisterMap){
        BigDecimal value = rawOutput.getValue();

        PubKeyScript scriptPubKey = rawOutput.getScriptPubKey();
        ScriptTypes scriptTypes = scriptPubKey.getType();
        List<String> addresses = scriptPubKey.getAddresses();

        if((!scriptTypes.getName().equals(ScriptTypes.PUB_KEY_HASH.getName()) && !scriptTypes.getName().equals(ScriptTypes.SCRIPT_HASH.getName())) || addresses.size()>1) {
            logger.error("script types error, RawOutput:{}", rawOutput);

            return null;
        }

        String address = addresses.get(0);

        if(ethAddressRegisterMap.get(address) == null) {
            return null;
        }

        TransactionBtcUtxo utxo = new TransactionBtcUtxo();
        utxo.setTxTo(address);
        utxo.setTxValue(value.multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger());
        utxo.setTxIndex(new BigInteger(rawOutput.getN().toString()));

        return utxo;
    }

    protected void handlerIn(RawOutput rawOutput, String txHash, BigInteger blockNumber, Long time, BigInteger actualFee, Map<String, String> ethAddressRegisterMap) {
        TransactionBtcUtxo utxo = handleUTXO(rawOutput, ethAddressRegisterMap);
        if(utxo == null) {
            return;
        }


        utxo.setTxFrom(utxo.getTxTo());
        utxo.setTxTo(null);
        utxo.setTxType(TransactionBtcUtxo.TX_TYPE_OUT);

        addTransactionBtcUtxo(txHash, blockNumber, time, actualFee, utxo);
    }

    protected void handlerOut(RawOutput rawOutput, String txHash, BigInteger blockNumber, Long time, BigInteger actualFee, Map<String, String> ethAddressRegisterMap) {
        TransactionBtcUtxo utxo = handleUTXO(rawOutput, ethAddressRegisterMap);
        if(utxo == null) {
            return;
        }

        utxo.setTxType(TransactionBtcUtxo.TX_TYPE_IN);
        utxo.setActualFee(BigInteger.ZERO);

        addTransactionBtcUtxo(txHash, blockNumber, time, actualFee, utxo);
    }

    protected void btcTransactionsObservable(RawTransaction rawTransaction, BigInteger blockNumber, Long time) {

        String txHash = rawTransaction.getTxId();

        // 查询需要监听的数据
        Map<String, String> ethAddressRegisterMap = addressRegisterService.getBTCAddressRegister();

        List<RawInput> vInRawInput = rawTransaction.getVIn();
        List<RawOutput> vOut = rawTransaction.getVOut();

        List<RawOutput> vIn = new ArrayList<RawOutput>(vInRawInput.size());
        for(RawInput rawInput : vInRawInput) {
            Integer vOutIndex = rawInput.getVOut();
            String txId = rawInput.getTxId();
            if(StringUtils.isTrimEmpty(txId)) {
                continue;
            }
            try {
                RawTransaction inRawTransaction = btcService.getRawTransaction(txId);
                RawOutput rawOutput = inRawTransaction.getVOut().get(vOutIndex);
                vIn.add(rawOutput);
            }catch (Exception e) {
                continue;
            }
        }

        BigDecimal inValue = BigDecimal.ZERO;
        for(RawOutput rawOutput : vIn) {
            inValue = inValue.add(rawOutput.getValue());
        }

        BigInteger actualFee = BigInteger.ZERO;
        if(inValue.compareTo(BigDecimal.ZERO) != 0) {
            BigDecimal outValue = BigDecimal.ZERO;
            for(RawOutput rawOutput : vOut) {
                outValue = outValue.add(rawOutput.getValue());
            }
            actualFee = inValue.subtract(outValue).multiply(TransactionBtcUtxo.BTC_UNIT).toBigInteger();
        }

        // 判断交易vin中是否存在观察地址，如果有存入数据库
        for(RawOutput rawOutput : vIn) {
            handlerIn(rawOutput, txHash, blockNumber, time, actualFee, ethAddressRegisterMap);
        }

        // 判断交易out中是否存在观察地址，如果有存入数据库
        for(RawOutput rawOutput : vOut) {
            handlerOut(rawOutput, txHash, blockNumber, time, actualFee, ethAddressRegisterMap);
        }
    }

    protected List<List<String>> splitTxList( List<String> txList) {
        List<List<String>> splitList = new ArrayList<List<String>>(FIXED_THREAD_POOL_NUMBER);
        int listSize = (txList.size()/FIXED_THREAD_POOL_NUMBER)+1;
        List<String> list = null;
        for(int i=0; i<txList.size(); i++) {
            if(i % listSize == 0) {
                list = new ArrayList<String>();
                splitList.add(list);
            }
            list.add(txList.get(i));
        }
        return splitList;
    }

    protected void btcTransactionsObservable(List<List<String>> splitList, BigInteger blockNumber, Long time) {
        for(List<String> txList : splitList) {
            pool.execute(new Runnable() {
                @Override
                public void run() {
                    try {

                        for(String tx : txList) {
                            try {
                                logger.info("=============>tx:{}",tx);
                                RawTransaction rawTransaction = btcService.getRawTransaction(tx);
                                btcTransactionsObservable(rawTransaction, blockNumber, time);
                            }catch (Exception e) {
                                continue;
                            }
                        }

                    } finally {
                        workFinish();
                    }
                }
            });
        }
    }

    protected void btcTransactionsObservable(Block block, BigInteger blockNumber) {

        Long time = block.getTime();

        // 根据区块信息查询区块交易
        List<String> txList = block.getTx();
        List<List<String>> splitList = splitTxList(txList);
        workThreadNumber.getAndSet(splitList.size());

        btcTransactionsObservable(splitList, blockNumber, time);

    }

    protected void btcTransactionsObservable() {
        // 查看数据库是否有当前区块数据
        BlockChainSync blockChainSync = blockChainSyncService.getBTCBlockChainSync();

        // 如果没有区块数据获取当前区块数据插入数据库
        Integer startBlockNumber;
        Integer blockNumber = btcService.getBlockCount();
        if(blockChainSync == null) {
            startBlockNumber = blockNumber;
            blockChainSyncService.addBTCBlockChainSync(new BigInteger(startBlockNumber.toString()));
        } else {
            startBlockNumber = blockChainSync.getCurrentSyncBlockNumber().intValue();
        }

        // 根据区块数据查询区块信息
        while (true) {
            try {

                if(workThreadNumber.get() != 0) {
                    threadSleep(1);
                    continue;
                }

                // 判断当前块数是否与区块链区块数相等，如果相等不处理
                if (startBlockNumber > blockNumber) {
                    threadSleep(60);
                    blockNumber = btcService.getBlockCount();
                    continue;
                }
                logger.info("observable current btc block number,blockNumber:{}",startBlockNumber);

                Block block = btcService.getBlock(startBlockNumber);
                btcTransactionsObservable(block, new BigInteger(startBlockNumber.toString()));
                blockChainSyncService.setBTCBlockChainCurrentNumber(new BigInteger(startBlockNumber.toString()));

                blockNumber = btcService.getBlockCount();
                startBlockNumber++;

            } catch (Throwable e) {
                logger.error("btc subscribe error", e);
            }
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {

                btcTransactionsObservable();

            }
        }).start();
    }

    protected void threadSleep(int timeout) {
        try {
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            logger.error("ethTransactionsObservable error", e);
        }
    }

}
