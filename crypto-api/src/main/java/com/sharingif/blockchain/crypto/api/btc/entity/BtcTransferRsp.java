package com.sharingif.blockchain.crypto.api.btc.entity;

/**
 * 转账响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午2:28
 */
public class BtcTransferRsp {

    /**
     * raw Transaction
     */
    private String rawTransaction;

    public String getRawTransaction() {
        return rawTransaction;
    }

    public void setRawTransaction(String rawTransaction) {
        this.rawTransaction = rawTransaction;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BtcTransferRsp{");
        sb.append("rawTransaction='").append(rawTransaction).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
