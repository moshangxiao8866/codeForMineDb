package com.jfinal.ext.plugin.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.log.Log;
import com.jfinal.plugin.IPlugin;

/**
 * 实现作业的调度
 * 
 * @author jerri liu
 *
 */
public class QuartzPlugin implements IPlugin {
	private static Log logger = Log.getLog(QuartzPlugin.class);
	private SchedulerFactory sf = null;
	private Scheduler sched = null;

	private String config = "job.properties";
	private Properties properties;

	public QuartzPlugin(String config) {
		this.config = config;
	}

	public QuartzPlugin() {
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean start() {
		// 创建scheduler 实例工厂
		sf = new StdSchedulerFactory();
		try {
			// 从工厂里面拿到一个scheduler实例
			sched = sf.getScheduler();
		} catch (SchedulerException e) {
			new RuntimeException(e);
		}
		loadProperties();
		Enumeration enums = properties.keys();
		while (enums.hasMoreElements()) {
			String key = enums.nextElement() + "";
			if (!key.endsWith("job")) {
				continue;
			}
			String cronKey = key.substring(0, key.indexOf("job")) + "cron";
			String enable = key.substring(0, key.indexOf("job")) + "enable";
			if (isDisableJob(enable)) {
				continue;
			}
			String jobClassName = properties.get(key) + "";
			logger.info("jobClassName=" + jobClassName);
			String jobCronExp = properties.getProperty(cronKey) + "";
			Class clazz;
			try {
				clazz = Class.forName(jobClassName);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			// 真正执行的任务并不是Job接口的实例，而是用反射的方式实例化的一个JobDetail实例
			JobDetail job = newJob(clazz).withIdentity(jobClassName, jobClassName).build();
			// 定义一个触发器，startAt方法定义了任务应当开始的时间
			CronTrigger trigger = newTrigger().withIdentity(jobClassName, jobClassName)
					.withSchedule(cronSchedule(jobCronExp)).build();
			Date ft = null;
			try {
				// 将任务和Trigger放入scheduler
				ft = sched.scheduleJob(job, trigger);
				sched.start();
			} catch (SchedulerException ee) {
				new RuntimeException(ee);
			}
			logger.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: "
					+ trigger.getCronExpression());
		}
		return true;
	}

	private boolean isDisableJob(String enable) {
		return Boolean.valueOf(properties.get(enable) + "") == false;
	}

	private void loadProperties() {
		properties = new Properties();
		InputStream is = QuartzPlugin.class.getClassLoader().getResourceAsStream(config);
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean stop() {
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			logger.error("shutdown error", e);
			return false;
		}
		return true;

	}

	public static void main(String[] args) {
		QuartzPlugin plugin = new QuartzPlugin();
		plugin.start();
		logger.info("执行成功！！！");

	}
}
