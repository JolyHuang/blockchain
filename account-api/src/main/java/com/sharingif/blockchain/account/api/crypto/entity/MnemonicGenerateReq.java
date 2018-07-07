package com.sharingif.blockchain.account.api.crypto.entity;

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
     * 别名
     */
    private java.lang.String alias;
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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

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
        sb.append("alias='").append(alias).append('\'');
        sb.append(", locale=").append(locale);
        sb.append(", length=").append(length);
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
