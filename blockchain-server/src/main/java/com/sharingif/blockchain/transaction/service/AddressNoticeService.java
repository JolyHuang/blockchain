package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.cube.support.service.base.IBaseService;

import java.util.List;


public interface AddressNoticeService extends IBaseService<AddressNotice, java.lang.String> {

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

}
