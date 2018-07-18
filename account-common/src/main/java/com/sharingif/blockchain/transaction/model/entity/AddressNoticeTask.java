package com.sharingif.blockchain.transaction.model.entity;


import javax.validation.constraints.*;

import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.*;


public class AddressNoticeTask implements java.io.Serializable, IObjectDateOperationHistory {
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * 地址通知id			db_column: ADDRESS_NOTICE_ID 
     */	
	@NotBlank @Length(max=32)
	private java.lang.String addressNoticeId;
    /**
     * 类型(CZCLZ:充值处理中、CZQKQRZ:充值区块确认中、CZCG:充值成功)			db_column: TYPE 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String type;
    /**
     * 处理状态(WCL:未处理、CLZ:处理中、SUCCESS:处理成功、FAIL:处理失败)			db_column: STATUS 
     */	
	@NotBlank @Length(max=100)
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
	public void setAddressNoticeId(java.lang.String addressNoticeId) {
		this.addressNoticeId = addressNoticeId;
	}
	public java.lang.String getAddressNoticeId() {
		return this.addressNoticeId;
	}
	public void setType(java.lang.String type) {
		this.type = type;
	}
	public java.lang.String getType() {
		return this.type;
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
		return new StringBuilder("AddressNoticeTask [")
			.append("Id=").append(getId()).append(", ")
					.append("AddressNoticeId=").append(getAddressNoticeId()).append(", ")
					.append("Type=").append(getType()).append(", ")
					.append("Status=").append(getStatus()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

