package com.sharingif.blockchain.transaction.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


public class AddressNotice implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 通知类型(deposit:充值)
	 */
	public static final String NOTICE_TYPE_DEPOSIT = "deposit";
	
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
     * 通知地址			db_column: NOTICE_ADDRESS 
     */	
	@NotBlank @Length(max=2000)
	private java.lang.String noticeAddress;
    /**
     * 通知类型(deposit:充值)			db_column: NOTICE_TYPE 
     */	
	@NotBlank @Length(max=20)
	private java.lang.String noticeType;
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
	public void setNoticeAddress(java.lang.String noticeAddress) {
		this.noticeAddress = noticeAddress;
	}
	public java.lang.String getNoticeAddress() {
		return this.noticeAddress;
	}
	public void setNoticeType(java.lang.String noticeType) {
		this.noticeType = noticeType;
	}
	public java.lang.String getNoticeType() {
		return this.noticeType;
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
					.append("Address=").append(getAddress()).append(", ")
					.append("CoinType=").append(getCoinType()).append(", ")
					.append("NoticeAddress=").append(getNoticeAddress()).append(", ")
					.append("NoticeType=").append(getNoticeType()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

