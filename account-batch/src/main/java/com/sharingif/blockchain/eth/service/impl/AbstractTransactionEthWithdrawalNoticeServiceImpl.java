package com.sharingif.blockchain.eth.service.impl;

import co.olivecoin.wallet.api.blockchain.entity.TransactionEthWithdrawalApiReq;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.WithdrawalService;
import com.sharingif.blockchain.app.components.UrlBody;
import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * 提现通知服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:06
 */
public abstract class AbstractTransactionEthWithdrawalNoticeServiceImpl extends AbstractTransactionEthNoticeServiceImpl {

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


    protected void doTransactionEth(TransactionEth transactionEth) {
        // 获取取现id
        Withdrawal withdrawal = withdrawalService.getWithdrawalByTxHash(transactionEth.getTxHash());

        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = dataSourceTransactionManager.getTransaction(def);
        try {
            // 修改交易状态
            updateTxStatus(withdrawal, transactionEth.getId());

            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            logger.error("eth withdrawal update txStatus error, withdrawal:{}, exception:{}", withdrawal, e);
            dataSourceTransactionManager.rollback(status);
            throw e;
        }
    }

    @Deprecated
    @Override
    void updateTxStatus(String id) {

    }

    @Deprecated
    @Override
    void sendNotice(TransactionEth transactionEth) {

    }

    abstract void updateTxStatus(Withdrawal withdrawal, String id);

}
