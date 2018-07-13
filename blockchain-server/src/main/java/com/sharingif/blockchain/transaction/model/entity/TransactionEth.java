package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigInteger;


public class TransactionEth implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 交易状态(0x0:失败)
	 */
	private static final String TX_STATUS_FAIL = "0x0";
	/**
	 * 交易状态(0x1:成功)
	 */
	private static final String TX_STATUS_SUCCESS = "0x1";

	/**
	 * 交易状态(S:成功、F:失败、P:处理中)
	 */
	public static final String TX_RECEIPT_STATUS_SUCCESS = "S";
	/**
	 * 交易状态(S:成功、F:失败、P:处理中)
	 */
	public static final String TX_RECEIPT_STATUS_FAIL = "F";

	/**
	 * 交易是否有效(WCL:未处理)
	 */
	public static final String TX_STATUS_UNTREATED = "WCL";

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
     * 交易hash			db_column: TX_HASH 
     */	
	@Length(max=100)
	private java.lang.String txHash;
    /**
     * 区块号			db_column: BLOCK_NUMBER 
     */	
	
	private BigInteger blockNumber;
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
     * 合约地址			db_column: CONTRACT_ADDRESS 
     */	
	@Length(max=100)
	private java.lang.String contractAddress;
    /**
     * input			db_column: TX_INPUT 
     */	
	@Length(max=1000)
	private java.lang.String txInput;
    /**
     * 交易值			db_column: TX_VALUE 
     */	
	@Length(max=100)
	private java.lang.String txValue;
    /**
     * tx index			db_column: TX_INDEX 
     */	
	
	private BigInteger txIndex;
    /**
     * gas limit			db_column: GAS_LIMIT 
     */	
	
	private BigInteger gasLimit;
    /**
     * gas used			db_column: GAS_USED 
     */	
	
	private BigInteger gasUsed;
    /**
     * gas price			db_column: GAS_PRICE 
     */	
	
	private BigInteger gasPrice;
    /**
     * actual fee			db_column: ACTUAL_FEE 
     */	
	
	private BigInteger actualFee;
    /**
     * nonce			db_column: NONCE 
     */	
	
	private BigInteger nonce;
    /**
     * 交易状态(S:成功、F:失败、P:处理中)			db_column: TX_RECEIPT_STATUS 
     */	
	@NotBlank @Length(max=1)
	private java.lang.String txReceiptStatus;
    /**
     * 交易时间			db_column: TX_TIME 
     */	
	
	private java.util.Date txTime;
    /**
     * 确认区块数			db_column: CONFIRM_BLOCK_NUMBER 
     */	
	
	private java.lang.Integer confirmBlockNumber;
    /**
     * 交易是否有效(WCL:未处理、CLZ:处理中、YX:有效、WX:无效)			db_column: TX_STATUS 
     */	
	@NotBlank @Length(max=10)
	private java.lang.String txStatus;
    /**
     * 交易类型(0:转入、1:转出)			db_column: TX_TYPE 
     */	
	@NotBlank @Length(max=1)
	private java.lang.String txType;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private java.util.Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private java.util.Date modifyTime;
	//columns END

	public void setTxHash(java.lang.String txHash) {
		this.txHash = txHash;
	}
	public java.lang.String getTxHash() {
		return this.txHash;
	}
	public void setBlockNumber(BigInteger blockNumber) {
		this.blockNumber = blockNumber;
	}
	public BigInteger getBlockNumber() {
		return this.blockNumber;
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
	public void setContractAddress(java.lang.String contractAddress) {
		this.contractAddress = contractAddress;
	}
	public java.lang.String getContractAddress() {
		return this.contractAddress;
	}
	public void setTxInput(java.lang.String txInput) {
		this.txInput = txInput;
	}
	public java.lang.String getTxInput() {
		return this.txInput;
	}
	public void setTxValue(java.lang.String txValue) {
		this.txValue = txValue;
	}
	public java.lang.String getTxValue() {
		return this.txValue;
	}
	public void setTxIndex(BigInteger txIndex) {
		this.txIndex = txIndex;
	}
	public BigInteger getTxIndex() {
		return this.txIndex;
	}
	public void setGasLimit(BigInteger gasLimit) {
		this.gasLimit = gasLimit;
	}
	public BigInteger getGasLimit() {
		return this.gasLimit;
	}
	public void setGasUsed(BigInteger gasUsed) {
		this.gasUsed = gasUsed;
	}
	public BigInteger getGasUsed() {
		return this.gasUsed;
	}
	public void setGasPrice(BigInteger gasPrice) {
		this.gasPrice = gasPrice;
	}
	public BigInteger getGasPrice() {
		return this.gasPrice;
	}
	public void setActualFee(BigInteger actualFee) {
		this.actualFee = actualFee;
	}
	public BigInteger getActualFee() {
		return this.actualFee;
	}
	public void setNonce(BigInteger nonce) {
		this.nonce = nonce;
	}
	public BigInteger getNonce() {
		return this.nonce;
	}
	public void setTxReceiptStatus(java.lang.String txReceiptStatus) {
		this.txReceiptStatus = txReceiptStatus;
	}
	public java.lang.String getTxReceiptStatus() {
		return this.txReceiptStatus;
	}
	public void setTxTime(java.util.Date txTime) {
		this.txTime = txTime;
	}
	public java.util.Date getTxTime() {
		return this.txTime;
	}
	public void setConfirmBlockNumber(java.lang.Integer confirmBlockNumber) {
		this.confirmBlockNumber = confirmBlockNumber;
	}
	public java.lang.Integer getConfirmBlockNumber() {
		return this.confirmBlockNumber;
	}
	public void setTxStatus(java.lang.String txStatus) {
		this.txStatus = txStatus;
	}
	public java.lang.String getTxStatus() {
		return this.txStatus;
	}
	public void setTxType(java.lang.String txType) {
		this.txType = txType;
	}
	public java.lang.String getTxType() {
		return this.txType;
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

	public void convertTxReceiptStatus(String status) {
		this.txReceiptStatus = (TX_STATUS_SUCCESS.equals(status) ? TX_RECEIPT_STATUS_SUCCESS : TX_RECEIPT_STATUS_FAIL);
	}

	public String toString() {
		return new StringBuilder("TransactionEth [")
			.append("TxHash=").append(getTxHash()).append(", ")
					.append("BlockNumber=").append(getBlockNumber()).append(", ")
					.append("TxFrom=").append(getTxFrom()).append(", ")
					.append("TxTo=").append(getTxTo()).append(", ")
					.append("ContractAddress=").append(getContractAddress()).append(", ")
					.append("TxInput=").append(getTxInput()).append(", ")
					.append("TxValue=").append(getTxValue()).append(", ")
					.append("TxIndex=").append(getTxIndex()).append(", ")
					.append("GasLimit=").append(getGasLimit()).append(", ")
					.append("GasUsed=").append(getGasUsed()).append(", ")
					.append("GasPrice=").append(getGasPrice()).append(", ")
					.append("ActualFee=").append(getActualFee()).append(", ")
					.append("Nonce=").append(getNonce()).append(", ")
					.append("TxReceiptStatus=").append(getTxReceiptStatus()).append(", ")
					.append("TxTime=").append(getTxTime()).append(", ")
					.append("ConfirmBlockNumber=").append(getConfirmBlockNumber()).append(", ")
					.append("TxStatus=").append(getTxStatus()).append(", ")
					.append("TxType=").append(getTxType()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

