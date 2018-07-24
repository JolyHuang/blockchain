package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressRegister;
import com.sharingif.blockchain.transaction.model.entity.ETHAddressRegister;
import com.sharingif.cube.support.service.base.IBaseService;


public interface AddressRegisterService extends IBaseService<AddressRegister, String> {

    /**
     * 获取eth注册地址
     * @return
     */
    ETHAddressRegister getETHAddressRegister();

}
