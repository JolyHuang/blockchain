package com.sharingif.blockchain.account.api.account.entity;

import java.math.BigInteger;

/**
 * 取现请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午2:23
 */
public class WithdrawalApplyReq {

    /**
     * 取现唯一编号
     */
    private String withdrawalId;
    /**
     * 币种
     */
    private String coinType;
    /**
     * 取现地址
     */
    private String address;
    /**
     * 金额
     */
    private BigInteger amount;
    /**
     * 通知地址
     */
    private String noticeAddress;

    public String getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(String withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public String getNoticeAddress() {
        return noticeAddress;
    }

    public void setNoticeAddress(String noticeAddress) {
        this.noticeAddress = noticeAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WithdrawalReq{");
        sb.append("withdrawalId='").append(withdrawalId).append('\'');
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", noticeAddress='").append(noticeAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
