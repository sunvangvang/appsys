package com.aibyd.appsys.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibyd.appsys.domain.AppsysMenuRole;
import com.aibyd.appsys.domain.AppsysMenuRoleRepository;

@Service
public class AppsysMenuRoleService {

	private final AppsysMenuRoleRepository repository;

	@Autowired
	public AppsysMenuRoleService(AppsysMenuRoleRepository repository) {
		this.repository = repository;
	}

	public AppsysMenuRole findSysMenyRoleById(long id) {
		return repository.findSysMenuRoleById(id);
	}

	public Iterable<AppsysMenuRole> findSysRoleMenus() {
		return repository.findAll();
	}

	public Set<AppsysMenuRole> findSysMenuRolesByRole(long roleId) {
		return repository.findSysMenusByRoleId(roleId);
	}

	public Set<Long> findMenuIdsByRoleId(long roleId) {
		return repository.findMenuIdsByRoleId(roleId);
	}

	public Set<Long> findRoleIdsByMenuId(long menuId) {
		return repository.findRoleIdsByMenId(menuId);
	}

	public String findRoleIdStrByMenuId(long menuId) {
		return repository.findRoleIdStrByMenuId(menuId);
	}

}
