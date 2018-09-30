package com.aibyd.appsys.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.aibyd.appsys.domain.AppsysUser;

@Component
public class AppsysUserDetailsService implements UserDetailsService {

	@Autowired // 业务服务类
	private AppsysUserService userService;

	@Autowired // 业务服务类
	private AppsysUserRoleService userRoleService;

	// @Autowired // 业务服务类
	// private AppsysMenuRoleService menuRoleService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// SysUser对应数据库中的用户表，是最终存储用户和密码的表，可自定义
		// 本例使用SysUser中的name作为用户名:
		AppsysUser user = userService.findUserByName(userName);
		// Set<Long> permissionSet = new HashSet<Long>();
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
		if (user == null) {
			throw new UsernameNotFoundException("UserName " + userName + " not found");
		} else {
			Set<Long> roleIdSet = userRoleService.findRoleIdsByUserId(user.getId());
			if (roleIdSet != null) {
				for (long roleId : roleIdSet) {
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(String.valueOf(roleId));
					grantedAuthorities.add(grantedAuthority);
				}
			}
		}
		return new User(user.getUserName(), user.getPasswd(), grantedAuthorities);
	}

	// public UserDetails loadUserByUsername(String username) {
	// SysUser user = userDao.findByUserName(username);
	// if (user != null) {
	// List<Permission> permissions =
	// permissionDao.findByAdminUserId(user.getId());
	// List<GrantedAuthority> grantedAuthorities = new ArrayList <>();
	// for (Permission permission : permissions) {
	// if (permission != null && permission.getName()!=null) {
	//
	// GrantedAuthority grantedAuthority = new
	// SimpleGrantedAuthority(permission.getName());
	// //1：此处将权限信息添加到 GrantedAuthority 对象中，在后面进行全权限验证时会使用GrantedAuthority 对象。
	// grantedAuthorities.add(grantedAuthority);
	// }
	// }
	// return new User(user.getUsername(), user.getPassword(),
	// grantedAuthorities);
	// } else {
	// throw new UsernameNotFoundException("admin: " + username + " do not
	// exist!");
	// }
	// }

}