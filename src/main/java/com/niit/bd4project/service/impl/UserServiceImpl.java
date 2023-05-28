package com.niit.bd4project.service.impl;

import com.niit.bd4project.mapper.UserMapper;
import com.niit.bd4project.pojo.Files;
import com.niit.bd4project.pojo.User;
import com.niit.bd4project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
//再网上看到 好像是 service和controller和compent 还有这个 都是没有本质区别的
@Repository
public class UserServiceImpl implements UserService {
    //这里就是需要把这个实现方法具体的写出来了,然后这里就需要加入注解了
    //这就是所谓的自动装配的功能吧
    @Autowired
    private UserMapper userMapper;
    @Override
    public List<User> getUserList() {
        try{
            List<User> users= userMapper.getUserList();
            return users;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public User getUserByUsername(String username) {
        try{
            User user= userMapper.getUserByUsername(username);
            return user;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void insertUser(User user) {
        try{
            userMapper.insertUser(user);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public User getUserByToken(String token) {
        try{
            User user= userMapper.getUserByToken(token);
            return user;

        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void uploadFile(String filename, String origin_filename, String filepath,
                           String hdfspath, String username, String time){
        try{
            userMapper.uploadFile(filename, origin_filename, filepath, hdfspath, username, time);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public Files getFilePath(String username, String origin_filename, String time){
        try{
            Files filepath = userMapper.getFilePath(username, origin_filename, time);
            return  filepath;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void deleteFile(String username, String origin_filename, String time){
        try{
            userMapper.deleteFile(username, origin_filename, time);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
