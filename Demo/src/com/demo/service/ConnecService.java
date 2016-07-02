package com.demo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 服务，主要是用来进行与服务器进行的交互
 * 
 * @author jin
 * 
 */
public class ConnecService extends Service {
	private String Tag = "MSH";

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 启动的时候 监听传送的消息
	 */
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		getOnLineMsg();
		getOnLineFile();
	}

	/**
	 * 文本
	 */
	private void getOnLineMsg() {
		ConnecMethod.getOnLine(this);
	}

	/**
	 * 文件
	 */
	private void getOnLineFile() {
		ConnecMethod.getOnLineFile(this);
	}
}
