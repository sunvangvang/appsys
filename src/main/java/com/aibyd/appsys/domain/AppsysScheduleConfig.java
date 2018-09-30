package com.aibyd.appsys.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Table;

@Entity(name = "appsys_schedule_config")
@Table(appliesTo = "appsys_schedule_config")
public class AppsysScheduleConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "schedule_cron")
	private String scheduleCron;

	@Column(name = "schedule_name")
	private String scheduleName;

	@Column(name = "schedule_desc")
	private String scheduleDesc;

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

	public String getScheduleCron() {
		return scheduleCron;
	}

	public void setScheduleCron(String scheduleCron) {
		this.scheduleCron = scheduleCron;
	}

	public String getScheduleName() {
		return scheduleName;
	}

	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}

	public String getScheduleDesc() {
		return scheduleDesc;
	}

	public void setScheduleDesc(String scheduleDesc) {
		this.scheduleDesc = scheduleDesc;
	}

	public String getValid() {
		return this.valid;
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
