package com.demo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.demo.R;
import com.demo.adapter.FriendsAdap;
import com.demo.service.ConnecMethod;
import com.demo.service.ConnecService;
import com.demo.utils.DemoApplication;
import com.demo.utils.XmppTool;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有朋友
 * 
 * @author jin
 * 
 */
public class FriendsAcy extends Activity implements OnClickListener {
	private String Tag = "MSH";
	private List<RosterEntry> rosterEntries = new ArrayList<RosterEntry>();
	private FriendsAdap adap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acy_friends);
		initView();
	}

	private Handler insHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				adap.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};

	private void initView() {
		getOffLine();
		// 开启服务 监听服务器,接收离线消息和当前的消息
		Intent intent = new Intent(this, ConnecService.class);
		startService(intent);

		findViewById(R.id.btn_search).setOnClickListener(this);
		findViewById(R.id.btn_newgroup).setOnClickListener(this);

		ListView frilistview = (ListView) findViewById(R.id.lv_friends);
		adap = new FriendsAdap(this, rosterEntries);
		frilistview.setAdapter(adap);
		frilistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				DemoApplication.FromName = ((FriendsAdap.ViewHolder) conView
						.getTag()).fName.getText().toString();
				Log.d(Tag, arg2 + ";" + DemoApplication.FromName);

				Intent intent = new Intent(FriendsAcy.this, TalkAcy.class);
				startActivity(intent);
			}
		});
		getFriends();
	}

	/**
	 * 搜索用户
	 */
	private void searchUser() {
		new Thread() {
			public void run() {
				String usr = ((EditText) findViewById(R.id.et_searchname))
						.getText().toString();
				ArrayList<String> users = ConnecMethod.searchUsers(usr);

				// 跳转页面
				Intent intent = new Intent(FriendsAcy.this, SearchAcy.class);
				intent.putStringArrayListExtra("users", users);
				startActivity(intent);
			};
		}.start();
	}

	/**
	 * 获取所有好友的信息
	 */
	private void getFriends() {
		new Thread() {
			public void run() {
				rosterEntries.clear();
				// 获取所有好友，不分组
				// for (RosterEntry enty : ConnecMethod.searchAllFriends()) {
				// rosterEntries.add(enty);
				// }

				// 获取所有好友，分组
				for (RosterGroup group : ConnecMethod.searchAllGroup()) {
					Log.d(Tag, group.getName() + "");
					for (RosterEntry enty : ConnecMethod.getGroupFriends(group
							.getName())) {
						enty.setName(group.getName());
						rosterEntries.add(enty);
					}
				}
				insHandler.sendEmptyMessage(0);
			};
		}.start();
	}

	/**
	 * 创建新的组好友
	 */
	private void createNewGroup() {
		new Thread() {
			public void run() {
				String group = ((EditText) findViewById(R.id.et_searchname))
						.getText().toString();
				String friend = ((EditText) findViewById(R.id.et_groupname))
						.getText().toString();
				ConnecMethod.addGroupFriend(group, friend);
			};
		}.start();
	}

	/**
	 * 离线消息
	 */
	private void getOffLine() {
		new Thread() {
			public void run() {
				List<org.jivesoftware.smack.packet.Message> msglist = ConnecMethod
						.getOffLine();
				for (org.jivesoftware.smack.packet.Message msg : msglist) {
					Log.d(Tag, msg.getTo()+"         " + msg.getBody() +"         "+ msg.getFrom());
				}
			};
		}.start();
	}

	/**
	 * 在退出的时候注销
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			XmppTool.getConnection().getAccountManager().deleteAccount();
		} catch (XMPPException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_search:
			searchUser();
			break;
		case R.id.btn_newgroup:
			createNewGroup();
			break;
		default:
			break;
		}
	}
}
