package com.sharingif.blockchain.eth.service;

import org.web3j.protocol.Web3j;

/**
 * Web3j服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/6 下午5:49
 */
public interface Web3jService {

    /**
     * 获取当前有效的Web3j 对象
     * @return
     */
    Web3j getCurrentAvailableWeb3j();

}
