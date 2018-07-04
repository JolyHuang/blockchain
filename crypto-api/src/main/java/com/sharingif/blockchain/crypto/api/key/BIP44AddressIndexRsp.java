package com.sharingif.blockchain.crypto.api.key;

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
     * key id
     */
    private String keyId;
    /**
     * 地址
     */
    private String address;
    /**
     * 生成密钥存放文件名
     */
    private String fileName;

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44AddressIndexRsp{");
        sb.append("keyId='").append(keyId).append('\'');
        sb.append(", address='").append(address).append('\'');
        sb.append(", fileName='").append(fileName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
