package com.sharingif.blockchain.account.service.impl;

import com.sharingif.blockchain.account.api.account.entity.AccountSysSetChangeExtendedKeyReq;
import com.sharingif.blockchain.account.api.account.entity.AccountSysSetWithdrawalReq;
import com.sharingif.blockchain.account.dao.AccountSysPrmDAO;
import com.sharingif.blockchain.account.model.entity.AccountSysPrm;
import com.sharingif.blockchain.account.service.AccountSysPrmService;
import com.sharingif.blockchain.crypto.model.entity.ExtendedKey;
import com.sharingif.blockchain.crypto.model.entity.KeyPath;
import com.sharingif.blockchain.crypto.model.entity.SecretKey;
import com.sharingif.blockchain.crypto.service.ExtendedKeyService;
import com.sharingif.blockchain.crypto.service.SecretKeyService;
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
    private ExtendedKeyService extendedKeyService;
    private SecretKeyService secretKeyService;

    @Resource
    public void setAccountSysPrmDAO(AccountSysPrmDAO accountSysPrmDAO) {
        super.setBaseDAO(accountSysPrmDAO);
        this.accountSysPrmDAO = accountSysPrmDAO;
    }
    @Resource
    public void setExtendedKeyService(ExtendedKeyService extendedKeyService) {
        this.extendedKeyService = extendedKeyService;
    }
    @Resource
    public void setSecretKeyService(SecretKeyService secretKeyService) {
        this.secretKeyService = secretKeyService;
    }

    @Override
    public String extendedKey(int coinType) {
        AccountSysPrm accountSysPrm = new AccountSysPrm();
        accountSysPrm.setPrmName(AccountSysPrm.CHANGE_EXTENDED_KEY_PREFIX+coinType);
        accountSysPrm.setPrmStatus(AccountSysPrm.PRM_STATUS_VALID);

        accountSysPrm = accountSysPrmDAO.query(accountSysPrm);

        return accountSysPrm.getPrmValue();
    }

    @Override
    public String withdrawalAccount(int coinType) {
        AccountSysPrm accountSysPrm = new AccountSysPrm();
        accountSysPrm.setPrmName(AccountSysPrm.WITHDRAWAL_SECRET_KEY_PREFIX+coinType);
        accountSysPrm.setPrmStatus(AccountSysPrm.PRM_STATUS_VALID);

        accountSysPrm = accountSysPrmDAO.query(accountSysPrm);

        return accountSysPrm.getPrmValue();
    }

    @Override
    public void setChangeExtendedKey(AccountSysSetChangeExtendedKeyReq req) {
        ExtendedKey extendedKey = extendedKeyService.getById(req.getChangeExtendedKeyId());

        KeyPath keyPath = new KeyPath(extendedKey.getExtendedKeyPath());

        AccountSysPrm accountSysPrm = new AccountSysPrm();
        accountSysPrm.setPrmName(AccountSysPrm.CHANGE_EXTENDED_KEY_PREFIX+keyPath.getCoinType());
        accountSysPrm.setPrmValue(req.getChangeExtendedKeyId());
        accountSysPrm.setPrmDesc(accountSysPrm.getPrmName());
        accountSysPrm.setPrmStatus(AccountSysPrm.PRM_STATUS_VALID);

        accountSysPrmDAO.insert(accountSysPrm);
    }

    @Override
    public void setWithdrawalAccount(AccountSysSetWithdrawalReq req) {
        SecretKey secretKey = secretKeyService.getById(req.getSecretKeyId());

        ExtendedKey extendedKey = extendedKeyService.getById(secretKey.getExtendedKeyId());

        KeyPath keyPath = new KeyPath(extendedKey.getExtendedKeyPath());


        AccountSysPrm accountSysPrm = new AccountSysPrm();
        accountSysPrm.setPrmName(AccountSysPrm.WITHDRAWAL_SECRET_KEY_PREFIX+keyPath.getCoinType());
        accountSysPrm.setPrmValue(secretKey.getId());
        accountSysPrm.setPrmDesc(accountSysPrm.getPrmName());
        accountSysPrm.setPrmStatus(AccountSysPrm.PRM_STATUS_VALID);

        accountSysPrmDAO.insert(accountSysPrm);
    }

}
