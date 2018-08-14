package com.sharingif.blockchain.crypto.api.btc.entity;

import java.math.BigInteger;

/**
 * 转账请求list
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午3:20
 */
public class BtcTransferListReq {

    /**
     * txId
     */
    private String txId;
    /**
     * vout
     */
    private Integer vout;
    /**
     * scriptPubKey
     */
    private String scriptPubKey;
    /**
     * amount
     */
    private BigInteger amount;

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Integer getVout() {
        return vout;
    }

    public void setVout(Integer vout) {
        this.vout = vout;
    }

    public String getScriptPubKey() {
        return scriptPubKey;
    }

    public void setScriptPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BtcTransferListReq{");
        sb.append("txId='").append(txId).append('\'');
        sb.append(", vout=").append(vout);
        sb.append(", scriptPubKey='").append(scriptPubKey).append('\'');
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
