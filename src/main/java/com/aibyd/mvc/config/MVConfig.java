package com.aibyd.mvc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.aibyd.appsys.domain.AppsysMenu;
import com.aibyd.appsys.service.AppsysMenuService;

@Configuration
public class MVConfig implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MVConfig.class);

	@Autowired
	private AppsysMenuService menuService;

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		Iterable<AppsysMenu> menus = menuService.findAllMenu();
		if (menus == null) {
			return;
		}
		for (AppsysMenu menu : menus) {
			String url = menu.getPageUrl();
			LOGGER.info("Loading System Resources: " + url);
			if (url == null || "".equals(url)) {
				return;
			}
			if ("/".equals(url)) {
				registry.addViewController(url).setViewName("index");
				continue;
			}
			registry.addViewController(url).setViewName(url.substring(1, url.length()));
		}
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
	}

}
