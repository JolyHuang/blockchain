package com.sharingif.blockchain.account.api.account.entity;

/**
 * 取现响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/9/26 下午12:17
 */
public class WithdrawalApplyRsp {

    /**
     * 取现唯一编号
     */
    private String withdrawalId;

    /**
     * 取现返回订单编号
     */
    private String withdrawalOrderId;

    public String getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(String withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public String getWithdrawalOrderId() {
        return withdrawalOrderId;
    }

    public void setWithdrawalOrderId(String withdrawalOrderId) {
        this.withdrawalOrderId = withdrawalOrderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WithdrawalApplyRsp{");
        sb.append("withdrawalId='").append(withdrawalId).append('\'');
        sb.append(", withdrawalOrderId='").append(withdrawalOrderId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
