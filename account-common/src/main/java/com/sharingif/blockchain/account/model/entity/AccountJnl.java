package com.sharingif.blockchain.account.model.entity;


import javax.validation.constraints.*;

import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.*;

import java.math.BigInteger;


public class AccountJnl implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 类型(00:转入)
	 */
	public static final String TYPE_IN = "00";
	/**
	 * 类型(01:转出)
	 */
	public static final String TYPE_OUT = "01";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * from地址			db_column: ACCOUNT_FROM 
     */	
	@Length(max=100)
	private java.lang.String accountFrom;
    /**
     * to地址			db_column: ACCOUNT_TO 
     */	
	@Length(max=100)
	private java.lang.String accountTo;
    /**
     * 币种			db_column: COIN_TYPE 
     */	
	@Length(max=20)
	private java.lang.String coinType;
    /**
     * 金额			db_column: BALANCE 
     */	
	@NotNull 
	private BigInteger balance;
    /**
     * 类型(00:转入、01:转出)			db_column: TYPE 
     */	
	@Length(max=2)
	private java.lang.String type;
    /**
     * 交易hash			db_column: TX_HASH 
     */	
	@Length(max=200)
	private java.lang.String txHash;
    /**
     * 交易时间			db_column: TRANS_TIME 
     */	
	
	private java.util.Date transTime;
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
	public void setAccountFrom(java.lang.String accountFrom) {
		this.accountFrom = accountFrom;
	}
	public java.lang.String getAccountFrom() {
		return this.accountFrom;
	}
	public void setAccountTo(java.lang.String accountTo) {
		this.accountTo = accountTo;
	}
	public java.lang.String getAccountTo() {
		return this.accountTo;
	}
	public void setCoinType(java.lang.String coinType) {
		this.coinType = coinType;
	}
	public java.lang.String getCoinType() {
		return this.coinType;
	}
	public void setBalance(BigInteger balance) {
		this.balance = balance;
	}
	public BigInteger getBalance() {
		return this.balance;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.lang.String getType() {
		return this.type;
	}
	public void setTxHash(java.lang.String txHash) {
		this.txHash = txHash;
	}
	public java.lang.String getTxHash() {
		return this.txHash;
	}
	public void setTransTime(java.util.Date transTime) {
		this.transTime = transTime;
	}
	public java.util.Date getTransTime() {
		return this.transTime;
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
		return new StringBuilder("AccountJnl [")
			.append("Id=").append(getId()).append(", ")
					.append("AccountFrom=").append(getAccountFrom()).append(", ")
					.append("AccountTo=").append(getAccountTo()).append(", ")
					.append("CoinType=").append(getCoinType()).append(", ")
					.append("Balance=").append(getBalance()).append(", ")
					.append("Type=").append(getType()).append(", ")
					.append("TxHash=").append(getTxHash()).append(", ")
					.append("TransTime=").append(getTransTime()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

