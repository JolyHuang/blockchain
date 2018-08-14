package com.sharingif.blockchain.crypto.api.btc.entity;

import java.math.BigInteger;
import java.util.List;

/**
 * 转账请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/8/14 下午2:19
 */
public class BtcTransferReq {
    /**
     * secret key id
     */
    private String secretKeyId;
    /**
     * 密码
     */
    private String password;
    /**
     * 收款人地址
     */
    private String toAddress;
    /**
     * 金额
     */
    private BigInteger amount;
    /**
     * 手续费
     */
    private BigInteger fee;
    /**
     * output list
     */
    private List<BtcTransferListReq> outputList;

    public String getSecretKeyId() {
        return secretKeyId;
    }

    public void setSecretKeyId(String secretKeyId) {
        this.secretKeyId = secretKeyId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public BigInteger getFee() {
        return fee;
    }

    public void setFee(BigInteger fee) {
        this.fee = fee;
    }

    public List<BtcTransferListReq> getOutputList() {
        return outputList;
    }

    public void setOutputList(List<BtcTransferListReq> outputList) {
        this.outputList = outputList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BtcTransferReq{");
        sb.append("secretKeyId='").append(secretKeyId).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", toAddress='").append(toAddress).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", fee=").append(fee);
        sb.append(", outputList=").append(outputList);
        sb.append('}');
        return sb.toString();
    }
}
