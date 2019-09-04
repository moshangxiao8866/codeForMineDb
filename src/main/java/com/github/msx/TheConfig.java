package com.github.msx;

import cn.hutool.core.util.StrUtil;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

public class TheConfig {
	// 数据库读取
	public static final String BASE_MYSQL_READ = "baseMysqlRead";

	// public static final String ECOLOGY_MYSQL_READ = "baseEcologyMysqlRead";

	public static final String BASE_MYSQL_WRITE = "baseMysqlWrite";

	// 页面分页-每页大小
	public static final int PAGE_PAGESIZE = 10;

	public static Prop use(String name) {
		if (StrUtil.isBlank(name)) {
			return PropKit.use("sysConfig.properties");
		}
		return PropKit.use(name);
	}

	/** 获取配置文件 */
	public static String get(String key) {
		return TheConfig.use("sysConfig.properties").get(key).trim();
	}

	/** 获取配置文件 */
	public static String get(String name, String key) {
		return TheConfig.use(name).get(key).trim();
	}

	// 缓存库名称
	public static String REDIS_CACHE_NAME = get("redis.name");

	/* Token */
	public static final String TOKEN_PREFIX = "TOKEN_";

//	public static final String BASE_FILE_URL = get("BASE_FILE_URL");

	/**
	 * 查询人员定位信息的实时前置时间
	 */
	public static final Long LOCATION_HISTORY_LEAD_TIME = Long.valueOf(get("LOCATION_HISTORY_LEAD_TIME"));

	public static final long DIFF_SECOND = Long.valueOf(get("DIFF_SECOND"));

	public static final String DEVICE_LOCATION_BASE_URL = get("DEVICE_LOCATION_BASE_URL");
	
	public static final String GET_TEACHERIDS_OF_ONECLASS_URL = get("DEVICE_LOCATION_BASE_URL") + "getTeacherIdsOfOneClass";

	public static final String GET_PCRIDS_OF_ONEUNIT_URL = get("DEVICE_LOCATION_BASE_URL") + "getPcrIdsOfOneUnit";

	public static final String SEND_MSG_NO_TOKEN = get("send_msg");


	// 设备参数keys
	public static final String IS_DANGER = get("IS_DANGER");
	public static final String TERMINAL_NO = get("TERMINAL_NO");
	public static final String LOCATION_COORDINATE = get("LOCATION_COORDINATE");
	public static final String STAY_TIME = get("STAY_TIME");
	public static final String GATHER_NUMBER_1 = get("GATHER_NUMBER_1");
	public static final String GATHER_NUMBER_2 = get("GATHER_NUMBER_2");
}
