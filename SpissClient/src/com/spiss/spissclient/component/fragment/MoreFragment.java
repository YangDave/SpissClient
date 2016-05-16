package com.spiss.spissclient.component.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.spiss.spissclient.R;
import com.spiss.spissclient.component.activity.FaceLoginActivity;
import com.spiss.spissclient.component.activity.FaceReBuildActivity;

public class MoreFragment extends Fragment{
	
	LinearLayout rebuildLl;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_more, container,false);
		
		initView(rootView);
		addListener();
		return rootView;
	}
	
	private void addListener() {

		rebuildLl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent(getActivity(),FaceReBuildActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initView(View view){
		rebuildLl = (LinearLayout)view.findViewById(R.id.face_rebuild);
	}
	
	
	

}
