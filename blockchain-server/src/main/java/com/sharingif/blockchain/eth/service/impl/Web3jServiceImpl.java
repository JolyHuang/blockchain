package com.sharingif.blockchain.eth.service.impl;

import com.sharingif.blockchain.eth.service.Web3jService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * Web3j服务
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/6/6 下午5:54
 */
@Service
public class Web3jServiceImpl implements Web3jService, InitializingBean {

    private String ethWeb3jAddress;
    private Web3j currentAvailableWeb3j;

    @Value("${eth.web3j.address}")
    public void setEthWeb3jAddress(String ethWeb3jAddress) {
        this.ethWeb3jAddress = ethWeb3jAddress;
    }

    @Override
    public Web3j getCurrentAvailableWeb3j() {
        return currentAvailableWeb3j;
    }

    @Override
    public void afterPropertiesSet() {
        currentAvailableWeb3j = Web3j.build(new HttpService(ethWeb3jAddress));
    }
}
