package com.aibyd.appsys.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aibyd.appsys.domain.AppsysRole;
import com.aibyd.appsys.service.AppsysMenuRoleService;
import com.aibyd.appsys.service.AppsysMenuService;
import com.aibyd.appsys.service.AppsysRoleService;
import com.aibyd.appsys.service.AppsysUserService;
import com.aibyd.appsys.utils.CommonUtils;
import com.aibyd.appsys.utils.JSONUtils;
import com.aibyd.nosql.RedisComp;

@RestController
@RequestMapping(path = "/appsys/role")
public class AppsysRoleController {

	@Autowired
	private RedisComp redisComp;

	private final AppsysUserService userService;

	private final AppsysMenuService menuService;

	private final AppsysMenuRoleService menuRoleService;

	private final AppsysRoleService roleService;

	private final HttpServletRequest request;

	private final Logger LOGGER = LoggerFactory.getLogger(AppsysUserController.class);

	public AppsysRoleController(AppsysUserService userService, AppsysMenuService menuService,
			AppsysMenuRoleService menuRoleService, AppsysRoleService roleService, HttpServletRequest request) {
		this.userService = userService;
		this.menuService = menuService;
		this.menuRoleService = menuRoleService;
		this.roleService = roleService;
		this.request = request;
	}

	@Transactional
	@PostMapping(path = "/save")
	public @ResponseBody JSONObject addRole(@RequestParam String formData) {
		JSONObject json = (JSONObject) JSON.parse(formData);
		AppsysRole roleTemp = new AppsysRole();
		LOGGER.info(formData);
		String roleId = json.getString("roleId");
		String roleName = json.getString("roleName");
		String roleCode = json.getString("roleCode");
		String roleDesc = json.getString("roleDesc");
		String sysFlag = json.getString("sysFlag");
		int resCount = 0;
		roleTemp.setRoleCode(roleCode);
		roleTemp.setRoleName(roleName);
		roleTemp.setRoleDesc(roleDesc);
		roleTemp.setSysFlag(sysFlag);
		if (StringUtils.isBlank(roleId)) {
			resCount = roleService.addRole(roleTemp);
		} else {
			long id = Long.valueOf(roleId);
			roleTemp.setId(id);
			resCount = roleService.updateRole(roleTemp);
		}
		return JSONUtils.convertObjectResponse(resCount, JSONUtils.RES_200);
	}

	@PostMapping(path = "/update")
	public @ResponseBody JSONObject updateUser(@RequestParam String formData) {
		JSONObject json = (JSONObject) JSON.parse(formData);
		String userName = json.getString("loginame");
		String valid = json.getString("valid");
		int rows = userService.updateValidByName(valid, userName);
		return JSONUtils.convertObjectResponse(rows, JSONUtils.RES_200);
	}

	@Transactional
	@PostMapping(path = "/delete")
	public @ResponseBody JSONObject deleteRoles(@RequestParam String idstr) {
		JSONObject respJSON = new JSONObject();
		JSONObject statJSON = new JSONObject();
		JSONObject dataJSON = new JSONObject();

		int delCount = 0;
		try {
			delCount = roleService.deleteRolesByIds(idstr);
		} catch (Exception e) {
			e.printStackTrace();
			StringBuilder sb = new StringBuilder();
			sb.append(new Date().toString());
			sb.append(": 请将该错误提示截图发送给管理员! ");
			sb.append(e.getClass().getName());
			sb.append(",");
			sb.append(e.getMessage());
			dataJSON.put("rows", delCount);
			statJSON.put("code", "500");
			statJSON.put("desc", sb.toString());
			respJSON.put("data", dataJSON);
			respJSON.put("status", statJSON);
			return respJSON;
		}
		dataJSON.put("rows", delCount);
		statJSON.put("code", "200");
		statJSON.put("desc", "删除成功!");
		respJSON.put("data", dataJSON);
		respJSON.put("status", statJSON);

		return respJSON;
	}

	@GetMapping(path = "/{roleId}")
	public @ResponseBody AppsysRole getUser(@PathVariable String roleId) {
		long rid = Long.valueOf(roleId);
		AppsysRole role = roleService.findSysRoleById(rid);
		return role;
	}

	@GetMapping(path = "/page")
	public @ResponseBody JSONObject getRolesByPage(@RequestParam String searchCode, @RequestParam String searchName,
			@RequestParam String searchDesc, @RequestParam String searchSys, @RequestParam String numPerPage,
			@RequestParam String currentPageNum) {

		JSONObject returnStatus = new JSONObject();
		JSONObject returnValue = new JSONObject();
		JSONObject returnPage = new JSONObject();
		AppsysRole searchRole = new AppsysRole();
		searchRole.setRoleCode(StringUtils.isEmpty(searchCode) ? "" : searchCode);
		searchRole.setRoleName(StringUtils.isEmpty(searchName) ? "" : searchName);
		searchRole.setRoleDesc(StringUtils.isEmpty(searchDesc) ? "" : searchDesc);
		searchRole.setSysFlag(StringUtils.isEmpty(searchSys) ? "" : searchSys);

		long pageSettings = Long.valueOf(numPerPage);
		long currentPage = Long.valueOf(currentPageNum);

		long totalRoleNum = roleService.findRoleNumber(searchRole);

		if (totalRoleNum == 0) {
			returnValue.put("data", null);

			returnStatus.put("code", "500");
			returnStatus.put("desc", "无数据!");

			returnValue.put("status", returnStatus);
			return returnValue;
		}

		if (pageSettings == 0) {
			returnValue.put("data", null);

			returnStatus.put("code", "500");
			returnStatus.put("desc", "分页设置错误!");

			returnValue.put("status", returnStatus);
			returnValue.put("totalpage", "0");
			return returnValue;
		}
		long totalPage = 0;
		if ((totalRoleNum % pageSettings) == 0) {
			totalPage = (totalRoleNum / pageSettings);
		} else {
			totalPage = (totalRoleNum / pageSettings) + 1;
		}
		if (currentPage < 1 || currentPage > totalPage) {
			returnValue.put("data", null);
			returnStatus.put("code", "500");
			returnStatus.put("desc", "分页设置错误!");
			returnValue.put("status", returnStatus);
			returnValue.put("totalpage", "0");
			return returnValue;
		}
		long pageOffset = (currentPage - 1) * pageSettings;

		String uri = request.getRequestURI();
		boolean isCached = redisComp.exists(uri);
		LOGGER.info("If the uri " + uri + " auth has cached: " + isCached);
		String authRoleIds = "";
		if (isCached) {
			authRoleIds = redisComp.getString(uri);
			LOGGER.info("The uri " + uri + " had granted for: " + authRoleIds);
		} else {
			if (isNotExistUriInDb(uri)) {
				returnValue.put("data", new JSONObject());
				returnStatus.put("code", "500");
				returnStatus.put("desc", "请联系管理员注册该功能！");
				returnValue.put("status", returnStatus);
				returnValue.put("totalpage", "0");
				return returnValue;
			}
			long menuId = menuService.findMenuIdByUri(uri);
			authRoleIds = menuRoleService.findRoleIdStrByMenuId(menuId);
			if (authRoleIds != null) {
				redisComp.setStringKeyValue(uri, authRoleIds);
			}
		}
		boolean isAuthed = false;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) auth.getPrincipal();
		Collection<? extends GrantedAuthority> coll = auth.getAuthorities();
		for (GrantedAuthority grantedRoleId : coll) {
			String roleId = grantedRoleId.getAuthority();
			isAuthed = CommonUtils.isContained(authRoleIds, roleId, ",");
			if (isAuthed) {
				break;
			}
		}
		if (!isAuthed) {
			LOGGER.info(currentUser.getUsername() + "<------>" + uri + " not have granted.");

			returnValue.put("data", new JSONObject());
			returnStatus.put("code", "403");
			returnStatus.put("desc", "无此功能授权！");
			returnValue.put("status", returnStatus);
			returnValue.put("totalpage", "0");

			return returnValue;
		}

		Iterable<Object[]> queryData = roleService.findRolesByPage(searchRole, pageOffset, pageSettings);

		JSONArray ja = new JSONArray();
		for (Object[] os : queryData) {
			JSONObject jo = new JSONObject();
			jo.put("id", os[0]);
			jo.put("roleName", os[1]);
			jo.put("roleCode", os[2]);
			jo.put("roleDesc", os[3]);
			jo.put("sysFlag", os[4]);
			Timestamp createTimeStamp = (Timestamp) os[5];
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			jo.put("createTime", sf.format(createTimeStamp));
			ja.add(jo);
		}

		returnPage.put("currentpage", currentPage);
		returnPage.put("totalpage", totalPage);
		returnPage.put("pagesettings", pageSettings);
		returnPage.put("totalrolenum", totalRoleNum);

		returnStatus.put("code", "200");
		returnStatus.put("desc", "查询成功!");

		returnValue.put("data", ja);
		returnValue.put("status", returnStatus);
		returnValue.put("page", returnPage);
		return returnValue;
	}

	private boolean isNotExistUriInDb(String uri) {
		boolean isExist = true;
		int countNum = menuService.findCountNumByUri(uri);
		if (countNum > 0) {
			isExist = false;
		}
		return isExist;
	}

}
