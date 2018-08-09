package com.sharingif.blockchain.transaction.service;

/**
 * AddressNoticeSignatureService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午6:43
 */
public interface AddressNoticeSignatureService {

    /**
     * 签名
     * @param signData
     * @return
     */
    String sign(byte[] signData);

}
