package com.aibyd.nosql;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * redicache 工具类
 * 
 */
@Component
public class RedisComp {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	public String getString(final String key) {
		String value = stringRedisTemplate.opsForValue().get(key);
		return value;
	}

	public void setStringKeyValue(final String key, final String value) {
		stringRedisTemplate.opsForValue().set(key, value);
	}

	public void expire(final String key) {
		stringRedisTemplate.expire(key, -1, TimeUnit.SECONDS);
	}

	public void persist(final String key) {
		stringRedisTemplate.persist(key);
	}

}
