package com.aibyd.appsys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.aibyd.appsys.domain.AppsysScheduleConfig;
import com.aibyd.appsys.domain.AppsysScheduleConfigRepository;

@Service
public class AppsysScheduleConfigService {

	private final AppsysScheduleConfigRepository repository;

	@Autowired
	public AppsysScheduleConfigService(AppsysScheduleConfigRepository repository) {
		this.repository = repository;
	}

	public AppsysScheduleConfig saveAppsysScheduleConfig(
			String appsysScheduleConfigJson) {
		AppsysScheduleConfig scheduleEntity = JSON.parseObject(
				appsysScheduleConfigJson, AppsysScheduleConfig.class);
		AppsysScheduleConfig saveScheduleConfig = repository
				.save(scheduleEntity);
		return saveScheduleConfig;
	}

	public AppsysScheduleConfig updateAppsysScheduleConfig(
			String appsysScheduleConfigJson) {
		AppsysScheduleConfig scheduleEntity = JSON.parseObject(
				appsysScheduleConfigJson, AppsysScheduleConfig.class);
		AppsysScheduleConfig saveScheduleConfig = repository
				.save(scheduleEntity);
		return saveScheduleConfig;
	}

}
