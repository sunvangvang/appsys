package com.aibyd.appsys.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aibyd.appsys.domain.AppsysRole;
import com.aibyd.appsys.domain.AppsysRoleRepository;

@Service
public class AppsysRoleService {

	private final AppsysRoleRepository repository;

	@Autowired
	public AppsysRoleService(AppsysRoleRepository repository) {
		this.repository = repository;
	}

	@Transactional(rollbackFor = { Exception.class }, value = "transactionManagerAppsys")
	public int addRole(AppsysRole role) {
		String roleCode = role.getRoleCode();
		String roleName = role.getRoleName();
		String roleDesc = role.getRoleDesc();
		String sysFlag = role.getSysFlag();
		int resCount = repository.addRole(roleCode, roleName, roleDesc, sysFlag);
		return resCount;
	}

	@Transactional(rollbackFor = { Exception.class }, value = "transactionManagerAppsys")
	public int updateRole(AppsysRole role) {
		long id = role.getId();
		String roleCode = role.getRoleCode();
		String roleName = role.getRoleName();
		String roleDesc = role.getRoleDesc();
		String sysFlag = role.getSysFlag();
		int resCount = repository.updateRole(id, roleCode, roleName, roleDesc, sysFlag);
		return resCount;
	}

	@Transactional(rollbackFor = { Exception.class }, value = "transactionManagerAppsys")
	public int deleteRolesByIds(String ids) throws Exception {
		JSONArray jsa = JSON.parseArray(ids);
		int count = 0;
		for (int i = 0; i < jsa.size(); i++) {
			if (i == 2) {
				throw new Exception("异常测试");
			}
			String idStr = jsa.getString(i);
			long id = Long.valueOf(idStr);
			count = count + repository.deleteRoleById(id);
		}
		return count;
	}

	public AppsysRole findSysRoleById(long id) {
		return repository.findSysRoleById(id);
	}

	public Iterable<AppsysRole> findSysRoles() {
		Iterable<AppsysRole> roles = new HashSet<AppsysRole>();
		roles = repository.findAll();
		return roles;
	}

	public Set<Long> findRoleIds() {
		return repository.findRoleIds();
	}

	public long findRoleNumber(AppsysRole searchRole) {
		return repository.findRoleNumber(searchRole.getRoleCode(), searchRole.getRoleName(), searchRole.getRoleDesc(),
				searchRole.getSysFlag());
	}

	public Iterable<Object[]> findRolesByPage(AppsysRole searchRole, long offset, long settings) {
		return repository.findRolesByPage(searchRole.getRoleCode(), searchRole.getRoleName(), searchRole.getRoleDesc(),
				searchRole.getSysFlag(), offset, settings);
	}

}
