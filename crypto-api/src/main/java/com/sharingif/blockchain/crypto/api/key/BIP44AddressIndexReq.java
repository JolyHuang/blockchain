package com.sharingif.blockchain.crypto.api.key;

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
     * 地址索引
     */
    private String addressIndex;
    /**
     * change扩展密钥密码
     */
    private String changeExtendedKeyPassword;
    /**
     * change扩展密钥文件名
     */
    private String changeExtendedKeyFileName;
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

    public String getAddressIndex() {
        return addressIndex;
    }

    public void setAddressIndex(String addressIndex) {
        this.addressIndex = addressIndex;
    }

    public String getChangeExtendedKeyPassword() {
        return changeExtendedKeyPassword;
    }

    public void setChangeExtendedKeyPassword(String changeExtendedKeyPassword) {
        this.changeExtendedKeyPassword = changeExtendedKeyPassword;
    }

    public String getChangeExtendedKeyFileName() {
        return changeExtendedKeyFileName;
    }

    public void setChangeExtendedKeyFileName(String changeExtendedKeyFileName) {
        this.changeExtendedKeyFileName = changeExtendedKeyFileName;
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
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", change='").append(change).append('\'');
        sb.append(", addressIndex='").append(addressIndex).append('\'');
        sb.append(", changeExtendedKeyPassword='").append(changeExtendedKeyPassword).append('\'');
        sb.append(", changeExtendedKeyFileName='").append(changeExtendedKeyFileName).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
