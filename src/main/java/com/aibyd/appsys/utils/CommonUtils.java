package com.aibyd.appsys.utils;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class CommonUtils {

	public static String getIpFromRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static boolean isContained(String src, String target, String split) {
		if (src == null || target == null || split == null) {
			return false;
		}
		String[] srcArray = src.split(split);
		List<String> srcList = Arrays.asList(srcArray);
		if (srcList.contains(target)) {
			return true;
		}
		return false;
	}

}
