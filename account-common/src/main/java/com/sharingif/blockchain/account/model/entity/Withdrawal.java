package com.sharingif.blockchain.account.model.entity;


import javax.validation.constraints.*;

import com.sharingif.blockchain.account.api.account.entity.WithdrawalApplyReq;
import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;


public class Withdrawal implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 处理状态(QXWCL:取现未处理)
	 */
	public static final String STATUS_WITHDRAWAL_UNTREATED = "QXWCL";
	/**
	 * 处理状态(QXCLZ:取现处理中)
	 */
	public static final String STATUS_WITHDRAWAL_PROCESSING = "QXCLZ";
	/**
	 * 处理状态(SUCCESS:取现成功)
	 */
	public static final String STATUS_WITHDRAWAL_SUCCESS = "SUCCESS";
	/**
	 * 处理状态(SUCCESS:取现成功已通知)
	 */
	public static final String STATUS_WITHDRAWAL_SUCCESS_NOTIFIED = "SUCCESS_NOTIFIED";
	/**
	 * 处理状态(FAIL:取现失败)
	 */
	public static final String STATUS_WITHDRAWAL_FAIL = "FAIL";
	/**
	 * 处理状态(FAIL:取现失败已通知)
	 */
	public static final String STATUS_WITHDRAWAL_FAIL_NOTIFIED = "FAIL_NOTIFIED";

	/**
	 * 处理状态(WCL:未处理)
	 */
	public static final String TASK_STATUS_UNTREATED = "WCL";
	/**
	 * 处理状态(CLZ:处理中)
	 */
	public static final String TASK_STATUS_PROCESSING = "CLZ";
	/**
	 * 处理状态(SUCCESS:处理成功)
	 */
	public static final String TASK_STATUS_SUCCESS = "SUCCESS";
	/**
	 * 处理状态(FAIL:处理失败)
	 */
	public static final String TASK_STATUS_FAIL = "FAIL";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * 取现唯一编号			db_column: WITHDRAWAL_ID 
     */	
	@NotBlank @Length(max=32)
	private java.lang.String withdrawalId;
    /**
     * 币种			db_column: COIN_TYPE 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String coinType;
    /**
     * 子币种			db_column: SUB_COIN_TYPE 
     */	
	@Length(max=20)
	private java.lang.String subCoinType;
    /**
     * FORM地址			db_column: TX_FROM 
     */	
	@Length(max=100)
	private java.lang.String txFrom;
    /**
     * TO地址			db_column: TX_TO 
     */	
	@Length(max=100)
	private java.lang.String txTo;
    /**
     * 金额			db_column: AMOUNT 
     */	
	
	private BigInteger amount;
    /**
     * 冻结金额			db_column: FROZEN_AMOUNT 
     */	
	@NotNull 
	private BigInteger frozenAmount;
    /**
     * 手续费			db_column: FEE 
     */	
	
	private BigInteger fee;
    /**
     * 交易hash			db_column: TX_HASH 
     */	
	@Length(max=100)
	private java.lang.String txHash;
    /**
     * 处理状态(CZWCL:充值未处理、CZCLZ:充值处理中、SUCCESS:充值成功、FAIL:充值失败)			db_column: STATUS 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String status;
    /**
     * 处理状态(WCL:未处理、CLZ:处理中、SUCCESS:处理成功、FAIL:处理失败)			db_column: TASK_STATUS 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String taskStatus;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private Date modifyTime;
	//columns END

	public void setId(java.lang.String id) {
		this.id = id;
	}
	public java.lang.String getId() {
		return this.id;
	}
	public void setWithdrawalId(java.lang.String withdrawalId) {
		this.withdrawalId = withdrawalId;
	}
	public java.lang.String getWithdrawalId() {
		return this.withdrawalId;
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
	public void setTxFrom(java.lang.String txFrom) {
		this.txFrom = txFrom;
	}
	public java.lang.String getTxFrom() {
		return this.txFrom;
	}
	public void setTxTo(java.lang.String txTo) {
		this.txTo = txTo;
	}
	public java.lang.String getTxTo() {
		return this.txTo;
	}
	public void setAmount(BigInteger amount) {
		this.amount = amount;
	}
	public BigInteger getAmount() {
		return this.amount;
	}
	public void setFrozenAmount(BigInteger frozenAmount) {
		this.frozenAmount = frozenAmount;
	}
	public BigInteger getFrozenAmount() {
		return this.frozenAmount;
	}
	public void setFee(BigInteger fee) {
		this.fee = fee;
	}
	public BigInteger getFee() {
		return this.fee;
	}
	public void setTxHash(java.lang.String txHash) {
		this.txHash = txHash;
	}
	public java.lang.String getTxHash() {
		return this.txHash;
	}
	public void setStatus(java.lang.String status) {
		this.status = status;
	}
	public java.lang.String getStatus() {
		return this.status;
	}
	public void setTaskStatus(java.lang.String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public java.lang.String getTaskStatus() {
		return this.taskStatus;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return this.createTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public static Withdrawal convertWithdrawalReqToWithdrawal(WithdrawalApplyReq req) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setWithdrawalId(req.getWithdrawalId());
		withdrawal.setCoinType(req.getCoinType());
		withdrawal.setTxTo(req.getAddress());
		withdrawal.setAmount(req.getAmount());

		return withdrawal;
	}

	public String toString() {
		return new StringBuilder("Withdrawal [")
			.append("Id=").append(getId()).append(", ")
					.append("WithdrawalId=").append(getWithdrawalId()).append(", ")
					.append("CoinType=").append(getCoinType()).append(", ")
					.append("SubCoinType=").append(getSubCoinType()).append(", ")
					.append("TxFrom=").append(getTxFrom()).append(", ")
					.append("TxTo=").append(getTxTo()).append(", ")
					.append("Amount=").append(getAmount()).append(", ")
					.append("FrozenAmount=").append(getFrozenAmount()).append(", ")
					.append("Fee=").append(getFee()).append(", ")
					.append("TxHash=").append(getTxHash()).append(", ")
					.append("Status=").append(getStatus()).append(", ")
					.append("TaskStatus=").append(getTaskStatus()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

