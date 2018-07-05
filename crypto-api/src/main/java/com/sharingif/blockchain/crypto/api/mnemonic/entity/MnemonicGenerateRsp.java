package com.sharingif.blockchain.crypto.api.mnemonic.entity;

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
     * 助记词
     */
    private String mnemonic;
    /**
     * 生成助记词存放文件路径
     */
    private String filePath;

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MnemonicGenerateRsp{");
        sb.append("mnemonic='").append(mnemonic).append('\'');
        sb.append(", filePath='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
