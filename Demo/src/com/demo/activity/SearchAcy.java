package com.demo.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

import com.demo.R;
import com.demo.adapter.SearchsAdap;
import com.demo.service.ConnecMethod;

import org.jivesoftware.smackx.packet.VCard;

import java.util.ArrayList;

/**
 * 搜索
 * 
 * @author jin
 * 
 */
public class SearchAcy extends Activity {
	private String Tag = "MSH";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acy_search);
		initView();
	}

	private void initView() {
		ArrayList<String> users = null;
		// 判空
		if (null == (users = getIntent().getStringArrayListExtra("users"))) {
			this.finish();
		}

		ListView searlistview = (ListView) findViewById(R.id.lv_searchusers);
		SearchsAdap adap = new SearchsAdap(this, users);
		searlistview.setAdapter(adap);
		// 查看VCard
		searlistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String user = ((SearchsAdap.ViewHolder) conView.getTag()).fName
						.getText().toString();
				getVCard(user);
			}
		});
		// 发送消息 和加为好友
		searlistview.setOnItemLongClickListener(new OnItemLongClickListener() {
			private View cView = null;

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View conView,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				cView = conView;
				new AlertDialog.Builder(SearchAcy.this)
						.setTitle("陌生人")
						.setItems(new String[] { "打招呼", "加为好友" },
								new OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										// 打招呼
										if (0 == which) {

										}
										// 发送好友请求
										else {
											String user = ((SearchsAdap.ViewHolder) cView
													.getTag()).fName.getText()
													.toString();
											addItmeFriend(user);
										}
									}
								}).show();
				return false;
			}
		});
	}

	/**
	 * VCard
	 * 
	 * @param user
	 */
	private void getVCard(final String user) {
		new Thread() {
			public void run() {
				VCard card = ConnecMethod.getVCard(user + "@coboqo");
				// 可以获取更多详细的信息
				Log.d(Tag, "name:" + card.getTo());
			};
		}.start();
	}

	/**
	 * 添加好友
	 * 
	 * @param user
	 */
	private void addItmeFriend(final String user) {
		new Thread() {
			public void run() {
				ConnecMethod.addFriend(user);
			};
		}.start();
	}
}
