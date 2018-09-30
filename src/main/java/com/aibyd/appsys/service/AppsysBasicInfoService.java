package com.aibyd.appsys.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibyd.appsys.domain.AppsysBasicInfo;
import com.aibyd.appsys.domain.AppsysBasicInfoRepository;

@Service
public class AppsysBasicInfoService {

	private final AppsysBasicInfoRepository repository;

	@Autowired
	public AppsysBasicInfoService(AppsysBasicInfoRepository repository) {
		this.repository = repository;
	}

	public List<AppsysBasicInfo> findBasicInfosByType(String type) {
		return repository.findBasicInfosByType(type);
	}
	
	public List<AppsysBasicInfo> findBasicInfosByPid(long pid) {
		return repository.findBasicInfosByPid(pid);
	}

}
