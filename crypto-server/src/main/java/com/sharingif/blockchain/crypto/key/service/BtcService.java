package com.sharingif.blockchain.crypto.key.service;

import org.bitcoinj.core.NetworkParameters;

/**
 * BtcService
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午5:09
 */
public interface BtcService {

    /**
     * 根据币种获取btc网络类型
     * @param coinType
     * @return
     */
    NetworkParameters getNetworkParameters(int coinType);

}
