package com.aibyd.appsys.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibyd.appsys.domain.AppsysUserRoleRepository;

@Service
public class AppsysUserRoleService {

	private final AppsysUserRoleRepository repository;

	@Autowired
	public AppsysUserRoleService(AppsysUserRoleRepository repository) {
		this.repository = repository;
	}

	public Set<Long> findRoleIdsByUserId(long userId) {
		return repository.findRoleIdsByUserId(userId);
	}

	public String findRoleIdStrByUserId(long userId) {
		return repository.findRoleIdStrByUserId(userId);
	}

}
