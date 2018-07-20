package com.sharingif.blockchain.account.api.account.entity;

/**
 * 设置当前change ExtendedKey请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/19 下午3:53
 */
public class AccountSysSetChangeExtendedKeyReq {

    /**
     * change ExtendedKey id
     */
    private String changeExtendedKeyId;

    public String getChangeExtendedKeyId() {
        return changeExtendedKeyId;
    }

    public void setChangeExtendedKeyId(String changeExtendedKeyId) {
        this.changeExtendedKeyId = changeExtendedKeyId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AccountSysSetChangeExtendedKeyReq{");
        sb.append("changeExtendedKeyId='").append(changeExtendedKeyId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
