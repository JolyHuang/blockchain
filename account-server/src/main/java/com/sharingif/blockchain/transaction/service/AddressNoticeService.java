package com.sharingif.blockchain.transaction.service;


import com.sharingif.blockchain.transaction.model.entity.AddressNotice;
import com.sharingif.cube.support.service.base.IBaseService;


public interface AddressNoticeService extends IBaseService<AddressNotice, String> {

    /**
     * 注册通知地址
     * @param addressRegisterId
     * @param noticeAddress
     */
    void registerDepositAddressNotice(String addressRegisterId, String noticeAddress, String address, String coinType);

    /**
     * 提现通知地址
     * @param addressRegisterId
     * @param noticeAddress
     */
    void registerWithdrawalAddressNotice(String addressRegisterId, String noticeAddress, String address, String coinType);

    /**
     * 解除注册通知地址
     * @param addressRegisterId
     */
    void unregisterAddressNotice(String addressRegisterId);


}
