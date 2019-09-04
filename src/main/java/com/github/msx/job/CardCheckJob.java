package com.github.msx.job;

import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.kit.JsonKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import com.github.msx.utils.TheConfig;

public class CardCheckJob implements Job {

	private static Log log = Log.getLog(CardCheckJob.class);

	// private ILocationTrackService locationTrackService = new
	// LocationTrackServiceImpl();
	// private ILocationTrackService locationTrackService;

	private static boolean isRunning = false;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		if (isRunning) {
			log.info("cardcheckJob is running! ......");
			return;
		}
		try {//
			isRunning = true;
			String today = "2019-06-12"; // TODO
//			String today = DateTimeUtil.getDate();
			Db.use(TheConfig.BASE_MYSQL_WRITE)
					.update("delete from xueange_card_exception_flow where errorDate='" + today + "'");
			List<Record> units = Db.use(TheConfig.BASE_MYSQL_READ).find(
					"SELECT DISTINCT a.uuid,a.unitName from basic_unit_info a LEFT JOIN iot_terminal_info b on b.unitId=a.uuid WHERE b.uuid is not null ");
			if (!units.isEmpty())// 遍历所有有设备的学校
				for (Record unit : units) {
					String unitId = unit.getStr("uuid");
					String unitName = unit.getStr("unitName");
					List<Record> students = Db.use(TheConfig.BASE_MYSQL_READ)
							.find("SELECT a.uuid,c.createTime bindTime,c.cardId,a.studentName,a.className "
									+ " from xueange_student_info a "
									+ " left JOIN xueange_student_card c on c.studentId=a.uuid  WHERE a.unitId='"
									+ unitId + "' and c.cardId is not null  ORDER BY a.className,a.studentName asc");
					if (!students.isEmpty())// 遍历学校所有绑卡的学生
						for (Record someone : students) {
							try {
								String studentId = someone.getStr("uuid");
								String stuName = someone.getStr("studentName");
								String cardId = someone.getStr("cardId");
								String className = someone.getStr("className");
								String bindTime = someone.get("bindTime").toString();
								List<Record> dutys = Db.use(TheConfig.BASE_MYSQL_READ)
										.find("SELECT * from duty_flowdata WHERE studentId='" + studentId
												+ "' and arriveTime like '" + today + "%' and comment is null group by studentId,arriveTime order by arriveTime ");
								Record eRecord = new Record().set("error1", "0").set("error2", "0").set("error3", "0")
										.set("errorDate", today).set("unitId", unitId).set("unitName", unitName)
										.set("className", className).set("studentId", studentId)
										.set("studentName", stuName).set("cardId", cardId).set("bindTime", bindTime);
								if (dutys.isEmpty()) {// 无进出校记录 异常
									// xueange_card_exception_flow
									eRecord.set("error1", "1");
								} else {// 判断是否有 连续进校记录 或 连续出校记录
									String lastduty = "";
									String lastdutyTime = "";
									for (int i = 0; i < dutys.size(); i++) {
										Record d = dutys.get(i);
										if (i == 0) {
											lastduty = d.get("accessType").toString();
											lastdutyTime = d.get("arriveTime").toString();
										} else {
											String thisduty = d.get("accessType").toString();
											String thisdutyTime = d.get("arriveTime").toString();
											if (lastduty.equals(thisduty) && !lastdutyTime.equals(thisdutyTime)) {
												eRecord.set(lastduty.equals("1") ? "error2" : "error3", "1");
											}
											lastduty = thisduty;
											lastdutyTime = d.get("arriveTime").toString();
										}
									}
								}
								log.info(JsonKit.toJson(eRecord));
								// System.out.println(JsonKit.toJson(eRecord));
								if (!eRecord.getStr("error1").equals("0") || !eRecord.getStr("error2").equals("0")
										|| !eRecord.getStr("error3").equals("0"))
									Db.use(TheConfig.BASE_MYSQL_WRITE).save("xueange_card_exception_flow", eRecord);
							} catch (Exception e) {
								e.printStackTrace();
								log.error("cardcheckJob each student error!\n" + JsonKit.toJson(someone));
							}
						}
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isRunning = false;
		}
	}

}
