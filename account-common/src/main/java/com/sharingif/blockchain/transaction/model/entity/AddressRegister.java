package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import org.hibernate.validator.constraints.Length;


public class AddressRegister implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 币种(BTC:比特币)
	 */
	public static final String COIN_TYPE_BTC = "BTC";
	/**
	 * 币种(ETH:以太坊)
	 */
	public static final String COIN_TYPE_ETH = "ETH";

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	private java.lang.String id;
    /**
     * 地址			db_column: ADDRESS 
     */	
	@Length(max=100)
	private java.lang.String address;
    /**
     * 合约地址			db_column: CONTRACT_ADDRESS 
     */	
	@Length(max=100)
	private java.lang.String contractAddress;
    /**
     * 币种			db_column: COIN_TYPE 
     */	
	@Length(max=20)
	private java.lang.String coinType;
    /**
     * 子币种			db_column: SUB_COIN_TYPE 
     */	
	@Length(max=20)
	private java.lang.String subCoinType;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private java.util.Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private java.util.Date modifyTime;
	//columns END

	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	public java.lang.String getAddress() {
		return this.address;
	}
	public void setContractAddress(java.lang.String contractAddress) {
		this.contractAddress = contractAddress;
	}
	public java.lang.String getContractAddress() {
		return this.contractAddress;
	}
	public void setCoinType(java.lang.String coinType) {
		this.coinType = coinType;
	}
	public java.lang.String getCoinType() {
		return this.coinType;
	}
	public void setSubCoinType(java.lang.String subCoinType) {
		this.subCoinType = subCoinType;
	}
	public java.lang.String getSubCoinType() {
		return this.subCoinType;
	}
	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}
	public java.util.Date getCreateTime() {
		return this.createTime;
	}
	public void setModifyTime(java.util.Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public java.util.Date getModifyTime() {
		return this.modifyTime;
	}

	public String toString() {
		return new StringBuilder("AddressRegister [")
			.append("Id=").append(getId()).append(", ")
					.append("Address=").append(getAddress()).append(", ")
					.append("ContractAddress=").append(getContractAddress()).append(", ")
					.append("CoinType=").append(getCoinType()).append(", ")
					.append("SubCoinType=").append(getSubCoinType()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

