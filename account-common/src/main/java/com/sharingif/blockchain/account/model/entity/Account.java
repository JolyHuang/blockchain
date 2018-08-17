package com.sharingif.blockchain.account.model.entity;


import javax.validation.constraints.*;

import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class Account implements java.io.Serializable, IObjectDateOperationHistory {

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
	@NotBlank @Length(max=100)
	private java.lang.String address;
    /**
     * 币种			db_column: COIN_TYPE 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String coinType;
    /**
     * 入账总额			db_column: TOTAL_IN 
     */	
	@NotNull 
	private BigInteger totalIn;
    /**
     * 出账总额			db_column: TOTAL_OUT 
     */	
	@NotNull 
	private BigInteger totalOut;
    /**
     * 余额			db_column: BALANCE 
     */	
	@NotNull 
	private BigInteger balance;
    /**
     * 冻结金额			db_column: FROZEN_AMOUNT 
     */	
	@NotNull 
	private BigInteger frozenAmount;
    /**
     * 账户状态(CZSD:充值锁定、ZZSD:转出锁定、NORMAL:正常)			db_column: STATUS 
     */	
	@NotBlank @Length(max=10)
	private java.lang.String status;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private Date modifyTime;
	//columns END

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public BigInteger getTotalIn() {
		return totalIn;
	}

	public void setTotalIn(BigInteger totalIn) {
		this.totalIn = totalIn;
	}

	public BigInteger getTotalOut() {
		return totalOut;
	}

	public void setTotalOut(BigInteger totalOut) {
		this.totalOut = totalOut;
	}

	public BigInteger getBalance() {
		return balance;
	}

	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}

	public BigInteger getFrozenAmount() {
		return frozenAmount;
	}

	public void setFrozenAmount(BigInteger frozenAmount) {
		this.frozenAmount = frozenAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Account{");
		sb.append("id='").append(id).append('\'');
		sb.append(", address='").append(address).append('\'');
		sb.append(", coinType='").append(coinType).append('\'');
		sb.append(", totalIn=").append(totalIn);
		sb.append(", totalOut=").append(totalOut);
		sb.append(", balance=").append(balance);
		sb.append(", frozenAmount=").append(frozenAmount);
		sb.append(", status='").append(status).append('\'');
		sb.append(", createTime=").append(createTime);
		sb.append(", modifyTime=").append(modifyTime);
		sb.append('}');
		return sb.toString();
	}
}

