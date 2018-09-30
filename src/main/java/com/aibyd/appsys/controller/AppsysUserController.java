package com.aibyd.appsys.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

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
import com.aibyd.appsys.domain.AppsysUser;
import com.aibyd.appsys.service.AppsysMenuRoleService;
import com.aibyd.appsys.service.AppsysMenuService;
import com.aibyd.appsys.service.AppsysUserService;
import com.aibyd.appsys.utils.CommonUtils;
import com.aibyd.appsys.utils.JSONUtils;
import com.aibyd.appsys.utils.MD5Utils;
import com.aibyd.nosql.RedisComp;

@RestController
@RequestMapping(path = "/appsys/user")
public class AppsysUserController {

	@Autowired
	private RedisComp redisComp;

	private final AppsysUserService userService;

	private final AppsysMenuService menuService;

	private final AppsysMenuRoleService menuRoleService;

	private final HttpServletRequest request;

	private final Logger LOGGER = LoggerFactory.getLogger(AppsysUserController.class);

	public AppsysUserController(AppsysUserService userService, AppsysMenuService menuService,
			AppsysMenuRoleService menuRoleService, HttpServletRequest request) {
		this.userService = userService;
		this.menuService = menuService;
		this.menuRoleService = menuRoleService;
		this.request = request;
	}

	@PostMapping(path = "/add")
	public @ResponseBody JSONObject addUser(@RequestParam String formData) {
		JSONObject json = (JSONObject) JSON.parse(formData);
		AppsysUser userTemp = new AppsysUser();
		userTemp.setUserName(json.getString("loginame"));
		userTemp.setPasswd(MD5Utils.encode(json.getString("password")));
		userTemp.setCreateTime(new Timestamp(System.currentTimeMillis()));
		userTemp.setValid(json.getString("valid"));
		userTemp.setVersion(0L);
		AppsysUser user = userService.addUser(userTemp);
		return JSONUtils.convertObjectResponse(user, JSONUtils.RES_200);
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
	public @ResponseBody JSONObject deleteUsers(@RequestParam String idstr) {
		int rows = userService.deleteUsersByIds(idstr);
		return JSONUtils.convertObjectResponse(rows, JSONUtils.RES_200);
	}

	@GetMapping(path = "/{userId}")
	public @ResponseBody AppsysUser getUser(@PathVariable String userId) {
		long uid = Long.valueOf(userId);
		AppsysUser user = userService.findUserById(uid);
		LOGGER.info("加载所有用户数据！");
		return user;
	}

	@GetMapping(path = "/list")
	public @ResponseBody Iterable<AppsysUser> getUsers() {
		Iterable<AppsysUser> users = userService.findAllUsers();
		LOGGER.info("加载所有用户数据！");
		return users;
	}

	@GetMapping(path = "/page")
	public @ResponseBody JSONObject getUsersByPage(@RequestParam String numPerPage,
			@RequestParam String currentPageNum) {

		BigDecimal bd = new BigDecimal(0.0);
		boolean isEqual = (bd.doubleValue() == 0.0d);
		System.out.println(isEqual);
		System.out.println(MD5Utils.encode("0"));

		JSONObject returnValue = new JSONObject();

		long pageSettings = Long.valueOf(numPerPage);
		long currentPage = Long.valueOf(currentPageNum);

		long totalUserNum = userService.findUserNumber();
		if (pageSettings == 0) {
			returnValue.put("data", null);
			returnValue.put("status", "500");
			returnValue.put("totalpage", "0");
			return returnValue;
		}
		long totalPage = 0;
		if ((totalUserNum % pageSettings) == 0) {
			totalPage = (totalUserNum / pageSettings);
		} else {
			totalPage = (totalUserNum / pageSettings) + 1;
		}
		if (currentPage < 1 || currentPage > totalPage) {
			returnValue.put("data", null);
			returnValue.put("status", "500");
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
			returnValue.put("data", null);
			returnValue.put("status", "403");
			returnValue.put("totalpage", "0");
			return returnValue;
		}

		Iterable<Object[]> queryData = userService.findUsersByPage(pageOffset, pageSettings);

		JSONArray ja = new JSONArray();
		for (Object[] os : queryData) {
			JSONObject jo = new JSONObject();
			jo.put("id", os[0]);
			jo.put("username", os[1]);
			jo.put("valid", os[2]);
			ja.add(jo);
		}

		JSONObject returnPage = new JSONObject();
		returnPage.put("currentpage", currentPage);
		returnPage.put("totalpage", totalPage);
		returnPage.put("pagesettings", pageSettings);

		returnValue.put("data", ja);
		returnValue.put("status", "200");
		returnValue.put("page", returnPage);
		return returnValue;
	}

}
