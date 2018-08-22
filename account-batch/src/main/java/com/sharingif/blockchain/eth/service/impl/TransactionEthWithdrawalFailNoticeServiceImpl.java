package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.account.model.entity.Account;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.blockchain.account.service.AccountService;
import com.sharingif.blockchain.common.constants.CoinType;
import com.sharingif.blockchain.transaction.model.entity.TransactionEth;
import com.sharingif.cube.core.util.StringUtils;
import com.sharingif.cube.persistence.database.pagination.PaginationCondition;
import com.sharingif.cube.persistence.database.pagination.PaginationRepertory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 取现失败通知
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午4:03
 */
@Service
public class TransactionEthWithdrawalFailNoticeServiceImpl extends AbstractTransactionEthWithdrawalNoticeServiceImpl {

    private AccountService accountService;

    @Resource
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    PaginationRepertory<TransactionEth> getPaginationRepertory(PaginationCondition<TransactionEth> paginationCondition) {
        return getTransactionEthService().getOutInvalid(paginationCondition);
    }

    @Override
    void updateTxStatus(Withdrawal withdrawal, String id) {
        getTransactionEthService().updateTxStatusToWithdrawalFailNotified(id);

        if(withdrawal != null) {
            Account ethAccount = accountService.getNormalAccountByAddress(withdrawal.getTxFrom(), CoinType.ETH.name());
            if(StringUtils.isTrimEmpty(withdrawal.getSubCoinType())) {
                accountService.unfreezeBalance(ethAccount.getId(), withdrawal.getAmount().add(withdrawal.getFee()));
            } else {
                accountService.unfreezeBalance(ethAccount.getId(), withdrawal.getFee());
                Account contractAccount = accountService.getNormalAccountByAddress(withdrawal.getTxFrom(), withdrawal.getSubCoinType());
                accountService.unfreezeBalance(contractAccount.getId(), withdrawal.getAmount());
            }
            getWithdrawalService().updateStatusToFail(withdrawal.getId());
        }
    }

    @Override
    void writeLoadDataLogger(PaginationRepertory<TransactionEth> paginationRepertory) {
        logger.info("eth withdrawal fail notice, totalCount:{}", paginationRepertory.getTotalCount());
    }

    @Override
    void writeError(Exception e) {
        logger.error("eth withdrawal fail notice error", e);
    }

}
