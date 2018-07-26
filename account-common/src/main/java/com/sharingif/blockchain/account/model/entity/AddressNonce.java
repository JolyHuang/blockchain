package com.sharingif.blockchain.account.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import org.hibernate.validator.constraints.Length;

import java.math.BigInteger;


public class AddressNonce implements java.io.Serializable, IObjectDateOperationHistory {
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * 地址			db_column: ADDRESS 
     */	
	@Length(max=100)
	private java.lang.String address;
    /**
     * nonce			db_column: NONCE 
     */	
	
	private BigInteger nonce;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private java.util.Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private java.util.Date modifyTime;
	//columns END

	public void setAddress(java.lang.String address) {
		this.address = address;
	}
	public java.lang.String getAddress() {
		return this.address;
	}
	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}
	public BigInteger getNonce() {
		return this.nonce;
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
		return new StringBuilder("AddressNonce [")
			.append("Address=").append(getAddress()).append(", ")
					.append("Nonce=").append(getNonce()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

