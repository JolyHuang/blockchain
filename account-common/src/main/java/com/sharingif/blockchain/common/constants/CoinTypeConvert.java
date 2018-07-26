package com.sharingif.blockchain.common.constants;


import java.util.HashMap;
import java.util.Map;

/**
 * CoinTypeConvert
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/19 下午5:15
 */
public class CoinTypeConvert {

    private static final  Map<Object, Object> COIN_TYPE_MAP;

    static {
        COIN_TYPE_MAP = new HashMap<Object, Object>();
        COIN_TYPE_MAP.put(CoinType.BIP_BTC.getBipCoinType(), CoinType.BTC.name());
        COIN_TYPE_MAP.put(CoinType.BIP_ETH.getBipCoinType(), CoinType.ETH.name());

        COIN_TYPE_MAP.put(CoinType.BTC.name(), CoinType.BIP_BTC.getBipCoinType());
        COIN_TYPE_MAP.put(CoinType.ETH.name(), CoinType.BIP_ETH.getBipCoinType());
        COIN_TYPE_MAP.put(CoinType.OLE.name(), CoinType.BIP_ETH.getBipCoinType());
    }

    public static String convertToCoinType(int bipCoinType) {

        return (String)COIN_TYPE_MAP.get(bipCoinType);
    }

    public static int convertToBipCoinType(String coinType) {

        return (int)COIN_TYPE_MAP.get(coinType);
    }

}
