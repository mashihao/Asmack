package com.mvp.biz;

import com.mvp.bean.User;

/**
 * Created by sv-004 on 2016/7/2.
 */
public interface OnLoginListener {
    void loginSuccess(User user);
    void loginFailed();
}
