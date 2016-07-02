package com.demo.activity;

import com.demo.R;
import com.demo.service.ConnecMethod;
import com.demo.utils.DemoApplication;
import com.demo.utils.XmppTool;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

/**
 * 主界面
 * 
 * @author jin
 * 
 */
public class MainAcy extends Activity implements OnClickListener {
	private String Tag = "MSH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acy_main);
		initView();
	}

	private void initView() {
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.btn_regis).setOnClickListener(this);
	}

	private Handler insHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			// 登录成功
			case 0:
				Log.d(Tag, "login suc");
				Intent intent = new Intent(MainAcy.this, FriendsAcy.class);
				startActivity(intent);
				break;
			// 登录失败
			case 1:
				Log.d(Tag, "login fail");
				break;
			// 注册成功
			case 2:
				Log.d(Tag, "reg suc");
				break;
			// 注册成功
			case 3:
				Log.d(Tag, "reg fail");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 账户登录
	 */
	private void accountLogin() {
		new Thread() {
			public void run() {
				String account = ((EditText) findViewById(R.id.et_name))
						.getText().toString();
				String pwd = ((EditText) findViewById(R.id.et_pwd)).getText()
						.toString();
				boolean is = ConnecMethod.login(account, pwd);
				if (is) {
					insHandler.sendEmptyMessage(0);
					// 将用户名保存
					DemoApplication.UserName = account;
				} else {
					insHandler.sendEmptyMessage(1);
				}
			};
		}.start();
	}

	/**
	 * 账户注册
	 */
	private void accountRegis() {
		new Thread() {
			public void run() {
				String account = ((EditText) findViewById(R.id.et_name))
						.getText().toString();
				String pwd = ((EditText) findViewById(R.id.et_pwd)).getText()
						.toString();
				String is = ConnecMethod.regist(account, pwd);
				if ("注册成功".equals(is)) {
					insHandler.sendEmptyMessage(2);
				} else {
					insHandler.sendEmptyMessage(3);
				}
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_login:
			accountLogin();
			break;
		case R.id.btn_regis:
			accountRegis();
			break;
		default:
			break;
		}
	}
}
