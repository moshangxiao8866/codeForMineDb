package com.github.msx.utils;

import com.jfinal.kit.PropKit;

public class TheConfig {
	// 数据库读取
	public static final String BASE_MYSQL_READ = "baseMysqlRead";
	public static final String KITCHEN2_READ = "kichen2read";
	public static final String KITCHEN2_WRITE = "kichen2write";

	// public static final String ECOLOGY_MYSQL_READ = "baseEcologyMysqlRead";

	public static final String BASE_MYSQL_WRITE = "baseMysqlWrite";

	public static String get(String key) {
		return PropKit.use("sysConfig.properties").get(key).trim();
	}

	// 设备参数keys
	public static final String IS_DANGER = get("IS_DANGER");
	public static final String TERMINAL_NO = get("TERMINAL_NO");
	public static final String LOCATION_COORDINATE = get("LOCATION_COORDINATE");
	public static final String STAY_TIME = get("STAY_TIME");
	public static final String GATHER_NUMBER_1 = get("GATHER_NUMBER_1");
	public static final String GATHER_NUMBER_2 = get("GATHER_NUMBER_2");

}
