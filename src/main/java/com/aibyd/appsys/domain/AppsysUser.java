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

@Entity(name = "appsys_user")
@Table(appliesTo = "appsys_user")
public class AppsysUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "passwd")
	private String passwd;
	
	@Column(name = "create_time")
	private Timestamp createTime;

	@Column(name = "valid")
	private String valid;

	@Column(name = "version")
	private long version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswd() {
		return this.passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	
	public Timestamp getCreateTime() {
		return this.createTime;
	}
	
	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
