package com.sharingif.blockchain.api.transaction.entity;

import java.util.List;

/**
 * 注册请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/8 下午5:57
 */
public class RegisterReq {

    /**
     * 地址
     */
    private String address;
    /**
     * 币种
     */
    private String coinType;
    /**
     * 通知地址列表
     */
    private List<String> noticeList;

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

    public List<String> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<String> noticeList) {
        this.noticeList = noticeList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RegisterReq{");
        sb.append("address='").append(address).append('\'');
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", noticeList=").append(noticeList);
        sb.append('}');
        return sb.toString();
    }
}
