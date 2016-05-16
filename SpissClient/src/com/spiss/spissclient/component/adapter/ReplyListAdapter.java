package com.spiss.spissclient.component.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.spiss.spissclient.R;
import com.spiss.spissclient.utils.MyTimeFormat;

public class ReplyListAdapter extends MyListAdapter<Map<String,Object>>{

	public ReplyListAdapter(Context context, List<Map<String, Object>> list) {
		super(context, list);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Holder holder = null;
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.reply_item, null);
			holder = new Holder();
			convertView.setTag(holder);
		}else{
			holder = (Holder) convertView.getTag();
		}
		
		holder.nameText = (TextView)convertView.findViewById(R.id.nameText);
		holder.timeText = (TextView)convertView.findViewById(R.id.dateText);
		holder.contentText = (TextView)convertView.findViewById(R.id.content);
		Map<String,Object> map = getItem(position);
		holder.nameText.setText(map.get("user_name").toString());
		holder.timeText.setText(MyTimeFormat.noYTimeFormat(map.get("replyDate").toString()));
		holder.contentText.setText(map.get("replyContent").toString());
		
		holder.nameText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Toast.makeText(context, "待完善", Toast.LENGTH_SHORT).show();

			}
		});
		
		
		return convertView;
	}
	
	class Holder{
		TextView nameText;
		TextView contentText;
		TextView timeText;
	}

}
