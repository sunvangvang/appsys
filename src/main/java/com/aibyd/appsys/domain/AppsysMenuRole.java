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

@Entity(name = "appsys_menu_role")
@Table(appliesTo = "appsys_menu_role")
public class AppsysMenuRole implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "sys_menu_id")
	private long menuId;
	
//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "sys_menu_id", insertable = false, updatable = false)
//	private AppsysMenu sysMenu;

	@Column(name = "sys_role_id")
	private long roleId;

//	@ManyToOne(fetch = FetchType.EAGER)
//	@JoinColumn(name = "sys_role_id", insertable = false, updatable = false)
//	private AppsysRole sysRole;
	
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

	public long getMenuId() {
		return menuId;
	}

	public void setMenuId(long menuId) {
		this.menuId = menuId;
	}
	
//	public AppsysMenu getSysMenu() {
//		return sysMenu;
//	}
//
//	public void setSysMenu(AppsysMenu sysMenu) {
//		this.sysMenu = sysMenu;
//	}

	public long getRoleId() {
		return roleId;
	}

	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}
	
//	public AppsysRole getSysRole() {
//		return sysRole;
//	}
//
//	public void setSysRole(AppsysRole sysRole) {
//		this.sysRole = sysRole;
//	}

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
