package com.sharingif.blockchain.crypto.api.key;

/**
 * 生成密钥请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/3 下午12:41
 */
public class KeyGenerateReq {

    /**
     * 唯一编号
     */
    private String id;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KeyGenerateReq{");
        sb.append("id='").append(id).append('\'');
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", account='").append(account).append('\'');
        sb.append(", change='").append(change).append('\'');
        sb.append(", addressIndex='").append(addressIndex).append('\'');
        sb.append(", parentPassword='").append(parentPassword).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
