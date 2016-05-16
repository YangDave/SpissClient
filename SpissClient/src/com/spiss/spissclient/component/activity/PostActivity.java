package com.spiss.spissclient.component.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.spiss.spissclient.R;
import com.spiss.spissclient.application.ExitApplication;
import com.spiss.spissclient.base.MyActivity;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.component.adapter.ReplyListAdapter;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.utils.MyJSONUtils;

public class PostActivity extends MyActivity{

	Map<String,Object> postMap;
	TextView titleText,nameText;
	TextView contentText,timeText;
	ListView replyListView;
	ReplyListAdapter adapter;
	int page = 1;
	final static String SIZE = "3";
	boolean isWorking = false;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.post_activity);

		findView();

		Intent intent = getIntent();
		postMap = (Map<String, Object>) intent.getSerializableExtra("postMap");
		showPostText();

		initList();
	}

	private void initList() {

		adapter = new ReplyListAdapter(this, new ArrayList<Map<String,Object>>());
		replyListView.setAdapter(adapter);

		refreshList();


		replyListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE && !isWorking){
					refreshList();
				}

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});




	}


	List<Map<String,Object>> notFullMapList = new ArrayList<Map<String,Object>>();

	@SuppressWarnings("unchecked")
	private void refreshList() {

		Map<String,Object> map = new HashMap<>();
		map.put("func", "getRepliesByPage");
		map.put("post_id", postMap.get("post_id").toString());
		map.put("page", String.valueOf(page));
		map.put("size", SIZE);
		isWorking = true;
		
		if(!notFullMapList.isEmpty()){
			adapter.getList().removeAll(notFullMapList);
		}

		HttpTaskUtil htu = new HttpTaskUtil(this, new OnPostSent() {

			@Override
			public void onPostSuccess(Object obj) {

				isWorking = false;
				JSONArray ja = (JSONArray)obj;
				if (ja.length() == Integer.parseInt(SIZE)) {
					for (int i = 0; i < ja.length(); i++) {
						Map<String,Object> map = MyJSONUtils.jsonToMap(ja.optJSONObject(i));
						adapter.getList().add(map);
					}
					page++;
				}else{
					for (int i = 0; i < ja.length(); i++) {
						Map<String,Object> map = MyJSONUtils.jsonToMap(ja.optJSONObject(i));
						notFullMapList.add(map);
						adapter.getList().add(map);
					}
				}

				adapter.notifyDataSetChanged();

			}
		});
		htu.execute(map);
	}

	private void showPostText() {
		titleText.setText(postMap.get("title").toString());
		contentText.setText(postMap.get("content").toString());
		timeText.setText(postMap.get("launch_date").toString());
		nameText.setText(postMap.get("user_name").toString());

	}

	private void findView() {
		titleText = (TextView)findViewById(R.id.title);
		contentText = (TextView)findViewById(R.id.content);
		replyListView = (ListView)findViewById(R.id.reply_list);
		timeText = (TextView)findViewById(R.id.time);
		nameText = (TextView)findViewById(R.id.post_user_name);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem item = menu.add("发表回复");
		item.setIcon(R.drawable.add);
		item.setTitle("发表回复");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				showDialog();
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	EditText replyEdit;
	private void showDialog(){
		AlertDialog.Builder builder = new Builder(this);
		View view = getLayoutInflater().inflate(R.layout.reply_dialog, null);
		replyEdit = (EditText)view.findViewById(R.id.reply_dialog_edit);
		builder.setIcon(R.drawable.add).setView(view);
		builder.setPositiveButton("提交", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String reply = replyEdit.getText().toString().trim();
				if(reply.equals("")){
					Toast.makeText(PostActivity.this, "回复不能为空", Toast.LENGTH_LONG).show();
				}else{
					sendReply(reply);
				}
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();

	}

	@SuppressWarnings("unchecked")
	protected void sendReply(String reply) {

		Map<String,Object> map = new HashMap<>();
		map.put("func", "addReply");
		map.put("userm_id", userm_id);
		map.put("post_id", postMap.get("post_id").toString());
		map.put("replyContent", reply);

		HttpTaskUtil htu = new HttpTaskUtil(this, new OnPostSent() {

			@Override
			public void onPostSuccess(Object obj) {

				Toast.makeText(PostActivity.this, "发帖成功", Toast.LENGTH_LONG).show();
				refreshList();
			}
		});
		htu.execute(map);
	}

}
