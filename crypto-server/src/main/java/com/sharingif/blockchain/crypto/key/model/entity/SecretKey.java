package com.sharingif.blockchain.crypto.key.model.entity;


import com.sharingif.cube.components.monitor.IObjectDateOperationHistory;
import com.sharingif.cube.components.sequence.Sequence;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


public class SecretKey implements java.io.Serializable, IObjectDateOperationHistory {
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
    /**
     * id			db_column: ID 
     */	
	@Length(max=32)
	@Sequence(ref="uuidSequenceGenerator")
	private String id;
    /**
     * 助记词id			db_column: MNEMONIC_ID
     */
	@Length(max=32)
	private String mnemonicId;
    /**
     * 扩展密钥id			db_column: EXTENDED_KEY_ID
     */
	@NotBlank @Length(max=32)
	private String extendedKeyId;
    /**
     * 地址			db_column: ADDRESS
     */
	@Length(max=100)
	private String address;
    /**
     * key路径			db_column: KEY_PATH
     */
	@Length(max=200)
	private String keyPath;
    /**
     * 文件路径			db_column: FILE_PATH
     */
	@Length(max=5000)
	private String filePath;
    /**
     * 创建时间			db_column: CREATE_TIME
     */

	private java.util.Date createTime;
    /**
     * 修改时间			db_column: MODIFY_TIME
     */

	private java.util.Date modifyTime;
	//columns END

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setMnemonicId(String mnemonicId) {
		this.mnemonicId = mnemonicId;
	}
	public String getMnemonicId() {
		return this.mnemonicId;
	}
	public void setExtendedKeyId(String extendedKeyId) {
		this.extendedKeyId = extendedKeyId;
	}
	public String getExtendedKeyId() {
		return this.extendedKeyId;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddress() {
		return this.address;
	}
	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}
	public String getKeyPath() {
		return this.keyPath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFilePath() {
		return this.filePath;
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
		return new StringBuilder("SecretKey [")
			.append("Id=").append(getId()).append(", ")
					.append("MnemonicId=").append(getMnemonicId()).append(", ")
					.append("ExtendedKeyId=").append(getExtendedKeyId()).append(", ")
					.append("Address=").append(getAddress()).append(", ")
					.append("KeyPath=").append(getKeyPath()).append(", ")
					.append("FilePath=").append(getFilePath()).append(", ")
					.append("CreateTime=").append(getCreateTime()).append(", ")
					.append("ModifyTime=").append(getModifyTime())
		.append("]").toString();
	}
	
}

