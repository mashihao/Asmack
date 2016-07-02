package com.mvp.view;

/**
 * Created by sv-004 on 2016/7/2.
 */
public interface IUserLoginView {
    String getUserName();
    String getPassWord();
    void clearUserName();
    void clearPassWrod();
    void showLoading();
    void hideLoading();
    void toMainActivity();
    void showFailedError();
}
