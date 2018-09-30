package com.aibyd.appsys.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import com.aibyd.appsys.domain.AppsysMenu;
import com.aibyd.appsys.service.AppsysMenuRoleService;
import com.aibyd.appsys.service.AppsysMenuService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class RabcInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private AppsysMenuService menuService;

	@Autowired
	private AppsysMenuRoleService menuRoleService;

	private HashMap<String, Collection<ConfigAttribute>> requestMap = null;

	/**
	 * 加载权限表中所有权限
	 */
	public void loadResourceDefine() {
		requestMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
		Collection<ConfigAttribute> cfgs;
		ConfigAttribute cfg;
		Iterable<AppsysMenu> menus = menuService.findAllMenu();
		if (menus != null) {
			for (AppsysMenu menu : menus) {
				Set<Long> permissions = menuRoleService.findRoleIdsByMenuId(menu.getId());
				cfgs = new ArrayList<ConfigAttribute>();
				if (permissions != null) {
					for (long permission : permissions) {
						cfg = new SecurityConfig(String.valueOf(permission));
						cfgs.add(cfg);
					}
				}
				requestMap.put(menu.getPageUrl(), cfgs);
			}
		}
	}

	// 此方法是为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		if (requestMap == null) {
			loadResourceDefine();
		}
		// object 中包含用户请求的request 信息
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		AntPathRequestMatcher matcher;
		String resUrl;
		for (Iterator<String> iter = requestMap.keySet().iterator(); iter.hasNext();) {
			resUrl = iter.next();
			matcher = new AntPathRequestMatcher(resUrl);
			if (matcher.matches(request)) {
				return requestMap.get(resUrl);
			}
		}
		return new HashSet<>();
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new HashSet<>();
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}