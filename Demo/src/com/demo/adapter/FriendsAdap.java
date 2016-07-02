package com.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.R;

import org.jivesoftware.smack.RosterEntry;

import java.util.List;

/**
 * 朋友
 */

/**
 * TODO 
 * @author msh
 * @time 2016/6/30 20:38
*/
public class FriendsAdap extends BaseAdapter {
	private List<RosterEntry> list;
	private Context context;

	public FriendsAdap(Context context, List<RosterEntry> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View conView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holdr;
		if (conView == null) {
			conView = LayoutInflater.from(context).inflate(
					R.layout.line_friend, null);
			holdr = new ViewHolder();
			holdr.fName = (TextView) conView.findViewById(R.id.tv_fname);
			conView.setTag(holdr);
		} else {
			holdr = (ViewHolder) conView.getTag();
		}

		RosterEntry rose = list.get(arg0);
		// 获取的jid
		holdr.fName.setText(list.get(arg0).getUser() + ";"
				+ list.get(arg0).getName());
		return conView;
	}

	public static class ViewHolder {
		public TextView fName;
	}
}
