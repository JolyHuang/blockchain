package com.sharingif.blockchain.common.constants;

/**
 * 币种
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/19 上午11:53
 */
public enum  CoinType {

    BIP_BTC(0),BIP_ETH(60),BTC("BTC"),ETH("ETH"),OLE("OLE");


    private String coinType;
    private int bipCoinType;

    CoinType(String coinType) {
        this.coinType = coinType;
    }

    CoinType(int bipCoinType) {
        this.bipCoinType = bipCoinType;
    }

    public String getCoinType() {
        return coinType;
    }

    public int getBipCoinType() {
        return bipCoinType;
    }

}
