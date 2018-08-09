package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.web3j.abi.datatypes.Int;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;


public class TransactionBtcUtxo implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * btc单位
	 */
	public static final BigDecimal BTC_UNIT = new BigDecimal("100000000");

	/**
	 * 交易状态(WCL:未处理)
	 */
	public static final String TX_STATUS_UNTREATED = "WCL";
	/**
	 * 交易状态(CZCLZYTZ:充值处理中已通知)
	 */
	public static final String TX_STATUS_DEPOSIT_PROCESSING_NOTIFIED = "CZCLZYTZ";
	/**
	 * 交易状态(QXCLZYTZ:取现处理中已通知)
	 */
	public static final String TX_STATUS_WITHDRAWAL_PROCESSING_NOTIFIED = "QXCLZYTZ";
	/**
	 * 交易状态(QKQRSCLZ:区块确认数处理中)
	 */
	public static final String TX_STATUS_BLOCK_CONFIRMING = "QKQRSCLZ";
	/**
	 * 交易状态(YEWQR:余额未确认)
	 */
	public static final String TX_STATUS_BALANCE_UNCONFIRM = "YEWQR";
	/**
	 * 交易状态(YEQRYC:余额确认异常)
	 */
	public static final String TX_STATUS_BALANCE_ERROR = "YEQRYC";
	/**
	 * 交易状态(WX:无效)
	 */
	public static final String TX_STATUS_INVALID = "WX";
	/**
	 * 交易状态(YX:有效)
	 */
	public static final String TX_STATUS_VALID = "YX";
	/**
	 * 交易状态(QXCGYTZ:充值成功已通知)
	 */
	public static final String TX_STATUS_WITHDRAWAL_SUCCESS_NOTIFIED= "QXCGYTZ";
	/**
	 * 交易状态(CZCGYTZ:充值成功已通知)
	 */
	public static final String TX_STATUS_DEPOSIT_SUCCESS_NOTIFIED= "CZCGYTZ";
	/**
	 * 交易状态(CZSBYTZ:充值失败已通知)
	 */
	public static final String TX_STATUS_DEPOSIT_FAIL_NOTIFIED= "CZSBYTZ";
	/**
	 * 交易状态(QXSBYTZ:充值失败已通知)
	 */
	public static final String TX_STATUS_WITHDRAWAL_FAIL_NOTIFIED= "QXSBYTZ";

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

	/**
	 * 交易类型(0:转入)
	 */
	public static final String TX_TYPE_IN = "0";
	/**
	 * 交易类型(1:转出)
	 */
	public static final String TX_TYPE_OUT = "1";

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private String id;
    /**
     * 交易hash			db_column: TX_HASH 
     */	
	@NotBlank @Length(max=100)
	private String txHash;
    /**
     * vin交易hash			db_column: VIN_TX_HASH 
     */	
	@NotBlank @Length(max=100)
	private String vinTxHash;
    /**
     * vin tx index			db_column: VIN_TX_INDEX 
     */	
	
	private Integer vinTxIndex;
    /**
     * 区块号			db_column: BLOCK_NUMBER 
     */	
	
	private BigInteger blockNumber;
    /**
     * FORM地址			db_column: TX_FROM 
     */	
	@Length(max=100)
	private String txFrom;
    /**
     * TO地址			db_column: TX_TO 
     */	
	@Length(max=100)
	private String txTo;
    /**
     * 交易值			db_column: TX_VALUE 
     */	
	
	private BigInteger txValue;
    /**
     * tx index			db_column: TX_INDEX 
     */	
	
	private BigInteger txIndex;
    /**
     * actual fee			db_column: ACTUAL_FEE 
     */	
	private BigInteger actualFee;
    /**
     * 交易时间			db_column: TX_TIME 
     */	
	
	private Date txTime;
    /**
     * 确认区块数			db_column: CONFIRM_BLOCK_NUMBER 
     */	
	
	private Integer confirmBlockNumber;
    /**
     * 交易类型(0:转入、1:转出)			db_column: TX_TYPE 
     */	
	@NotBlank @Length(max=1)
	private String txType;
    /**
     * 交易状态(WCL:未处理、CZCLZYTZ:充值处理中已通知、QKQRSCLZ:区块确认数处理中、YEWQR:余额未确认、YEQRYC:余额确认异常、YX:有效、WX:无效)			db_column: TX_STATUS 
     */	
	@NotBlank @Length(max=20)
	private String txStatus;
    /**
     * 处理状态(WCL:未处理、CLZ:处理中、SUCCESS:处理成功、FAIL:处理失败)			db_column: TASK_STATUS 
     */	
	@NotBlank @Length(max=20)
	private String taskStatus;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private Date modifyTime;
	//columns END

	private List<String> txStatusArray;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTxHash() {
		return txHash;
	}

	public void setTxHash(String txHash) {
		this.txHash = txHash;
	}

	public String getVinTxHash() {
		return vinTxHash;
	}

	public void setVinTxHash(String vinTxHash) {
		this.vinTxHash = vinTxHash;
	}

	public Integer getVinTxIndex() {
		return vinTxIndex;
	}

	public void setVinTxIndex(Integer vinTxIndex) {
		this.vinTxIndex = vinTxIndex;
	}

	public BigInteger getBlockNumber() {
		return blockNumber;
	}

	public void setBlockNumber(BigInteger blockNumber) {
		this.blockNumber = blockNumber;
	}

	public String getTxFrom() {
		return txFrom;
	}

	public void setTxFrom(String txFrom) {
		this.txFrom = txFrom;
	}

	public String getTxTo() {
		return txTo;
	}

	public void setTxTo(String txTo) {
		this.txTo = txTo;
	}

	public BigInteger getTxValue() {
		return txValue;
	}

	public void setTxValue(BigInteger txValue) {
		this.txValue = txValue;
	}

	public BigInteger getTxIndex() {
		return txIndex;
	}

	public void setTxIndex(BigInteger txIndex) {
		this.txIndex = txIndex;
	}

	public BigInteger getActualFee() {
		return actualFee;
	}

	public void setActualFee(BigInteger actualFee) {
		this.actualFee = actualFee;
	}

	public Date getTxTime() {
		return txTime;
	}

	public void setTxTime(Date txTime) {
		this.txTime = txTime;
	}

	public Integer getConfirmBlockNumber() {
		return confirmBlockNumber;
	}

	public void setConfirmBlockNumber(Integer confirmBlockNumber) {
		this.confirmBlockNumber = confirmBlockNumber;
	}

	public String getTxType() {
		return txType;
	}

	public void setTxType(String txType) {
		this.txType = txType;
	}

	public String getTxStatus() {
		return txStatus;
	}

	public void setTxStatus(String txStatus) {
		this.txStatus = txStatus;
	}

	public String getTaskStatus() {
		return taskStatus;
	}

	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}

	@Override
	public Date getCreateTime() {
		return createTime;
	}

	@Override
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Date getModifyTime() {
		return modifyTime;
	}

	@Override
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}



	public List<String> getTxStatusArray() {
		return txStatusArray;
	}

	public void setTxStatusArray(List<String> txStatusArray) {
		this.txStatusArray = txStatusArray;
	}

	public String toString() {
		return new StringBuilder("TransactionBtcUtxo [")
			.append("Id=").append(getId()).append(", ")
					.append("TxHash=").append(getTxHash()).append(", ")
					.append("VinTxHash=").append(getVinTxHash()).append(", ")
					.append("VinTxIndex=").append(getVinTxIndex()).append(", ")
					.append("BlockNumber=").append(getBlockNumber()).append(", ")
					.append("TxFrom=").append(getTxFrom()).append(", ")
					.append("TxTo=").append(getTxTo()).append(", ")
					.append("TxValue=").append(getTxValue()).append(", ")
					.append("TxIndex=").append(getTxIndex()).append(", ")
					.append("ActualFee=").append(getActualFee()).append(", ")
					.append("TxTime=").append(getTxTime()).append(", ")
					.append("ConfirmBlockNumber=").append(getConfirmBlockNumber()).append(", ")
					.append("TxType=").append(getTxType()).append(", ")
					.append("TxStatus=").append(getTxStatus()).append(", ")
					.append("TaskStatus=").append(getTaskStatus()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

