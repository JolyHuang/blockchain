package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.api.transaction.entity.UnregisterRsp;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.cube.support.service.base.IBaseService;


public interface AddressRegisterService extends IBaseService<AddressRegister, java.lang.String> {

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
	
}
