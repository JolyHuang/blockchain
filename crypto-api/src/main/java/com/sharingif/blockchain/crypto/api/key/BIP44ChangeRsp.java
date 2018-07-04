package com.sharingif.blockchain.crypto.api.key;

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
     * 扩展key唯一编号
     */
    private String extendedKeyId;
    /**
     * 生成助记词存放文件名
     */
    private String fileName;

    public String getExtendedKeyId() {
        return extendedKeyId;
    }

    public void setExtendedKeyId(String extendedKeyId) {
        this.extendedKeyId = extendedKeyId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
