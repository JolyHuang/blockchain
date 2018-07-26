package com.sharingif.blockchain.crypto.api.eth.entity;

import java.math.BigInteger;

/**
 * eth转账请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午1:00
 */
public class EthTransferReq {

    /**
     * 交易唯一编号
     */
    private BigInteger nonce;
    /**
     * 收款人地址
     */
    private String toAddress;
    /**
     * 金额
     */
    private BigInteger amount;
    /**
     * gas价格
     */
    private BigInteger gasPrice;
    /**
     * secret key id
     */
    private String secretKeyId;
    /**
     * 密码
     */
    private String password;

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
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

    public BigInteger getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(BigInteger gasPrice) {
        this.gasPrice = gasPrice;
    }

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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EthTransferReq{");
        sb.append("nonce=").append(nonce);
        sb.append(", toAddress='").append(toAddress).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", gasPrice=").append(gasPrice);
        sb.append(", secretKeyId='").append(secretKeyId).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
