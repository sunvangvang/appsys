/*
 * 
 * File Name: User.java
 * Author: arvin
 * E-Mail: arvin.y.sun@gmailcom
 * Created Time: 2017-06-12 10:45
 * Description: The app's login user entity
 * 
 */

package com.aibyd.appsys.domain;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Table;

@Entity(name = "appsys_role")
@Table(appliesTo = "appsys_role")
public class AppsysRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "role_code")
	private String roleCode;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "role_desc")
	private String roleDesc;

	@Column(name = "sys_flag")
	private String sysFlag;

	@Column(name = "create_time")
	private Timestamp createTime;

	@Column(name = "version")
	private long version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public String getSysFlag() {
		return sysFlag;
	}

	public void setSysFlag(String sysFlag) {
		this.sysFlag = sysFlag;
	}

	public Timestamp getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
