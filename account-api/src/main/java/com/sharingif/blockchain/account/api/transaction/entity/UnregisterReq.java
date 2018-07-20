package com.sharingif.blockchain.account.api.transaction.entity;

/**
 * 解除注册请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/8 下午5:56
 */
public class UnregisterReq {

    /**
     * 地址
     */
    private String address;
    /**
     * 币种
     */
    private String coinType;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UnregisterReq{");
        sb.append("address='").append(address).append('\'');
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
