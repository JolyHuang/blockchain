package com.sharingif.blockchain.api.crypto.entity;

/**
 * 生成助记词响应
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/2 下午4:35
 */
public class MnemonicGenerateRsp {

    /**
     * id
     */
    private String id;
    /**
     * 助记词
     */
    private String mnemonic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MnemonicGenerateRsp{");
        sb.append("id='").append(id).append('\'');
        sb.append(", mnemonic='").append(mnemonic).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
