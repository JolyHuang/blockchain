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
     * 生成助记词存放文件路径
     */
    private String filePath;

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44ChangeRsp{");
        sb.append("filePath='").append(filePath).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
