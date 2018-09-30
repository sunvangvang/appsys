package com.aibyd.appsys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aibyd.appsys.domain.AppsysScheduleConfig;
import com.aibyd.appsys.service.AppsysScheduleConfigService;

@RestController
@RequestMapping(path = "/appsys/scheduleconf")
public class AppsysScheduleConfigController {

	private final AppsysScheduleConfigService scheduleConfigService;

	// private final AppsysLogService logService;

	// private final HttpServletRequest request;

	public AppsysScheduleConfigController(
			AppsysScheduleConfigService scheduleConfigService,
			HttpServletRequest request) {
		this.scheduleConfigService = scheduleConfigService;
		// this.request = request;
	}

	@PostMapping(path = "/add")
	public AppsysScheduleConfig saveAppsysScheduleConfig(
			@RequestParam String appsysScheduleConfigJson) {
		AppsysScheduleConfig savedSchedule = scheduleConfigService
				.saveAppsysScheduleConfig(appsysScheduleConfigJson);
		return savedSchedule;
	}

	@PostMapping(path = "/update")
	public AppsysScheduleConfig updateAppsysScheduleConfig(
			@RequestParam String appsysScheduleConfigJson) {
		AppsysScheduleConfig updatedSchedule = scheduleConfigService
				.updateAppsysScheduleConfig(appsysScheduleConfigJson);
		return updatedSchedule;
	}

}
