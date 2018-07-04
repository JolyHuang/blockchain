package com.sharingif.blockchain.crypto.api.mnemonic;

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
     * 助记词唯一编号
     */
    private String mnemonicId;
    /**
     * 助记词
     */
    private String mnemonic;
    /**
     * 生成助记词存放文件名
     */
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
