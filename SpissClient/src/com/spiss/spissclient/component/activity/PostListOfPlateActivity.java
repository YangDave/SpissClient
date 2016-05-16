package com.spiss.spissclient.component.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.spiss.spissclient.R;
import com.spiss.spissclient.base.MyListActivity;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.component.adapter.PostListAdapter;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.utils.MyJSONUtils;

public class PostListOfPlateActivity extends MyListActivity{

	Map<String,Object> plateInfo;
	PostListAdapter adapter;

	String plate_id;
	int page = 1;
	String size = "5";
	boolean isWorking = false;
	
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.post_list_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	




	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case R.id.add_post:showDialog();
			break;
			
		}
		return super.onMenuItemSelected(featureId, item);
	}
	EditText titleEdit;
	EditText contentEdit;
	private void showDialog(){
		AlertDialog.Builder builder = new Builder(this);
		View view = getLayoutInflater().inflate(R.layout.add_post_dialog, null);
		titleEdit = (EditText)view.findViewById(R.id.title_edit);
		contentEdit = (EditText)view.findViewById(R.id.content_edit);
		builder.setView(view);
		builder.setIcon(R.drawable.add).setTitle("发表新帖");
		builder.setPositiveButton("提交", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				handlePost();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
		
		
	}
	
	private void handlePost(){
		String title = titleEdit.getText().toString().trim();
		String content = contentEdit.getText().toString().trim();
		
		if(!title.equals("")&& !content.equals("")){
			sendNewPost(title,content);
		}else{
			Toast.makeText(this, "请填写完整", Toast.LENGTH_LONG).show();
		}
	}

	@SuppressWarnings("unchecked")
	private void sendNewPost(String title,String content) {
//		{"func":"newPost","title":"dsadfs","content":"dfasdf","plate_id":"1","userm_id":"1"}
		Map<String,Object> map = new HashMap<>();
		map.put("func", "newPost");
		map.put("title", title);
		map.put("content", content);
		map.put("plate_id", plate_id);
		map.put("userm_id", userm_id);
		HttpTaskUtil htu = new HttpTaskUtil(this, new OnPostSent() {
			
			@Override
			public void onPostSuccess(Object obj) {
				
				Toast.makeText(PostListOfPlateActivity.this, "发帖成功", Toast.LENGTH_LONG).show();
				nextList();
			}
		});
		htu.execute(map);
	}






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_postofplate);
		

		Intent intent = getIntent();
		@SuppressWarnings("unchecked")
		Map<String,Object> plateInfo =  (Map<String, Object>) intent.getSerializableExtra("plate");
		plate_id = plateInfo.get("plate_id").toString();
		setTitle(plateInfo.get("plate_name").toString());

		adapter = new PostListAdapter(this, new ArrayList<Map<String,Object>>());

		setListAdapter(adapter);

		initList();

		registerListener();


	}




	private void registerListener() {
		getListView().setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

				if((scrollState == OnScrollListener.SCROLL_STATE_IDLE )&&(!isWorking)){

					nextList();
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}
		});
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				HashMap<String,Object> hm = (HashMap<String, Object>) adapter.getItem(position);
				Intent intent = new Intent(PostListOfPlateActivity.this,PostActivity.class);
				Bundle data = new Bundle();
				data.putSerializable("postMap", hm);
				intent.putExtras(data);
				startActivity(intent);
			}
			
		});
	}




	private void initList() {

		nextList();
	}


	List<Map<String,Object>> notfullMapList = new ArrayList<>();


	@SuppressWarnings("unchecked")
	private void nextList() {
		Map<String,Object> map = new HashMap<>();
		map.put("func", "findPostByPage");
		map.put("plate_id", plate_id);
		map.put("page", String.valueOf(page));
		map.put("size", size);
		isWorking = true;
		if(!notfullMapList.isEmpty()){
			adapter.getList().removeAll(notfullMapList);
		}

		HttpTaskUtil htu = new HttpTaskUtil(this, new OnPostSent() {

			@Override
			public void onPostSuccess(Object obj) {

				isWorking = false;
				JSONArray ja = (JSONArray)obj;
				if (ja.length() == Integer.parseInt(size)) {
					for (int i = 0; i < ja.length(); i++) {
						Map<String,Object> map = MyJSONUtils.jsonToMap(ja.optJSONObject(i));
						adapter.getList().add(map);
					}
					page++;
				}else{
					for (int i = 0; i < ja.length(); i++) {
						Map<String,Object> map = MyJSONUtils.jsonToMap(ja.optJSONObject(i));
						notfullMapList.add(map);
						adapter.getList().add(map);
					}
				}

				adapter.notifyDataSetChanged();
			}
		});

		htu.execute(map);
	}

}
