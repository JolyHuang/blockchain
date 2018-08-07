package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.cube.support.service.base.IBaseService;

import java.util.Map;


public interface AddressRegisterService extends IBaseService<AddressRegister, String> {

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
