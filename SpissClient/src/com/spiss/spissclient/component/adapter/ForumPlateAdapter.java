package com.spiss.spissclient.component.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ForumPlateAdapter extends MyListAdapter<Map<String,Object>>{

	public ForumPlateAdapter(Context context, List<Map<String, Object>> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ItemHolder holder = null;
		
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
			holder = new ItemHolder();
			convertView.setTag(holder);
		}else{
			holder = (ItemHolder)convertView.getTag();
		}
		
		holder.text = (TextView)convertView.findViewById(android.R.id.text1);
		holder.text.setText(getItem(position).get("plate_name").toString());
		
		return convertView;
	}
	class ItemHolder{
		TextView text;
	}

}
