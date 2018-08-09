package com.sharingif.blockchain.crypto.key.service.impl;

import com.sharingif.blockchain.crypto.app.constants.CoinType;
import com.sharingif.blockchain.crypto.key.service.BtcService;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.springframework.stereotype.Service;

/**
 * BtcServiceImpl
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/8 下午5:11
 */
@Service
public class BtcServiceImpl implements BtcService {

    @Override
    public NetworkParameters getNetworkParameters(int coinType) {
        NetworkParameters networkParameters = MainNetParams.get();
        if(coinType == CoinType.BIP_BTC_TEST.getBipCoinType()) {
            networkParameters = TestNet3Params.get();
        }

        return networkParameters;
    }

}
