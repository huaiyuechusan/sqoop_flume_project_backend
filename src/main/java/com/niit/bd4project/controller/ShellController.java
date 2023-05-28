package com.niit.bd4project.controller;

import com.niit.bd4project.pojo.Shell;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;


@Import(Shell.class)
@RestController
public class ShellController {


    java.util.logging.Logger logger = Logger.getLogger("ShellController");

    @Autowired
    private Shell shell;
    @RequestMapping("/shell")
    public String shellexec(int n) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(shell.getUsername(), shell.getHost(), shell.getPort());
            session.setPassword(shell.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(shell.getCommand().toArray()[n].toString());
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            reader.close();
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "success";
    }

    @RequestMapping("/shellparams")
    public String shellexec(int n, String params) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(shell.getUsername(), shell.getHost(), shell.getPort());
            session.setPassword(shell.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            String command = shell.getCommand().toArray()[n].toString() + params;
            logger.info("已获取到指令");
            logger.info(command);
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            reader.close();
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return "error:+" + e.getMessage();
        }
        return "success";
    }

    @RequestMapping("/command")
    public String shellexec(String command){
                try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(shell.getUsername(), shell.getHost(), shell.getPort());
            session.setPassword(shell.getPassword());
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            logger.info("已获取到指令");
            logger.info(command);
            channel.setCommand(command);
            channel.setInputStream(null);
            channel.setErrStream(System.err);

            InputStream in = channel.getInputStream();
            channel.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
            reader.close();
            channel.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            return "error:+" + e.getMessage();
        }
        return "success";
    }

}
