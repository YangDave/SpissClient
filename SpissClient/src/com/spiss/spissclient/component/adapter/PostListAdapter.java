package com.spiss.spissclient.component.adapter;

import java.util.List;
import java.util.Map;

import com.spiss.spissclient.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PostListAdapter extends MyListAdapter<Map<String,Object>>{

	public PostListAdapter(Context context, List<Map<String, Object>> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemViewHolder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.post_item, null);
			holder = new ItemViewHolder();
			convertView.setTag(holder);
		}else{
			holder = (ItemViewHolder) convertView.getTag();
		}

		holder.title = (TextView)convertView.findViewById(R.id.title);
		holder.date = (TextView)convertView.findViewById(R.id.date);
		Map<String,Object> map = getItem(position);
		System.out.println(map);
		System.out.println(map.get("title").toString());
		System.out.println("holder.title"+holder.title);
		holder.title.setText(map.get("title").toString());
		String date;
		date = map.get("lastReplyDate").toString();
		holder.date.setText(date);

		return convertView;
	}


	class ItemViewHolder{
		TextView title;
		TextView date;
	}

}
