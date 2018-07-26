package com.sharingif.blockchain.crypto.api.eth.entity;

/**
 * eth转账响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午1:01
 */
public class EthTransferRsp {

    /**
     * 签名数据
     */
    private String hexValue;

    public String getHexValue() {
        return hexValue;
    }

    public void setHexValue(String hexValue) {
        this.hexValue = hexValue;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("EthTransferRsp{");
        sb.append("hexValue='").append(hexValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
