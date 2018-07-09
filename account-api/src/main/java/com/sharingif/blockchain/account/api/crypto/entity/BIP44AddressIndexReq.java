package com.sharingif.blockchain.account.api.crypto.entity;

import java.util.List;

/**
 * 生成addressIndex请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 下午2:28
 */
public class BIP44AddressIndexReq {

    /**
     * BTC
     */
    public static final String COIN_TYPE_BTC = "BTC";
    /**
     * ETH
     */
    public static final String COIN_TYPE_ETH = "ETH";

    /**
     * 币种
     */
    private String coinType;
    /**
     * 通知地址列表
     */
    private List<String> noticeList;

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public List<String> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<String> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44AddressIndexReq{");
        sb.append("coinType='").append(coinType).append('\'');
        sb.append(", noticeList=").append(noticeList);
        sb.append('}');
        return sb.toString();
    }
}
