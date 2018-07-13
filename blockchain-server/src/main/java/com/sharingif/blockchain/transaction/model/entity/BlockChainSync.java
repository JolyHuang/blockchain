package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.math.BigInteger;
import java.util.Date;


public class BlockChainSync implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 区块类型(BTC:比特币、ETH:以太坊)			db_column: BLOCK_CHAIN_TYPE
	 */
	public static final String BLOCK_CHAIN_TYPE_ETH = "ETH";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * 区块类型(BTC:比特币、ETH:以太坊)			db_column: BLOCK_CHAIN_TYPE 
     */	
	@NotBlank @Length(max=50)
	private java.lang.String blockChainType;
    /**
     * 当前同步区块数			db_column: CURRENT_SYNC_BLOCK_NUMBER 
     */	
	
	private BigInteger currentSyncBlockNumber;
    /**
     * 创建时间			db_column: CREATE_TIME 
     */	
	
	private java.util.Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME 
     */	
	
	private java.util.Date modifyTime;
	//columns END


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBlockChainType() {
		return blockChainType;
	}

	public void setBlockChainType(String blockChainType) {
		this.blockChainType = blockChainType;
	}

	public BigInteger getCurrentSyncBlockNumber() {
		return currentSyncBlockNumber;
	}

	public void setCurrentSyncBlockNumber(BigInteger currentSyncBlockNumber) {
		this.currentSyncBlockNumber = currentSyncBlockNumber;
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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("BlockChainSync{");
		sb.append("id='").append(id).append('\'');
		sb.append(", blockChainType='").append(blockChainType).append('\'');
		sb.append(", currentSyncBlockNumber=").append(currentSyncBlockNumber);
		sb.append(", createTime=").append(createTime);
		sb.append(", modifyTime=").append(modifyTime);
		sb.append('}');
		return sb.toString();
	}
}

