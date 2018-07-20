package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.account.api.transaction.entity.UnregisterRsp;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.cube.support.service.base.IBaseService;


public interface AddressRegisterService extends IBaseService<AddressRegister, String> {

    /**
     * 注册地址
     * @param req
     * @return
     */
    RegisterRsp register(RegisterReq req);

    /**
     * 解除注册地址
     * @param req
     * @return
     */
    UnregisterRsp unregister(UnregisterReq req);

    /**
     * 获取eth注册地址
     * @return
     */
    ETHAddressRegister getETHAddressRegister();

}
