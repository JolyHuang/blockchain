package com.sharingif.blockchain.api.crypto.entity;

import java.util.Locale;

/**
 * 生成助记词请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/2 下午4:35
 */
public class MnemonicGenerateReq {

    /**
     * 12个助记词
     */
    public static final int MNEMONIC_LENGTH_12 = 12;
    /**
     * 24个助记词
     */
    public static final int MNEMONIC_LENGTH_24 = 24;

    /**
     * 语言
     */
    private Locale locale;
    /**
     * 助记词长度
     */
    private int length;
    /**
     * 密码
     */
    private String password;

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MnemonicGenerateReq{");
        sb.append("locale=").append(locale);
        sb.append(", length=").append(length);
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
