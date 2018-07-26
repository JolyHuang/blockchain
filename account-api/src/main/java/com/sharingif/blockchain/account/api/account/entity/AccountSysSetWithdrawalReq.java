package com.sharingif.blockchain.account.api.account.entity;

/**
 * 设置取现地址请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/25 下午6:42
 */
public class AccountSysSetWithdrawalReq {

    /**
     * 密钥id
     */
    private String secretKeyId;

    public String getSecretKeyId() {
        return secretKeyId;
    }

    public void setSecretKeyId(String secretKeyId) {
        this.secretKeyId = secretKeyId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountSysSetWithdrawalReq{");
        sb.append("secretKeyId='").append(secretKeyId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
