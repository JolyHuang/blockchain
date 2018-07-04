package com.sharingif.blockchain.crypto.api.key;

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
    private String coinType;
    /**
     * 账号
     */
    private String account;
    /**
     * 零钱
     */
    private String change;
    /**
     * 助记词密码
     */
    private String mnemonicPassword;
    /**
     * 助记词文件名称
     */
    private String mnemonicFileName;
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

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getMnemonicPassword() {
        return mnemonicPassword;
    }

    public void setMnemonicPassword(String mnemonicPassword) {
        this.mnemonicPassword = mnemonicPassword;
    }

    public String getMnemonicFileName() {
        return mnemonicFileName;
    }

    public void setMnemonicFileName(String mnemonicFileName) {
        this.mnemonicFileName = mnemonicFileName;
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
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", change='").append(change).append('\'');
        sb.append(", mnemonicPassword='").append(mnemonicPassword).append('\'');
        sb.append(", mnemonicFileName='").append(mnemonicFileName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
