package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.cube.support.service.base.IBaseService;

import java.util.List;


public interface AddressNoticeService extends IBaseService<AddressNotice, String> {

    /**
     * 注册通知地址
     * @param addressRegisterId
     * @param noticeList
     */
    void registerAddressNotice(String addressRegisterId, List<String> noticeList);

    /**
     * 解除注册通知地址
     * @param addressRegisterId
     */
    void unregisterAddressNotice(String addressRegisterId);

    /**
     * 查询通知信息
     * @param address
     * @param coinType
     * @param noticeType
     * @return
     */
    AddressNotice getDepositNoticeAddress(String address, String coinType);

}
