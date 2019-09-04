package com.github.msx.impl;

import com.github.msx.utils.TheConfig;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentService implements Runnable {


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

//        final List<Record> orders = Db.use(TheConfig.KITCHEN2_READ).find("SELECT c.unitName,a.unitid unitUuid,a.* from kitchen_order a " +
//                " LEFT JOIN schoolecology.xueange_student_info b on a.studentUuid=b.uuid " +
//                " LEFT JOIN schoolecology.basic_unit_info c on c.uuid =a.unitid " +
//                " WHERE b.uuid is null and a.payed=2");
        final List<Record> orders = Db.use(TheConfig.KITCHEN2_READ).find("SELECT * from kitchen_order a " +
                "WHERE studentUuid not IN(SELECT studentUuid from kitchen_food WHERE batchUuid='081fa001141c403ebad7edda45bf9435' ) and payed=2");
        System.out.println(orders.size());
        String batchUuid = "081fa001141c403ebad7edda45bf9435";
        String batchNum = "201909";
        Map<String, Record> map = new HashMap<String, Record>();
        int aa = 0;
        if (!orders.isEmpty()) {
            for (Record r : orders) {
                String stuId = r.getStr("studentUuid");//order里的学生id  旧的id
                String stuName = r.getStr("studentName");//order里的学生名
                String parId = r.get("buyerUuid");//家长id
                String orderId = r.get("uuid");
                if (r.getInt("payed") != 2 || map.containsKey(stuId)) {
                    System.out.println(stuId + "\n" + JsonKit.toJson(r) + "\n" + JsonKit.toJson(map.get(stuId)));
                    break;
                }
                map.put(stuId, r);
                Record food = Db.use(TheConfig.KITCHEN2_READ).findFirst("SELECT * from kitchen_food WHERE studentUuid ='" + stuId + "' and batchUuid='" + batchUuid + "'");
                if (food != null) {
                    continue;
                }
                Record stuInfo = Db.use(TheConfig.BASE_MYSQL_READ).findFirst("select * from xueange_student_info WHERE uuid='" + stuId + "'");
                int isMuslin = 0;
                if (stuInfo != null && stuInfo.get("remark") != null && stuInfo.getStr("remark").length() > 0) {
                    isMuslin = 1;
                }
                int a = Db.use(TheConfig.KITCHEN2_WRITE).update("INSERT INTO `kitchen`.`kitchen_food`" +
                        "( `uuid`, `batchUuid`, `batchNum`, `orderUuid`, `studentUuid`, `studentName`, `parentUuid`, `parentName`, `foodFlag`, `foodNum`," +
                        " `day01`, `day02`, `day03`, `day04`, `day05`, `day06`, `day07`, `day08`, `day09`, `day10`, `day11`, `day12`, `day13`, `day14`," +
                        " `createTime`, `updateTime`, `day15`, `day16`, `day17`, `day18`, `day19`, `day20`, `day21`, `day22`, `day23`, `day24`, `day25`, `day26`, `day27`, " +
                        "`day28`, `day29`, `day30`, `day31`, `personType`, `isMuslin`, `allergy`) " +
                        "VALUES ( '" + stuId + "', '" + batchUuid + "', '" + batchNum + "', '" + orderId + "', '" + stuId + "', '" + stuName + "', '" + parId + "', NULL, NULL, NULL," +
                        " 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, '2019-08-30 16:52:09', '2019-08-30 16:52:09'," +
                        " 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, " + isMuslin + ", '')");
                aa += a;
            }

            System.out.println(aa + " :: " + map.keySet().size());
        }


//        if (!orders.isEmpty()) {
//            for (Record r : orders) {
//                String oldStuId = r.getStr("studentUuid");//order里的学生id  旧的id
//                String stuName = r.getStr("studentName");//order里的学生名
//                String parId = r.get("buyerUuid");//家长id
//                String mobile = r.get("buyerMobile");//家长手机号
//                String untiId = r.get("unitid");//学校id
//                //查出家长的信息
//                Record parInfo = Db.use(TheConfig.BASE_MYSQL_READ).findFirst("SELECT * from xueange_patriarch_info WHERE uuid='"+parId+"' and mobile='"+mobile+"' limit 1");
//                System.out.println("parInfo ::" + parInfo);
//                if(parInfo==null) break;
//                //查出旧学生的关系
//                Record parAndStu = Db.use(TheConfig.BASE_MYSQL_READ).findFirst("SELECT * from schoolecology.xueange_patriarch_student WHERE studentUuid='"+oldStuId+"' and patriarchUuid='"+parId+"' limit 1");
//                System.out.println("parAndStu ::" + parAndStu);
//                if(parAndStu==null) break;
//
//                Record newStudent = Db.use(TheConfig.BASE_MYSQL_READ).findFirst("SELECT a.* from schoolecology.xueange_student_info a " +
//                        " LEFT JOIN schoolecology.xueange_patriarch_student b on b.studentUuid=a.uuid " +
//                        " WHERE b.patriarchUuid='"+parId+"' and a.studentName='"+stuName+"' and a.unitId='"+untiId+"' limit 1");
//                final String newStuId=newStudent.getStr("uuid");//新学生的uuid
//                //查出新学生的关系记录 置为 -1 删除
//                Record newParAndStu = Db.use(TheConfig.BASE_MYSQL_READ).findFirst("SELECT * from schoolecology.xueange_patriarch_student WHERE studentUuid='"+newStuId+"' and patriarchUuid='"+parId+"' limit 1");
//
//
//
//
//
////                Db.use(TheConfig.BASE_MYSQL_WRITE).update("INSERT INTO `schoolEcology2`.`xueange_student_info`" +
////                        "(`uuid`, `studentCode`, `studentName`, `userId`, `unitId`, `unitName`, " +
////                        "`gradeId`, `gradeName`, `classId`, `className`, `createTime`, " +
////                        "`status`, `sex`, `birthday`, `height`, `weight`, `img`, `parentName`, " +
////                        "`parentPhone`, `section`, `inSchoolYear`, `allergy`, `remark`, `isVIP`) VALUES " +
////                        "( '"+oldStuId+"', '201810005', '小赵亚洲', NULL, 'ba553b0b85314414a9b4b196d01a9238', '邯郸市阳光厨房第一小学', NULL, NULL, '813d2d0956444ca189421af18b3e1e07', '2018级1班', '2019-08-13 09:26:59', '1', '男', NULL, NULL, NULL, NULL, '', '', '小学', '2018', NULL, NULL, '0');")
//
//            }
//        }

    }
}
