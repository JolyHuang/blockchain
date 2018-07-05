package com.sharingif.blockchain.api.crypto;

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
     * 助记词编号
     */
    private String mnemonicId;
    /**
     * 助记词
     */
    private String mnemonic;

    public String getMnemonicId() {
        return mnemonicId;
    }

    public void setMnemonicId(String mnemonicId) {
        this.mnemonicId = mnemonicId;
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
        sb.append("mnemonicId='").append(mnemonicId).append('\'');
        sb.append(", mnemonic='").append(mnemonic).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
