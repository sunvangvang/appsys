package com.aibyd.appsys.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JSONUtils {

	public static final String RES_200 = "{\"code\":\"200\",\"desc\":\"请求成功!\"}";
	
	public static final String RES_400 = "{\"code\":\"400\",\"desc\":\"错误请求!\"}";

	public static final String RES_401 = "{\"code\":\"401\",\"desc\":\"身份验证错误!\"}";

	public static final String RES_403 = "{\"code\":\"403\",\"desc\":\"服务器拒绝请求!\"}";

	public static final JSONObject convertObjectResponse(Object o, String status) {
		JSONObject json = new JSONObject();
		JSONObject resStatus = JSON.parseObject(status);
		json.put("status", resStatus);
		json.put("data", o);
		return json;
	}

}
