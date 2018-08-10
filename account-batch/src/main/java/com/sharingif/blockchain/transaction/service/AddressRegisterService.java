package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.account.api.transaction.entity.RegisterReq;
import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.cube.support.service.base.IBaseService;

import java.util.Map;


public interface AddressRegisterService extends IBaseService<AddressRegister, String> {

    /**
     * 注册地址
     * @param req
     */
    void register(RegisterReq req);

    /**
     * 获取ETH注册地址
     * @return
     */
    ETHAddressRegister getETHAddressRegister();

    /**
     * 获取BTC注册地址
     * @return
     */
    Map<String, String> getBTCAddressRegister();

}
