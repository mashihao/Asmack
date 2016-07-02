package com.mvp.biz;

import com.mvp.bean.User;

/**
 * Created by sv-004 on 2016/7/2.
 */

/**
 * TODO 业务类
 * TODO User的业务逻辑模拟耗时登录
 * @author msh
 * @time 2016/7/2 9:51
*/
public class UserBiz implements IUserBiz {

    //登录属于耗时操作,需要放到子线程当中去
    @Override
    public void login(final String username, final String password, final OnLoginListener loginListener) {


        new Thread(){
            @Override
            public void run() {
                try
                {
                    Thread.sleep(2000);

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if ("mashihao".equals(username)&&"mashihao".equals(password))
                {
                    User user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    loginListener.loginSuccess(user);
                }
                else
                {
                    loginListener.loginFailed();
                }
            }
        }.start();
    }
}
