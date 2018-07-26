package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.cube.support.service.base.IBaseService;


public interface AddressNoticeService extends IBaseService<AddressNotice, String> {

    /**
     * 查询通知信息
     * @param address
     * @param coinType
     * @return
     */
    AddressNotice getDepositNoticeAddress(String address, String coinType);

}
