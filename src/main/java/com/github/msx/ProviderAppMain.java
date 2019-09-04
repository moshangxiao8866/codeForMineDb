package com.github.msx;

import com.github.msx.impl.TestService;
import com.github.msx.utils.TheConfig;

import com.alibaba.druid.util.JdbcConstants;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.spring.SpringPlugin;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.plugin.redis.RedisPlugin;
import com.jfinal.plugin.redis.serializer.JdkSerializer;

public class ProviderAppMain {

    public static void main(String[] args) throws InterruptedException {
        // redis 插件
//		RedisPlugin redis = new RedisPlugin(PropKit.use("sysConfig.properties").get("redis.name"),
//				PropKit.use("sysConfig.properties").get("redis.host"),
//				PropKit.use("sysConfig.properties").getInt("redis.port"),
//				PropKit.use("sysConfig.properties").getInt("redis.timeout"),
//				PropKit.use("sysConfig.properties").get("redis.password"));
//		// 采用jdk序列化
//		redis.setSerializer(new JdkSerializer());
//		redis.start();

        // 读取配置文件
        Prop p = new Prop("db.properties");
        // 读库操作连接池配置
        // 配置Druid数据库连接池插件
        DruidPlugin dp_mysql_read = new DruidPlugin(p.get("mysql_base_read.url"), p.get("mysql_base_read.username"),
                p.get("mysql_base_read.password").trim());

        DruidPlugin kitchen_read = new DruidPlugin(p.get("kitchenread.url"), p.get("kitchenread.username"),
                p.get("kitchenread.password").trim());
        WallFilter wall = new WallFilter();
        wall.setDbType(JdbcConstants.MYSQL);
        dp_mysql_read.addFilter(wall);
        kitchen_read.addFilter(wall);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp = new ActiveRecordPlugin(TheConfig.BASE_MYSQL_READ, dp_mysql_read);
        arp.setDialect(new MysqlDialect());
        arp.setShowSql(p.getBoolean("devMode", false));
        arp.setDevMode(p.getBoolean("devMode", false));

        ActiveRecordPlugin arp111 = new ActiveRecordPlugin(TheConfig.KITCHEN2_READ, kitchen_read);
        arp111.setDialect(new MysqlDialect());
        arp111.setShowSql(p.getBoolean("devMode", false));
        arp111.setDevMode(p.getBoolean("devMode", false));

        // 写库操作连接池配置
        DruidPlugin dp_mysql_write = new DruidPlugin(p.get("mysql_base_write.url"), p.get("mysql_base_write.username"),
                p.get("mysql_base_write.password").trim());

        DruidPlugin kitchen_write = new DruidPlugin(p.get("kitchen.url"), p.get("kitchen.username"),
                p.get("kitchen.password").trim());

        dp_mysql_write.addFilter(wall);
        kitchen_write.addFilter(wall);

        // 配置ActiveRecord插件
        ActiveRecordPlugin arp_write = new ActiveRecordPlugin(TheConfig.BASE_MYSQL_WRITE, dp_mysql_write);
        arp_write.setDialect(new MysqlDialect());
        arp_write.setShowSql(p.getBoolean("devMode", false));
        arp_write.setDevMode(p.getBoolean("devMode", false));

        ActiveRecordPlugin arp_kitchen_write = new ActiveRecordPlugin(TheConfig.KITCHEN2_WRITE, kitchen_write);
        arp_kitchen_write.setDialect(new MysqlDialect());
        arp_kitchen_write.setShowSql(p.getBoolean("devMode", false));
        arp_kitchen_write.setDevMode(p.getBoolean("devMode", false));


        // 手动启动各插件
        dp_mysql_read.start();
        arp.start();
        kitchen_read.start();
        arp111.start();

        // dp_mysql_readEcology.start();
        // arpEcology.start();

        dp_mysql_write.start();
        arp_write.start();
        kitchen_write.start();
        arp_kitchen_write.start();

// 配置Spring插件
//		SpringPlugin sp = new SpringPlugin("classpath*:applicationContext.xml");
//		sp.start();


        // 定时任务插件
//        new QuartzPlugin().start();

        System.out.println("Code for my Db 启动完成。");

        // 没有这一句，启动到这服务就退出了
        //Thread.currentThread().join();
//		new StudentService().run();
		new TestService().run();

    }

}
