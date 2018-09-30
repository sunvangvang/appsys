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

@Entity(name = "appsys_menu")
@Table(appliesTo = "appsys_menu")
public class AppsysMenu implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "pid")
	private long pid;

	@Column(name = "sys_id")
	private long sysId;

	@Column(name = "menu_name")
	private String menuName;

	@Column(name = "page_url")
	private String pageUrl;
	
	@Column(name = "level")
	private String level;
	
	@Column(name = "sort")
	private int sort;
	
//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sysRole")
//	private Set<AppsysMenuRole> sysMenuRoleSet = new HashSet<AppsysMenuRole>(0);

	@Column(name = "version")
	private long version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//	@OneToMany(fetch = FetchType.EAGER, mappedBy = "sysRole")
//	public Set<AppsysMenuRole> getSysMenuRoleSet() {
//		return sysMenuRoleSet;
//	}
//
//	public void setSysMenuRoleSet(Set<AppsysMenuRole> sysMenuRoleSet) {
//		this.sysMenuRoleSet = sysMenuRoleSet;
//	}
	
	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public long getSysId() {
		return sysId;
	}

	public void setSysId(long sysId) {
		this.sysId = sysId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
