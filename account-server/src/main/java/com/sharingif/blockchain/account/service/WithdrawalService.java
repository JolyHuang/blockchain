package com.sharingif.blockchain.account.service;


import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyRsp;
import com.sharingif.blockchain.account.model.entity.Withdrawal;
import com.sharingif.cube.support.service.base.IBaseService;


public interface WithdrawalService extends IBaseService<Withdrawal, java.lang.String> {

    /**
     * 取现
     * @param req
     */
    WithdrawalApplyRsp apply(WithdrawalApplyReq req);

}
