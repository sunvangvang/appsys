package com.aibyd.appsys.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.aibyd.appsys.domain.AppsysMenu;
import com.aibyd.appsys.service.AppsysMenuService;
import com.aibyd.nosql.RedisComp;

@RestController
@RequestMapping(path = "/appsys/menu")
public class AppsysMenuController {

	@Autowired
	private RedisComp redisComp;

	private final AppsysMenuService menuService;

	private final Logger LOGGER = LoggerFactory.getLogger(AppsysMenuController.class);

	public AppsysMenuController(AppsysMenuService menuService, HttpServletRequest request) {
		this.menuService = menuService;
	}

	@GetMapping(path = "/tree")
	public @ResponseBody JSONObject findMenuTree() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails currentUser = (UserDetails) auth.getPrincipal();
		String userName = currentUser.getUsername();
		JSONObject json = new JSONObject();
		boolean isUserMenuCached = redisComp.exists("user-menu");
		if (!isUserMenuCached) {
			json.put("data", null);
			JSONObject statusJO = new JSONObject();
			statusJO.put("code", "error");
			statusJO.put("desc", "user-menu not have cached!");
			json.put("status", statusJO);
			return json;
		}
		String userMenuCachedStr = redisComp.getString("user-menu");
		JSONObject userMenuCachedJSON = JSON.parseObject(userMenuCachedStr);
		JSONArray userMenuCachedJSONArray = userMenuCachedJSON.getJSONArray("user-menu");
		if (userMenuCachedJSONArray == null || userMenuCachedJSONArray.size() <= 0) {
			json.put("data", null);
			JSONObject statusJO = new JSONObject();
			statusJO.put("code", "error");
			statusJO.put("desc", "user-menu have cached null data!");
			json.put("status", statusJO);
			return json;
		}
		for (int i = 0; i < userMenuCachedJSONArray.size(); i++) {
			JSONObject userMenuJO = userMenuCachedJSONArray.getJSONObject(i);
			if (!userMenuJO.containsKey(userName)) {
				continue;
			} else {
				JSONArray menuCacheJA = userMenuJO.getJSONArray(userName);
				json.put("data", menuCacheJA);
				JSONObject statusJO = new JSONObject();
				statusJO.put("code", "success");
				statusJO.put("desc", "search user-menu cache done!");
				json.put("status", statusJO);
				break;
			}
		}
		return json;
	}

	@GetMapping(path = "/menus")
	public @ResponseBody JSONObject findAllMenu() {
		List<AppsysMenu> menuList = new ArrayList<AppsysMenu>();
		JSONObject returnJO = new JSONObject();
		Iterable<Object[]> menusIter = menuService.findFuncMenus();
		if (menusIter == null) {
			returnJO.put("data", null);
			JSONObject statusJO = new JSONObject();
			statusJO.put("code", "error");
			statusJO.put("desc", "Don't have any menu data!");
			returnJO.put("status", statusJO);
			return returnJO;
		}
		for (Object[] query : menusIter) {
			AppsysMenu menu = new AppsysMenu();
			int menuId = (int) query[0];
			int parentMenuId = (int) query[1];
			String menuName = (String) query[2];
			String menuUri = (String) query[3];
			char menuLevel = (char) query[4];
			int menuSort = (int) query[5];
			menu.setId(menuId);
			menu.setPid(parentMenuId);
			menu.setMenuName(menuName);
			menu.setPageUrl(menuUri);
			menu.setLevel(String.valueOf(menuLevel));
			menu.setSort(menuSort);
			menuList.add(menu);
		}
		JSONArray menusJA = assembleMenusJSON(0, menuList);
		returnJO.put("data", menusJA);
		JSONObject statusJO = new JSONObject();
		statusJO.put("code", "success");
		statusJO.put("desc", "Find all menu sucess!");
		returnJO.put("status", statusJO);
		LOGGER.info("所有功能菜单数量：" + returnJO.toJSONString());
		return returnJO;

	}

	@GetMapping(path = "/{menuId}")
	public JSONObject findMenuById(@PathVariable String menuId) {
		LOGGER.info("获取菜单标识： " + menuId);
		JSONObject returnJO = new JSONObject();
		long mid = Long.valueOf(menuId);
		Iterable<Object[]> menuIter = menuService.findMenuNameUriLevelSortById(mid);
		JSONArray menuJSONArray = new JSONArray();
		if (menuIter != null) {
			for (Object[] menuObj : menuIter) {
				JSONObject menuJSONObject = new JSONObject();
				String menuName = (String) menuObj[0];
				String menuUri = (String) menuObj[1];
				char menuLevel = (char) menuObj[2];
				int menuSort = (int) menuObj[3];
				int pMenuId = (int) menuObj[4];
				menuJSONObject.put("menuId", String.valueOf(menuId));
				menuJSONObject.put("menuName", menuName);
				menuJSONObject.put("menuUri", menuUri);
				menuJSONObject.put("menuLevel", menuLevel);
				menuJSONObject.put("menuSort", String.valueOf(menuSort));
				menuJSONObject.put("menuParent", String.valueOf(pMenuId));
				menuJSONArray.add(menuJSONObject);
			}

			JSONObject statusJO = new JSONObject();
			statusJO.put("statusCode", "1");
			statusJO.put("statusDesc", "success");

			returnJO.put("data", menuJSONArray);
			returnJO.put("status", statusJO);

		} else {
			JSONObject statusJO = new JSONObject();
			statusJO.put("statusCode", "0");
			statusJO.put("statusDesc", "fail");
			returnJO.put("data", new JSONObject());
			returnJO.put("status", statusJO);
		}
		return returnJO;
	}

	private JSONArray assembleMenusJSON(long pid, List<AppsysMenu> menuList) {
		JSONArray returnJA = new JSONArray();
		if (menuList == null || menuList.size() <= 0) {
			return returnJA;
		}
		List<AppsysMenu> childMenus = new ArrayList<AppsysMenu>();
		if (0 == pid) {
			for (AppsysMenu menu : menuList) {
				if (pid != menu.getPid()) {
					continue;
				}
				childMenus.add(menu);
			}
		} else {
			childMenus = menuList;
		}
		Collections.sort(childMenus, new Comparator<AppsysMenu>() {
			@Override
			public int compare(AppsysMenu first, AppsysMenu second) {
				int firstSort = first.getSort();
				int secondSort = second.getSort();
				if (firstSort > secondSort) {
					return 1;
				}
				if (firstSort < secondSort) {
					return -1;
				}
				return 0;
			}
		});
		for (AppsysMenu menu : childMenus) {
			List<AppsysMenu> grantSonMenus = new ArrayList<AppsysMenu>();
			long id = menu.getId();
			for (AppsysMenu grantSonMenu : menuList) {
				long grantSonPid = grantSonMenu.getPid();
				if (id != grantSonPid) {
					continue;
				}
				grantSonMenus.add(grantSonMenu);
			}
			JSONObject menuJO = new JSONObject();
			menuJO.put("menuId", menu.getId());
			menuJO.put("parentMenuId", menu.getPid());
			menuJO.put("menuName", menu.getMenuName());
			menuJO.put("menuUri", menu.getPageUrl());
			menuJO.put("menuLevel", menu.getLevel());
			menuJO.put("menuSort", menu.getSort());
			menuJO.put("childMenus", assembleMenusJSON(id, grantSonMenus));
			returnJA.add(menuJO);
		}
		return returnJA;
	}

	@PostMapping(path = "/add")
	public @ResponseBody JSONObject save(@RequestParam String formData) {
		JSONObject returnJO = new JSONObject();
		JSONObject statusJO = new JSONObject();
		AppsysMenu newMenu = new AppsysMenu();
		JSONObject menuJO = JSON.parseObject(formData);
		String idStr = menuJO.getString("menuId");
		String menuName = menuJO.getString("menuName");
		String menuUri = menuJO.getString("menuUri");
		String menuLevel = menuJO.getString("menuLevel");
		String menuSortStr = menuJO.getString("menuSort");
		int menuSort = Integer.valueOf(menuSortStr);
		if (idStr.contains("parent")) {
			String parentId = idStr.split("-")[0];
			long pid = Long.valueOf(parentId);
			newMenu.setMenuName(menuName);
			newMenu.setPageUrl(menuUri);
			newMenu.setPid(pid);
			newMenu.setSort(menuSort);
			newMenu.setLevel(menuLevel);
			AppsysMenu menu = menuService.save(newMenu);
			returnJO.put("data", menu.getId());
			statusJO.put("code", "1");
			statusJO.put("desc", "success!");
			returnJO.put("status", statusJO);
		} else {
			// newMenu.setMenuName(menuName);
			// newMenu.setPageUrl(menuUri);
			// newMenu.setSort(menuSort);
			// newMenu.setLevel(menuLevel);
			// AppsysMenu menu = menuService.save(newMenu);
		}

		return returnJO;
	}

}
