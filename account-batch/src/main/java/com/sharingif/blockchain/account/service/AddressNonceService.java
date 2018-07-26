package com.sharingif.blockchain.account.service;

import com.sharingif.blockchain.account.model.entity.AddressNonce;
import com.sharingif.cube.support.service.base.IBaseService;

import java.math.BigInteger;

/**
 * AddressNonceService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午2:00
 */
public interface AddressNonceService extends IBaseService<AddressNonce, String> {

    /***
     * 根据地址获取转账唯一编号
     * @param address
     * @return
     */
    BigInteger getNonce(String address);

}
