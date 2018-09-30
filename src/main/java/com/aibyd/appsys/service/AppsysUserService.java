package com.aibyd.appsys.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.aibyd.appsys.domain.AppsysUser;
import com.aibyd.appsys.domain.AppsysUserRepository;

@Service
public class AppsysUserService {

	private final AppsysUserRepository repository;

	@Autowired
	public AppsysUserService(AppsysUserRepository repository) {
		this.repository = repository;
	}

	public AppsysUser findUserByName(String userName) {
		return repository.findUserByName(userName);
	}

	public AppsysUser findUserById(long userId) {
		return repository.findUserById(userId);
	}

	public AppsysUser addUser(AppsysUser inUser) {
		AppsysUser outUser = repository.save(inUser);
		return outUser;
	}

	public int updateValidByName(String valid, String userName) {
		return repository.updateValidByName(valid, userName);
	}

	public int deleteUserById(long id) {
		return repository.deleteUserById(id);
	}

	@Transactional(rollbackFor = { Exception.class }, value = "transactionManagerAppsys")
	public int deleteUsersByIds(String ids) {
		JSONArray jsa = JSON.parseArray(ids);
		int count = 0;
		for (int i = 0; i < jsa.size(); i++) {
			String idStr = jsa.getString(i);
			long id = Long.valueOf(idStr);
			count = count + repository.deleteUserById(id);
		}
		return count;
	}

	public Iterable<AppsysUser> findAllUsers() {
		return repository.findAll();
	}

	public Iterable<Object[]> findUsersByPage(long offset, long settings) {
		return repository.findUsersByPage(offset, settings);
	}

	public long findUserNumber() {
		return repository.findUserNumber();
	}

	public long findUserIdByUserName(String userName) {
		return repository.findUserIdByUserName(userName);
	}

	public Iterable<String> findAllUsersName() {
		return repository.findAllUsersName();
	}

}
