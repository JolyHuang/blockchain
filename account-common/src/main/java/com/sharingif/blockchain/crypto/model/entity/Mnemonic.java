package com.sharingif.blockchain.crypto.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


public class Mnemonic implements java.io.Serializable, IObjectDateOperationHistory {

	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	/**
	 * id			db_column: ID
	 */
	@Length(max=32)
	private java.lang.String id;
	/**
	 * 别名			db_column: ALIAS
	 */
	@Length(max=200)
	private java.lang.String alias;
	/**
	 * 密码			db_column: PASSWORD
	 */
	@NotBlank @Length(max=500)
	private java.lang.String password;
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
	public void setAlias(java.lang.String alias) {
		this.alias = alias;
	}
	public java.lang.String getAlias() {
		return this.alias;
	}
	public void setPassword(java.lang.String password) {
		this.password = password;
	}
	public java.lang.String getPassword() {
		return this.password;
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
		return new StringBuilder("Mnemonic [")
				.append("Id=").append(getId()).append(", ")
				.append("Alias=").append(getAlias()).append(", ")
				.append("Password=").append(getPassword()).append(", ")
				.append("CreateTime=").append(getCreateTime()).append(", ")
				.append("ModifyTime=").append(getModifyTime())
				.append("]").toString();
	}

}