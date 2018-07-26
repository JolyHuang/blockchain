package com.sharingif.blockchain.account.service;

import com.sharingif.blockchain.account.api.account.entity.AccountSysSetChangeExtendedKeyReq;
import com.sharingif.blockchain.account.api.account.entity.AccountSysSetWithdrawalReq;
import com.sharingif.blockchain.account.model.entity.AccountSysPrm;
import com.sharingif.cube.support.service.base.IBaseService;

/**
 * AccountSysPrmService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/8 下午4:23
 */
public interface AccountSysPrmService extends IBaseService<AccountSysPrm, String> {

    /**
     * 默认ExtendedKey前缀
     * @param coinType
     * @return
     */
    String extendedKey(int coinType);

    /**
     * 取现账户
     * @param coinType
     * @return
     */
    String withdrawalAccount(int coinType);

    /**
     * 设置当前change ExtendedKey
     * @param req
     */
    void setChangeExtendedKey(AccountSysSetChangeExtendedKeyReq req);

    /**
     * 设置取现地址
     * @param req
     */
    void setWithdrawalAccount(AccountSysSetWithdrawalReq req);

}
