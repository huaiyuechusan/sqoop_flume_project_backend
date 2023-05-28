package com.niit.bd4project.pojo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;


@Component
@ConfigurationProperties(prefix = "shell")
public class Shell {

    String host;
    int port;
    String username;
    String password;
    Set<String> command;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getCommand() {
        return command;
    }

    public void setCommand(Set<String> command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "Shell{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", command=" + command +
                '}';
    }
}
