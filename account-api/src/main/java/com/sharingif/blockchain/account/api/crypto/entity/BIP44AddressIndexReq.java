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
     * change ExtendedKey Id
     */
    private String changeExtendedKeyId;
    /**
     * 币种
     */
    private int coinType;
    /**
     * 通知地址
     */
    private String noticeAddress;

    public String getChangeExtendedKeyId() {
        return changeExtendedKeyId;
    }

    public void setChangeExtendedKeyId(String changeExtendedKeyId) {
        this.changeExtendedKeyId = changeExtendedKeyId;
    }

    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    public String getNoticeAddress() {
        return noticeAddress;
    }

    public void setNoticeAddress(String noticeAddress) {
        this.noticeAddress = noticeAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44AddressIndexReq{");
        sb.append("changeExtendedKeyId='").append(changeExtendedKeyId).append('\'');
        sb.append(", coinType=").append(coinType);
        sb.append(", noticeAddress='").append(noticeAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
