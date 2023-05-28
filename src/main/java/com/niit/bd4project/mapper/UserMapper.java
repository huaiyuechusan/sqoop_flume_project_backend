package com.niit.bd4project.mapper;

import com.niit.bd4project.pojo.Files;
import com.niit.bd4project.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
public interface UserMapper {
    //然后做一些数据库的对应操作，这次是查询操作
    @Select("select * from users order by id")
    List<User> getUserList();

    @Select("select * from users where username=#{username}")
    User getUserByUsername(String username);

    @Insert("insert into users(username,password,token) values(#{username},#{password}, #{token})")
    void insertUser(User user);

    // 校验token
    @Select("select * from users where token=#{token}")
    User getUserByToken(String token);

    // 插入用户上传文件地址
    @Insert("insert into files(filename, origin_filename, filepath, hdfspath, username, time) values(#{filename},#{origin_filename}, #{filepath}, #{hdfspath}, #{username}, #{time})")
    void uploadFile(String filename, String origin_filename, String filepath, String hdfspath, String username, String time);

    // 通过用户名和原始文件名获取文件的路径
    @Select("select * from files where username = #{username} and origin_filename = #{origin_filename} and time = #{time}")
    Files getFilePath(String username, String origin_filename, String time);

    // 通过用户名和原始文件名删除数据库数据
    @Select("delete from files where username = #{username} and origin_filename = #{origin_filename} and time = #{time}")
    void deleteFile(String username, String origin_filename, String time);

}
