package com.sharingif.blockchain.crypto.api.key.entity;

/**
 * 生成change ExtendedKey响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 上午11:13
 */
public class BIP44ChangeRsp {

    /**
     * id
     */
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44ChangeRsp{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
