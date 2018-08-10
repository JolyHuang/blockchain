package com.sharingif.blockchain.account.batch.api.transaction.entity;

/**
 * 注册请求
 *
 * @author Joly
 * @version v1.0
 * @since v1.0
 * 2018/7/8 下午5:57
 */
public class RegisterReq {

    /**
     * 地址
     */
    private String address;
    /**
     * 币种
     */
    private String coinType;
    /**
     * 子币种
     */
    private String subCoinType;
    /**
     * 合约地址
     */
    private String contractAddress;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public String getSubCoinType() {
        return subCoinType;
    }

    public void setSubCoinType(String subCoinType) {
        this.subCoinType = subCoinType;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RegisterReq{");
        sb.append("address='").append(address).append('\'');
        sb.append(", coinType='").append(coinType).append('\'');
        sb.append(", subCoinType='").append(subCoinType).append('\'');
        sb.append(", contractAddress='").append(contractAddress).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
