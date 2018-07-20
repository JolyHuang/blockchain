package com.sharingif.blockchain.crypto.api.key.entity;

/**
 * 生成addressIndex响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 下午2:29
 */
public class BIP44AddressIndexRsp {

    /**
     * id
     */
    private String id;
    /**
     * 地址
     */
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44AddressIndexRsp{");
        sb.append("id='").append(id).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
