package com.github.msx.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Record;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

public class DateTimeUtil {

	/**
	 * 缺省的日期显示格式： yyyy-MM-dd
	 */
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public static final String[] NUMBERS = { "〇", "一", "二", "三", "四", "五", "六", "七", "八", "九" };

	/**
	 * 私有构造方法，禁止对该类进行实例化
	 */
	private DateTimeUtil() {
	}

	/**
	 * 字符串转时间
	 * 
	 * @param dateString
	 * @param pattern
	 * @return
	 */
	public static Date getDate(String dateString, String pattern) {
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(dateString);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;

	}

	/**
	 * 格式化时间
	 * 
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getDateString(Date date, String pattern) {
		if (pattern == null || "".equals(pattern)) {
			pattern = DEFAULT_DATE_FORMAT;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);

		return format.format(date);

	}

	/**
	 * 得到系统当前日期时间
	 * 
	 * @return 当前日期时间
	 */
	public static Date getNow() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * 得到用缺省方式格式化的当前日期
	 * 
	 * @return 当前日期
	 */
	public static String getDate() {
		return getDateTime(DEFAULT_DATE_FORMAT);
	}

	/**
	 * 得到用缺省方式格式化的当前日期及时间
	 * 
	 * @return 当前日期及时间
	 */
	public static String getDateTime() {
		return getDateTime(DEFAULT_DATETIME_FORMAT);
	}

	/**
	 * 得到系统当前日期及时间，并用指定的方式格式化
	 * 
	 * @param pattern 显示格式
	 * @return 当前日期及时间
	 */
	public static String getDateTime(String pattern) {
		Date datetime = Calendar.getInstance().getTime();
		return getDateTime(datetime, pattern);
	}

	/**
	 * 得到用指定方式格式化的日期
	 * 
	 * @param date    需要进行格式化的日期
	 * @param pattern 显示格式
	 * @return 日期时间字符串
	 */
	public static String getDateTime(Date date, String pattern) {
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATETIME_FORMAT;
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}

	/**
	 * 得到当前年份
	 * 
	 * @return 当前年份
	 */
	public static int getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 * 
	 * @return 当前月份
	 */
	public static int getCurrentMonth() {
		// 用get得到的月份数比实际的小1，需要加上
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * 得到当前日
	 * 
	 * @return 当前日
	 */
	public static int getCurrentDay() {
		return Calendar.getInstance().get(Calendar.DATE);
	}

	/**
	 * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
	 * 
	 * @param days 增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(int days) {
		return add(getNow(), days, Calendar.DATE);
	}

	/**
	 * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
	 * 
	 * @param date 基准日期
	 * @param days 增加的日期数
	 * @return 增加以后的日期
	 */
	public static Date addDays(Date date, int days) {
		return add(date, days, Calendar.DATE);
	}

	/**
	 * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
	 * 
	 * @param months 增加的月份数
	 * @return 增加以后的日期
	 */
	public static Date addMonths(int months) {
		return add(getNow(), months, Calendar.MONTH);
	}

	/**
	 * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
	 * 
	 * @param date   基准日期
	 * @param months 增加的月份数
	 * 
	 * @return 增加以后的日期
	 */
	public static Date addMonths(Date date, int months) {
		return add(date, months, Calendar.MONTH);
	}

	/**
	 * 内部方法。为指定日期增加相应的天数或月数
	 * 
	 * @param date   基准日期
	 * @param amount 增加的数量
	 * @param field  增加的单位，年，月或者日
	 * @return 增加以后的日期
	 */
	private static Date add(Date date, int amount, int field) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(field, amount);
		return calendar.getTime();
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffDays(Date one, Date two) {
		return (one.getTime() - two.getTime()) / (24 * 60 * 60 * 1000);
	}

	/**
	 * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return 两个日期相差月份数
	 */
	public static int diffMonths(Date one, Date two) {
		Calendar calendar = Calendar.getInstance();
		// 得到第一个日期的年分和月份数
		calendar.setTime(one);
		int yearOne = calendar.get(Calendar.YEAR);
		int monthOne = calendar.get(Calendar.MONDAY);
		// 得到第二个日期的年份和月份
		calendar.setTime(two);
		int yearTwo = calendar.get(Calendar.YEAR);
		int monthTwo = calendar.get(Calendar.MONDAY);
		return (yearOne - yearTwo) * 12 + (monthOne - monthTwo);
	}

	/**
	 * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
	 * 
	 * @param one 第一个日期数，作为基准
	 * @param two 第二个日期数，作为比较
	 * @return 两个日期相差天数
	 */
	public static long diffSecond(String beginTime, String endTime) throws ParseException {
		SimpleDateFormat dfs = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date begin = dfs.parse(beginTime);
		java.util.Date end = dfs.parse(endTime);
		long between = (end.getTime() - begin.getTime()) / 1000;// 除以1000是为了转换成秒

		// long minute1 = between % 3600 / 60;
		// long second1 = between % 60 / 60;
		return between;
	}

	/**
	 * 将一个字符串用给定的格式转换为日期类型。<br>
	 * 注意：如果返回null，则表示解析失败
	 * 
	 * @param datestr 需要解析的日期字符串
	 * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
	 * @return 解析后的日期
	 */
	public static Date parse(String datestr, String pattern) {
		Date date = null;
		if (null == pattern || "".equals(pattern)) {
			pattern = DEFAULT_DATE_FORMAT;
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			date = dateFormat.parse(datestr);
		} catch (ParseException e) {
		}
		return date;
	}

	/**
	 * 返回本月的最后一天
	 * 
	 * @return 本月最后一天的日期
	 */
	public static Date getMonthLastDay() {
		return getMonthLastDay(getNow());
	}

	/**
	 * 返回给定日期中的月份中的最后一天
	 * 
	 * @param date 基准日期
	 * @return 该月最后一天的日期
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}

	public static Date getMonthFirstDay() {
		return getMonthFirstDay(getNow());
	}

	/**
	 * 
	 * 返回给定日期中的月份中的第一天 zxl 2015年11月30日
	 */
	public static Date getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// 将日期设置为下一月第一天
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, 1);
		// 减去1天，得到的即本月的最后一天
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}

	/**
	 * 返回给定日期中的返回大写年月日
	 * 
	 * @param samRq 小写的年月日
	 * @return 大写年月日
	 */
	public static String rQ2CNRQ(String samRq) {
		String tmp = "";
		StringBuffer sb = new StringBuffer();
		// 转换年
		for (int i = 0; i < 4; i++) {
			tmp = samRq.substring(i, i + 1);
			sb.append(NUMBERS[Integer.parseInt(tmp)]);
		}
		sb.append("年");
		// 转换月
		tmp = samRq.substring(5, 6);
		// sb.append(NUMBERS[Integer.parseInt(tmp)]);
		if (!"0".equals(tmp)) {
			sb.append("十");
		}
		tmp = samRq.substring(6, 7);
		if (!"0".equals(tmp)) {
			sb.append(NUMBERS[Integer.parseInt(tmp)]);
		}
		sb.append("月");
		// 转换日
		tmp = samRq.substring(8, 9);
		if ("2".equals(tmp) || "3".equals(tmp)) {
			sb.append(NUMBERS[Integer.parseInt(tmp)]);
		}
		if (!"0".equals(tmp)) {
			sb.append("十");
		}
		tmp = samRq.substring(9, 10);
		if (!"0".equals(tmp)) {
			sb.append(NUMBERS[Integer.parseInt(tmp)]);
		}
		sb.append("日");

		return sb.toString();

	}

	/**
	 * 
	 * @param date 开始时间
	 * @param miu  分钟数
	 * @return
	 * @throws ParseException
	 */
	public static String addMins(String date, String addMinutes) {
		String tmp = "";
		SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1;
		try {
			d1 = sim.parse(date);
			long time = d1.getTime();
			Long tm = Long.parseLong(addMinutes) * 60 * 1000;
			time += tm;
			Date d2 = new Date(time);
			tmp = getDateTime(d2, "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tmp;
	}

	public static final Long getSJC() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		// System.out.println(getSJC());
		String pss = "url,abc,ef";
		String[] ps = pss.split(",");
		Record iot = new Record();
		iot.set("properties", "[{\"name\":\"url\",\"value\":\"12321\"},{\"name\":\"abc\",\"value\":\"321321\"}]");
		String proStr = iot.getStr("properties");
		JSONArray array = JSONUtil.parseArray(proStr);
		JSONArray newprop = JSONUtil.createArray();
		for (int j = 0; j < ps.length; j++) {
			String k = ps[j];
			String v = "";
			for (int i = 0; i < array.size(); i++)
				if (k.equals(array.getJSONObject(i).getStr("name"))) {
					v = array.getJSONObject(i).getStr("value");
				}
			JSONObject e = JSONUtil.createObj();
			e.put("name", k);
			e.put("value", v == null ? "" : v);
			newprop.add(e);
		}
		// iot.set("properties", JSONUtil.toJsonStr(newprop));
		System.out.println(JsonKit.toJson(newprop));
	}

	public static final Date getFisrtDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 6);
		} else {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		return new Date(calendar.getTimeInMillis());
	}

	public static final Date getCurrentFisrtDayOfWeek() {
		return getFisrtDayOfWeek(new Date());
	}

	public static final Date getLastDayOfWeek(Date date) {
		if (date == null) {
			return null;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 7);
		}
		return new Date(calendar.getTimeInMillis());
	}

	/**
	 * 
	 * 返回 yyyy-MM zxl 2016年5月5日 下午2:21:47
	 * 
	 * @return String
	 */
	public static String getCurrentMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String yearMonth = "";
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		if (month < 10) {
			yearMonth = year + "-0" + month;
		} else {
			yearMonth = year + "-" + month;
		}
		return yearMonth;
	}

	/**
	 * 返回 yyyy/mm
	 * 
	 * zxl 2016年6月23日 下午2:21:59
	 * 
	 * @return String
	 */
	public static String getCurrentYearMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);

		String yearMonth = year + "/" + month + "/" + day;

		return yearMonth;
	}

	/**
	 * 判断两个时间
	 * 
	 * @Description:
	 * @return:int
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compareBetweenDate(String date1, String date2) {
		DateTime dateTime1 = DateUtil.parseDateTime(date1);
		DateTime dateTime2 = DateUtil.parseDateTime(date2);
		if (dateTime1.isAfter(dateTime2)) {
			return 1;
		}
		if (dateTime1.isBefore(dateTime2)) {
			return -1;
		}
		return 0;
	}
}
