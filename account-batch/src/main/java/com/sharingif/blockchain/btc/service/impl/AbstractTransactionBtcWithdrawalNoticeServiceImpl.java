package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.transaction.model.entity.TransactionBtcUtxo;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * 提现通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午4:33
 */
public abstract class AbstractTransactionBtcWithdrawalNoticeServiceImpl extends AbstractTransactionBtcNoticeServiceImpl {

    private WithdrawalService withdrawalService;
    private DataSourceTransactionManager dataSourceTransactionManager;

    public WithdrawalService getWithdrawalService() {
        return withdrawalService;
    }
    @Resource
    public void setWithdrawalService(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }
    @Resource
    public void setDataSourceTransactionManager(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    protected void doTransaction(TransactionBtcUtxo transactionBtcUtxo) {
        Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionBtcUtxo.getTxHash());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 修改交易状态
            updateTxStatus(withdrawal, transactionBtcUtxo.getId());

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("btc withdrawal update txStatus error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }
    }

    @Deprecated
    @Override
    void updateTxStatus(String txHash) {

    }

    @Deprecated
    @Override
    void sendNotice(TransactionBtcUtxo transactionBtcUtxo) {

    }

    abstract void updateTxStatus(Withdrawal withdrawal, String id);

}
