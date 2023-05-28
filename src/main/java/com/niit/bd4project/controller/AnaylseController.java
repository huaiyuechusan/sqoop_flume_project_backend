package com.niit.bd4project.controller;

import com.niit.bd4project.pojo.Files;
import com.niit.bd4project.pojo.Hive;
import com.niit.bd4project.service.impl.UserServiceImpl;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jooq.Result;
import org.jooq.Record;
import org.jooq.DSLContext;
import org.jooq.JSONFormat;
import com.google.gson.Gson;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
@Import(Hive.class)
public class AnaylseController {
Logger logger = Logger.getLogger("AnaylseController");

    @Autowired
    private Hive hive;

    @Autowired
    private UserServiceImpl userServer;

    @RequestMapping("/execute")
    public Object execute(String sql){
        try {
         BasicConfigurator.configure();//开启日志
        //加载hive驱动
        Class.forName(hive.getDriver());
        //连接hive数据库
        Connection conn = DriverManager.getConnection(
            hive.getUrl(), hive.getUser(), hive.getPassword());
        DSLContext create = DSL.using(conn, SQLDialect.DEFAULT);
        Result<Record> result = create.fetch(sql);
        logger.info("获取到结果如下:\n"+result);
        String json = result.formatJSON(new JSONFormat().recordFormat(JSONFormat.RecordFormat.OBJECT));
        // 关闭资源
        conn.close();
        // 返回JSON字符串
        return json;
        }
        catch (Exception e)
        {
            logger.error("错误，"+e.getMessage());
            return "Error";
        }
    }

    // 通过表单数据来建立hive表
    @PostMapping("/insert")
    public String insert(String tablename,String filename, String username, String time){
        try {
            BasicConfigurator.configure();//开启日志
            //加载hive驱动
            Class.forName(hive.getDriver());
            //连接hive数据库
            Connection conn = DriverManager.getConnection(
                    hive.getUrl(), hive.getUser(), hive.getPassword());
            DSLContext create = DSL.using(conn, SQLDialect.DEFAULT);
            // create.execute(tablename);
            // 获取文件路径
            Files fileinfo = userServer.getFilePath(username, filename, time);
            logger.info(username);
            logger.info(filename);
            logger.info(time);
            logger.info(fileinfo.getHdfspath());
            // fileinfo.getHdfspath()
            String hiveinsert = "LOAD DATA INPATH "+"'"+fileinfo.getHdfspath()+"'"+" INTO TABLE "+tablename+"\n";
                    //  +
                    // "ROW FORMAT DELIMITED\n" +
                    // "FIELDS TERMINATED BY ','\n" +
                    // "LINES TERMINATED BY '\n'";
            create.execute(hiveinsert);
            logger.info("执行成功");
            // 关闭资源
            conn.close();
            // 返回JSON字符串
            return "success";
        }
        catch (Exception e)
        {
            logger.error("错误，"+e.getMessage());
            return "Error";
        }
    }


}
