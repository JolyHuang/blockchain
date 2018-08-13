package com.sharingif.blockchain.crypto.app.constants;

/**
 * 币种
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/19 上午11:53
 */
public enum CoinType {

    BTC(0,"BTC"),BIP_BTC_TEST(1, "BTC"),ETH(60, "ETH"),OLE(60, "OLE");

    private int bipCoinType;
    private String coinType;

    CoinType(int bipCoinType, String coinType) {
        this.bipCoinType = bipCoinType;
        this.coinType = coinType;
    }

    public int getBipCoinType() {
        return bipCoinType;
    }

    public String getCoinType() {
        return coinType;
    }

}
