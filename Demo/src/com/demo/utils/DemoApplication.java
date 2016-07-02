package com.demo.utils;

import android.app.Application;

/**
 * TODO 配置,用于保存用户名和密码 全局变量中,
 * 
 * @author jin
 * 
 */
public class DemoApplication extends Application {
	/**
	 * 当前登录的用户名
	 */
	public static String UserName = "";
	/**
	 * 当前与你聊天的用户名
	 */
	public static String FromName = "";
}
