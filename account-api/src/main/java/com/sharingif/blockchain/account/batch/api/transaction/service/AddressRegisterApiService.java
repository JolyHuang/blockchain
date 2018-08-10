package com.sharingif.blockchain.account.batch.api.transaction.service;


import com.sharingif.blockchain.account.batch.api.transaction.entity.RegisterReq;
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
    void register(RegisterReq req);

}
