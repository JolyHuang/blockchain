package com.sharingif.blockchain.api.transaction.service;


import com.sharingif.blockchain.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.api.transaction.entity.RegisterRsp;
import com.sharingif.blockchain.api.transaction.entity.UnregisterReq;
import com.sharingif.blockchain.api.transaction.entity.UnregisterRsp;
import com.sharingif.cube.core.handler.bind.annotation.RequestMapping;
import com.sharingif.cube.core.handler.bind.annotation.RequestMethod;

@RequestMapping(value="address")
public interface AddressRegisterApiService {

    /**
     * 注册地址
     * @param req
     * @return
     */
    @RequestMapping(value="register", method= RequestMethod.POST)
    RegisterRsp register(RegisterReq req);

    /**
     * 解除注册地址
     * @param req
     * @return
     */
    @RequestMapping(value="unregister", method= RequestMethod.POST)
    UnregisterRsp unregister(UnregisterReq req);
	
}
