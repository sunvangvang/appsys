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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Table;

@Entity(name = "appsys_user_role")
@Table(appliesTo = "appsys_user_role")
public class AppsysUserRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "sys_user_id")
	private long userId;

	@Column(name = "sys_role_id")
	private long roleId;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

}
