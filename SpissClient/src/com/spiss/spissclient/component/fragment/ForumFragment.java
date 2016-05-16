package com.spiss.spissclient.component.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.spiss.spissclient.R;
import com.spiss.spissclient.base.OnPostSent;
import com.spiss.spissclient.component.activity.PostListOfPlateActivity;
import com.spiss.spissclient.component.adapter.ForumPlateAdapter;
import com.spiss.spissclient.component.adapter.NewestAdapter;
import com.spiss.spissclient.component.adapter.RecommendAdapter;
import com.spiss.spissclient.constants.Config;
import com.spiss.spissclient.http.HttpTaskUtil;
import com.spiss.spissclient.http.HttpTaskWithLoadUtil;
import com.spiss.spissclient.utils.MyJSONUtils;

public class ForumFragment extends Fragment implements OnItemClickListener{
	
	RadioGroup rg;
	ForumPlateAdapter plateAdapter;
	NewestAdapter newestAdapter;
	RecommendAdapter recommendAdapter;
	ListView listView;
	
	static final String PLATE = "plate";
	static final String NEWEST = "newest";
	static final String RECOMMEND = "recommend";
	String selectedAdapterStr = PLATE;
	
	
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_forum,container, false);
		
		listView = (ListView)rootView.findViewById(android.R.id.list);
		plateAdapter = new ForumPlateAdapter(getActivity(), new ArrayList<Map<String,Object>>());
		newestAdapter = new NewestAdapter(getActivity(), new ArrayList<Map<String,Object>>());
		recommendAdapter = new RecommendAdapter(getActivity(), new ArrayList<Map<String,Object>>());
		
		listView.setAdapter(plateAdapter);
		listView.setOnItemClickListener(this);
		initRB(rootView);
		getPlateList();
		
		registerListener();
		
		return rootView;
	}
	

	private void registerListener() {

		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				switch (selectedAdapterStr) {
				case PLATE:
					 gotoPost(position);
					break;
				case NEWEST:
					break;
				case RECOMMEND:
					break;

				default:
					break;
				}
				
			}

			
		});
	}
	private void gotoPost(int position) {
		HashMap<String,Object> map = (HashMap<String, Object>) plateAdapter.getItem(position);
		Intent intent = new Intent(getActivity(),PostListOfPlateActivity.class);
		Bundle data = new Bundle();
		data.putSerializable("plate", map);
		intent.putExtras(data);
		startActivity(intent);
	}

	/**
	 * 列表无内容则执行刷新列表
	 * 用户也可以主动刷新列表
	 */

	@SuppressWarnings("unchecked")
	private void getPlateList() {
		plateAdapter.removeList();
		
		Map<String,Object> map = new HashMap<>();
		map.put("func", "getAllPlate");
		
		HttpTaskUtil htu = new HttpTaskUtil(getActivity(),new OnPostSent() {
			
			@Override
			public void onPostSuccess(Object obj) {
				
				JSONArray ja = (JSONArray)obj;
				
				List<Map<String,Object>> list = MyJSONUtils.jsonArrayToList(ja);
				plateAdapter.setList(list);
				plateAdapter.notifyDataSetChanged();
			}
			
		});
		htu.execute(map);
	}

	private void initRB(View v){
		
		rg = (RadioGroup)v.findViewById(R.id.rg);
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				
				switch(checkedId){
				case R.id.rb_plate:
					listView.setAdapter(plateAdapter);
					selectedAdapterStr = PLATE;
//					如果列表中没有item，则尝试从服务器获取
					if(plateAdapter.getList() == null){
						getPlateList();
					}
					break;
				case R.id.rb_newest:
					listView.setAdapter(newestAdapter);
					selectedAdapterStr = NEWEST;
					if(newestAdapter.getList() == null){
//						TODO
					}
					break;
				default:
					listView.setAdapter(recommendAdapter);
					selectedAdapterStr = RECOMMEND;
					if(recommendAdapter.getList() == null){
//						TODO
					}
					break;
					
				}
				
				
			}
		});
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (selectedAdapterStr) {
		case PLATE:
			int plate_id = (int) plateAdapter.getItem(position).get("plate_id");
			startActivity(new Intent(getActivity(),PostListOfPlateActivity.class).putExtra("plate_id", plate_id));
			break;

		default:
			break;
		}
	}
	
	

}
