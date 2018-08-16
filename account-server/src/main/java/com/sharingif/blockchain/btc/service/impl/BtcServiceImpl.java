package com.sharingif.blockchain.btc.service.impl;

import com.sharingif.blockchain.app.constants.Constants;
import com.sharingif.blockchain.btc.service.BtcService;
import com.sharingif.blockchain.common.constants.CoinType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * BtcServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/15 下午5:38
 */
@Service
public class BtcServiceImpl implements BtcService {

    private String btcNetType;

    @Value("${btc.net.type}")
    public void setBtcNetType(String btcNetType) {
        this.btcNetType = btcNetType;
    }

    @Override
    public int getBipCoinType() {
        if(Constants.BTC_NET_TYPE_TEST.equals(btcNetType)) {
            return CoinType.BIP_BTC_TEST.getBipCoinType();
        }

        return CoinType.BTC.getBipCoinType();
    }

}
