package com.sharingif.blockchain.crypto.api.key;

/**
 * 生成密钥请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:41
 */
public class BIP44GenerateReq {

    public static final String COIN_TYPE_ETH = "60";

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
     * 上级密码
     */
    private String parentPassword;
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

    public String getParentPassword() {
        return parentPassword;
    }

    public void setParentPassword(String parentPassword) {
        this.parentPassword = parentPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
