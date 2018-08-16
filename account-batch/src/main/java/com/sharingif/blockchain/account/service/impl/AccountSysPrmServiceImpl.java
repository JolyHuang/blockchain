package com.sharingif.blockchain.account.service.impl;

import com.sharingif.blockchain.account.dao.AccountSysPrmDAO;
import com.sharingif.blockchain.account.model.entity.AccountSysPrm;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.cube.support.service.base.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * AccountSysPrmServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/8 下午4:23
 */
@Service
public class AccountSysPrmServiceImpl extends BaseServiceImpl<AccountSysPrm, String> implements AccountSysPrmService {

    private AccountSysPrmDAO accountSysPrmDAO;

    @Resource
    public void setAccountSysPrmDAO(AccountSysPrmDAO accountSysPrmDAO) {
        super.setBaseDAO(accountSysPrmDAO);
        this.accountSysPrmDAO = accountSysPrmDAO;
    }

    @Override
    public String getWithdrawalAccount(int coinType) {
        AccountSysPrm accountSysPrm = new AccountSysPrm();
        accountSysPrm.setPrmName(AccountSysPrm.WITHDRAWAL_SECRET_KEY_PREFIX+coinType);
        accountSysPrm.setPrmStatus(AccountSysPrm.PRM_STATUS_VALID);

        accountSysPrm = accountSysPrmDAO.query(accountSysPrm);

        return accountSysPrm.getPrmValue();
    }

}
