package com.sharingif.blockchain.crypto.api.eth.entity;

/**
 * 生成transfer交易响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/26 下午12:55
 */
public class Erc20TransferRsp {

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
        final StringBuilder sb = new StringBuilder("Erc20TransferRsp{");
        sb.append("hexValue='").append(hexValue).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
