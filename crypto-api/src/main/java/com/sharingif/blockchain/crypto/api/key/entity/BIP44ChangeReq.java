package com.sharingif.blockchain.crypto.api.key.entity;

/**
 * 生成change ExtendedKey请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 上午11:13
 */
public class BIP44ChangeReq {

    /**
     * 助记词唯一编号
     */
    private String mnemonicId;
    /**
     * 币种
     */
    private Integer coinType;
    /**
     * 账号
     */
    private Integer account;
    /**
     * 零钱
     */
    private Integer change;
    /**
     * 助记词密码
     */
    private String mnemonicPassword;
    /**
     * 密码
     */
    private String password;

    public String getMnemonicId() {
        return mnemonicId;
    }

    public void setMnemonicId(String mnemonicId) {
        this.mnemonicId = mnemonicId;
    }

    public Integer getCoinType() {
        return coinType;
    }

    public void setCoinType(Integer coinType) {
        this.coinType = coinType;
    }

    public Integer getAccount() {
        return account;
    }

    public void setAccount(Integer account) {
        this.account = account;
    }

    public Integer getChange() {
        return change;
    }

    public void setChange(Integer change) {
        this.change = change;
    }

    public String getMnemonicPassword() {
        return mnemonicPassword;
    }

    public void setMnemonicPassword(String mnemonicPassword) {
        this.mnemonicPassword = mnemonicPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44ChangeReq{");
        sb.append("mnemonicId='").append(mnemonicId).append('\'');
        sb.append(", coinType=").append(coinType);
        sb.append(", account=").append(account);
        sb.append(", change=").append(change);
        sb.append(", mnemonicPassword='").append(mnemonicPassword).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
