package com.sharingif.blockchain.crypto.api.key.entity;

/**
 * 生成addressIndex请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/4 下午2:28
 */
public class BIP44AddressIndexReq {

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
     * change扩展密钥文件名
     */
    private String changeExtendedKeyId;
    /**
     * change扩展密钥密码
     */
    private String changeExtendedKeyPassword;
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

    public String getChangeExtendedKeyId() {
        return changeExtendedKeyId;
    }

    public void setChangeExtendedKeyId(String changeExtendedKeyId) {
        this.changeExtendedKeyId = changeExtendedKeyId;
    }

    public String getChangeExtendedKeyPassword() {
        return changeExtendedKeyPassword;
    }

    public void setChangeExtendedKeyPassword(String changeExtendedKeyPassword) {
        this.changeExtendedKeyPassword = changeExtendedKeyPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("BIP44AddressIndexReq{");
        sb.append("mnemonicId='").append(mnemonicId).append('\'');
        sb.append(", coinType=").append(coinType);
        sb.append(", account=").append(account);
        sb.append(", change=").append(change);
        sb.append(", changeExtendedKeyId='").append(changeExtendedKeyId).append('\'');
        sb.append(", changeExtendedKeyPassword='").append(changeExtendedKeyPassword).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
