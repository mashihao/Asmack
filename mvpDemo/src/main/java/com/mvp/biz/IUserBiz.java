package com.mvp.biz;

/**
 * Created by sv-004 on 2016/7/2.
 */
public interface IUserBiz {
    public void login(String username,String passwrod,OnLoginListener loginListener);
}
