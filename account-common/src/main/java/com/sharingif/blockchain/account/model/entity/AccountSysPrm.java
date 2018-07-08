package com.sharingif.blockchain.account.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


public class AccountSysPrm implements java.io.Serializable, IObjectDateOperationHistory {

	/**
	 * 参数状态(01:有效)
	 */
	public static final String PRM_STATUS_VALID = "01";
	/**
	 * 参数状态(02:失效)
	 */
	public static final String PRM_STATUS_INVALID = "02";


	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	private java.lang.String id;
    /**
     * 参数名称			db_column: PRM_NAME 
     */	
	@NotBlank @Length(max=100)
	private java.lang.String prmName;
    /**
     * 参数值			db_column: PRM_VALUE 
     */	
	@NotBlank @Length(max=200)
	private java.lang.String prmValue;
    /**
     * 参数描述			db_column: PRM_DESC 
     */	
	@Length(max=200)
	private java.lang.String prmDesc;
    /**
     * 参数状态(01:有效、02:失效)			db_column: PRM_STATUS 
     */	
	@NotBlank @Length(max=2)
	private java.lang.String prmStatus;
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
	public void setPrmName(java.lang.String prmName) {
		this.prmName = prmName;
	}
	public java.lang.String getPrmName() {
		return this.prmName;
	}
	public void setPrmValue(java.lang.String prmValue) {
		this.prmValue = prmValue;
	}
	public java.lang.String getPrmValue() {
		return this.prmValue;
	}
	public void setPrmDesc(java.lang.String prmDesc) {
		this.prmDesc = prmDesc;
	}
	public java.lang.String getPrmDesc() {
		return this.prmDesc;
	}
	public void setPrmStatus(java.lang.String prmStatus) {
		this.prmStatus = prmStatus;
	}
	public java.lang.String getPrmStatus() {
		return this.prmStatus;
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
		return new StringBuilder("AccountSysPrm [")
			.append("Id=").append(getId()).append(", ")
					.append("PrmName=").append(getPrmName()).append(", ")
					.append("PrmValue=").append(getPrmValue()).append(", ")
					.append("PrmDesc=").append(getPrmDesc()).append(", ")
					.append("PrmStatus=").append(getPrmStatus()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

