package com.aibyd.appsys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Table;

@Entity(name = "appsys_basic_info")
@Table(appliesTo = "appsys_basic_info")
public class AppsysBasicInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "pid")
	private long pid;

	@Column(name = "info_code")
	private String infoCode;

	@Column(name = "info_name")
	private String infoName;

	@Column(name = "info_type")
	private String infoType;

	@Column(name = "version")
	private long version;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPid() {
		return pid;
	}

	public void setPid(long pid) {
		this.pid = pid;
	}

	public String getInfoCode() {
		return infoCode;
	}

	public void setInfoCode(String infoCode) {
		this.infoCode = infoCode;
	}

	public String getInfoName() {
		return infoName;
	}

	public void setInfoName(String infoName) {
		this.infoName = infoName;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	public long getVersion() {
		return this.version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
