package com.niit.bd4project.service;

import com.niit.bd4project.pojo.Files;
import com.niit.bd4project.pojo.User;
import org.springframework.stereotype.Service;

import java.util.List;
//也是没有写对应的注解的，到时候可以改一下
@Service
public interface UserService {
    //和mapper相呼应，有一个对应的方法
    List<User> getUserList();

    User getUserByUsername(String username);

    void insertUser(User user);
    User getUserByToken(String token);

    public void uploadFile(String filename, String origin_filename, String filepath,
                           String hdfspath, String username, String time);

    Files getFilePath(String username, String origin_filename, String time);

    void deleteFile(String username, String origin_filename, String time);
}

