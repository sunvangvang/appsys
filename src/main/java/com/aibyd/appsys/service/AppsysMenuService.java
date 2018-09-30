package com.aibyd.appsys.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aibyd.appsys.domain.AppsysMenu;
import com.aibyd.appsys.domain.AppsysMenuRepository;

@Service
public class AppsysMenuService {

	private final AppsysMenuRepository repository;

	@Autowired
	public AppsysMenuService(AppsysMenuRepository repository) {
		this.repository = repository;
	}

	public Set<Long> findMenuIds() {
		return repository.findMenuIds();
	}

	public Iterable<AppsysMenu> findAllMenu() {
		return repository.findAll();
	}

	public long findMenuIdByUri(String pageUrl) {
		return repository.findMenuIdByUri(pageUrl);
	}

	public Iterable<Object[]> findMenuNameUriLevelSortById(long menuId) {
		return repository.findMenuNameUriLevelSortById(menuId);
	}

	public Iterable<Object[]> findFuncMenus() {
		return repository.findFuncMenus();
	}
	
	public AppsysMenu save(AppsysMenu menu) {
		return repository.save(menu);
	}
	
	public int findCountNumByUri(String uri) {
		return repository.findCountNumByUri(uri);
	}

}
