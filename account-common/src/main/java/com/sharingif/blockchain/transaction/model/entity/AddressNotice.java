package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


public class AddressNotice implements java.io.Serializable, IObjectDateOperationHistory {
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private java.lang.String id;
    /**
     * 地址注册id			db_column: ADDRESS_REGISTER_ID 
     */	
	@NotBlank @Length(max=32)
	private java.lang.String addressRegisterId;
    /**
     * 通知地址			db_column: NOTICE_ADDRESS 
     */	
	@Length(max=2000)
	private java.lang.String noticeAddress;
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
	public void setAddressRegisterId(java.lang.String addressRegisterId) {
		this.addressRegisterId = addressRegisterId;
	}
	public java.lang.String getAddressRegisterId() {
		return this.addressRegisterId;
	}
	public void setNoticeAddress(java.lang.String noticeAddress) {
		this.noticeAddress = noticeAddress;
	}
	public java.lang.String getNoticeAddress() {
		return this.noticeAddress;
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
		return new StringBuilder("AddressNotice [")
			.append("Id=").append(getId()).append(", ")
					.append("AddressRegisterId=").append(getAddressRegisterId()).append(", ")
					.append("NoticeAddress=").append(getNoticeAddress()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

