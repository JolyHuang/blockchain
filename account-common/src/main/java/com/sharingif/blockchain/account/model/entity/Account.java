package com.sharingif.blockchain.account.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;


public class Account implements java.io.Serializable , IObjectDateOperationHistory {

	/**
	 * 账户状态(NORMAL:ZCSD:转出锁定)
	 */
	public static final String STATUS_TRANSFOR_OUT_LOCK = "ZCSD";
	/**
	 * 账户状态(NORMAL:正常)
	 */
	public static final String STATUS_NORMAL = "NORMAL";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * 地址			db_column: ADDRESS 
     */	
	@Length(max=100)
	private java.lang.String address;
    /**
     * 币种			db_column: COIN_TYPE 
     */	
	@Length(max=20)
	private java.lang.String coinType;
    /**
     * 入账总额			db_column: TOTAL_IN 
     */	
	private BigInteger totalIn;
    /**
     * 出账总额			db_column: TOTAL_OUT 
     */	
	private BigInteger totalOut;
    /**
     * 余额			db_column: BALANCE 
     */	
	@NotNull 
	private BigInteger balance;
    /**
     * 账户状态(CZSD:充值锁定、ZZSD:转出锁定、NORMAL:正常)			db_column: STATUS
     */	
	@Length(max=10)
	private java.lang.String status;
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
	public void setCoinType(java.lang.String coinType) {
		this.coinType = coinType;
	}
	public java.lang.String getCoinType() {
		return this.coinType;
	}
	public void setTotalIn(BigInteger totalIn) {
		this.totalIn = totalIn;
	}
	public BigInteger getTotalIn() {
		return this.totalIn;
	}
	public void setTotalOut(BigInteger totalOut) {
		this.totalOut = totalOut;
	}
	public BigInteger getTotalOut() {
		return this.totalOut;
	}
	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}
	public BigInteger getBalance() {
		return this.balance;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getStatus() {
		return this.status;
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
		return new StringBuilder("Account [")
			.append("Id=").append(getId()).append(", ")
					.append("Address=").append(getAddress()).append(", ")
					.append("CoinType=").append(getCoinType()).append(", ")
					.append("TotalIn=").append(getTotalIn()).append(", ")
					.append("TotalOut=").append(getTotalOut()).append(", ")
					.append("Balance=").append(getBalance()).append(", ")
					.append("Status=").append(getStatus()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

