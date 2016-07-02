package com.mvp.bean;

/**
 * Created by sv-004 on 2016/7/2.
 */
/**
 * TODO 实体类
 * @author msh
 * @time 2016/7/2 10:09
*/
public class User {
    private String username;
    private String password;

    public User() {
    }

    public User(String password, String username) {
        this.password = password;
        this.username = username;
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
}
