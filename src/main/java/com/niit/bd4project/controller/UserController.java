package com.niit.bd4project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.niit.bd4project.pojo.User;
import com.niit.bd4project.service.impl.UserServiceImpl;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
public class UserController {

    Logger logger = Logger.getLogger("UserController");

    @Autowired
    private UserServiceImpl userServer;

    @RequestMapping("/getuserlist")
    public List<User> getUserList()
    {
      return userServer.getUserList();
    }

    @RequestMapping("/login")
    public Object getUserByUsername(String username,String password)
    {
        try {
                User user = userServer.getUserByUsername(username);
                if(!user.getPassword().equals(password))
                {
                    logger.warning("错误，密码错误");
                    return "密码错误";
                }
                logger.info(user.toString());
              return user.getToken();
        }catch (Exception e)
        {
            logger.warning("错误，没有找到该用户");
            return "Error";
        }

    }

    @RequestMapping("/register")
    public String insertUser(User user)
    {

        // 判断是否有该用户
        if(userServer.getUserByUsername(user.getUsername())!=null)
        {
            logger.warning("错误，已经存在该用户");
            return "已经存在该用户";
        }
        // 发放登录令牌
        user.setToken(UUID.randomUUID().toString());
        logger.info(user.toString());
        userServer.insertUser(user);
        logger.info("成功插入用户");
        User getuser = userServer.getUserByUsername(user.getUsername());
        logger.info(getuser.toString());
        return "注册成功";
    }

    @RequestMapping("/checktoken")
    public String checkToken(String token)
    {
        User user = userServer.getUserByToken(token);
        if(user==null)
        {
            logger.warning("错误，没有找到该用户");
            return "Error";
        }
        logger.info(user.toString());
        return "Success";
    }

    // 获取token
    @RequestMapping("/gettoken")
    public String getToken(String username,String password)
    {
        try {
                User user = userServer.getUserByUsername(username);
                if(!user.getPassword().equals(password))
                {
                    logger.warning("错误，密码错误");
                    return "密码错误";
                }
                logger.info(user.toString());
              return user.getToken();
        }catch (Exception e)
        {
            logger.warning("错误，没有找到该用户");
            return "Error";
        }

    }

}