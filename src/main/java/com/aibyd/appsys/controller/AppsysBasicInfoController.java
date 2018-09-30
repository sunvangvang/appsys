package com.aibyd.appsys.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.aibyd.appsys.domain.AppsysBasicInfo;
import com.aibyd.appsys.service.AppsysBasicInfoService;

@RestController
@RequestMapping(path = "/appsys/infos")
public class AppsysBasicInfoController {

	private final AppsysBasicInfoService infoService;

	// private final AppsysLogService logService;

	// private final HttpServletRequest request;

	public AppsysBasicInfoController(
			AppsysBasicInfoService infoService,
			HttpServletRequest request) {
		this.infoService = infoService;
		// this.request = request;
	}

	@GetMapping(path = "/province")
	public @ResponseBody List<AppsysBasicInfo> provinces(@RequestParam String type) {
		List<AppsysBasicInfo> infos = infoService.findBasicInfosByType(type);
		return infos;
	}
	
	@GetMapping(path = "/city")
	public @ResponseBody List<AppsysBasicInfo> citys(@RequestParam String pid) {
		long pidValue = Long.parseLong(pid);
		List<AppsysBasicInfo> citys = infoService.findBasicInfosByPid(pidValue);
		return citys;
	}
	
	@GetMapping(path = "/county")
	public @ResponseBody List<AppsysBasicInfo> countys(@RequestParam String pid) {
		long pidValue = Long.parseLong(pid);
		List<AppsysBasicInfo> countys = infoService.findBasicInfosByPid(pidValue);
		return countys;
	}
	
}
