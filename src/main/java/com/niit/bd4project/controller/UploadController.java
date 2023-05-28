package com.niit.bd4project.controller;

import com.niit.bd4project.pojo.Files;
import com.niit.bd4project.pojo.Shell;
import com.niit.bd4project.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import com.niit.bd4project.pojo.HDFS;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@Import(HDFS.class)
public class UploadController {
    SimpleDateFormat sdf=new SimpleDateFormat("/yyyy-MM-dd/");
    Logger logger = LogManager.getLogger(UploadController.class);

    @Autowired
    private HDFS hdfs;

    private final ShellController shellController;

    @Autowired
    public UploadController(ShellController shellController) {
        this.shellController = shellController;
    }


    @Autowired
    private UserServiceImpl userServer;

    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest request, String username,String time){
        logger.info("username:"+username);
        String format =  sdf.format(new Date());
        File directory = new File("");
        String realPath = directory.getAbsolutePath()+"/upload/" + username+format;
        File folder = new File(realPath);
        //如果文件夹已经不存在那么我们就创建一下
        if(!folder.exists()){
            folder.mkdirs();
        }
        logger.info(file);
        String originalFilename = file.getOriginalFilename();
        String newName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
         logger.info(hdfs.getUrl());
         logger.info(hdfs.getLocation());
        try {
            file.transferTo(new File(folder,newName));
            String filepath = realPath+newName;
            logger.info("上传成功,路径为"+filepath);
            Configuration conf = new Configuration();
            conf.set("fs.defaultFS",hdfs.getUrl());
            //通过如下的方式进行客户端身份的设置
            System.setProperty("HADOOP_USER_NAME",hdfs.getUsername());
            //通过FileSystem的静态方法获取文件系统客户端对象
            FileSystem fs = FileSystem.get(conf);
            // 查看是否有hdfs路径，如果没有则进行创建
            if (!fs.exists(new Path(hdfs.getLocation()))) {
                fs.create(new Path(hdfs.getLocation()));
            }
            fs.copyFromLocalFile(new Path(filepath),new Path(hdfs.getLocation()+newName));
            // 获取当前的时间戳
            // String time = String.valueOf(System.currentTimeMillis());
            // 添加到数据库
            userServer.uploadFile(newName, file.getOriginalFilename(), realPath+newName, hdfs.getLocation()+newName, username, time);
            // 输出文件名字
            return "上传成功"+file.getOriginalFilename();
        } catch (Exception e) {
            logger.error("上传失败:"+e.getMessage());
        }
        return "上传失败";
    }

    @PostMapping("/delete")
    public String delete(String username,String filename,String time){
        try{
            // 获取文件路径
            Files fileinfo = userServer.getFilePath(username, filename, time);
            logger.info("----------已获取到文件路径----------");
            logger.info("本地路径为："+fileinfo.getFilepath());
            logger.info("HDFS路径为："+fileinfo.getHdfspath());
            logger.info("----------正在删除该文件----------");
            logger.info("删除本地上的文件："+shellController.shellexec(1, fileinfo.getFilepath()));
            logger.info("删除HDFS上的文件："+shellController.shellexec(2, fileinfo.getHdfspath()));
            logger.info("----------正在删除该文件的数据库数据----------");
            userServer.deleteFile(username, filename, time);
            logger.info("----------删除完毕----------");
        }catch (Exception e){
            logger.error(e);
            return "False";
        }
        return "True";
    }


}
