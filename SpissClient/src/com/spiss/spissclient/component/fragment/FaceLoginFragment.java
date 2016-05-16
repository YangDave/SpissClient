package com.spiss.spissclient.component.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.facepp.error.FaceppParseException;
import com.facepp.http.HttpRequests;
import com.facepp.http.PostParameters;
import com.spiss.spissclient.R;
import com.spiss.spissclient.component.activity.FaceCameraActivity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FaceLoginFragment extends Fragment{
	
	private Button button1;
	private Button button_rebuilt;
	private String username;
	private Thread thread;
	EditText userNameEdit;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_face_login, container,false);
		button1 = (Button)view.findViewById(R.id.button1);
		userNameEdit = (EditText)view.findViewById(R.id.user_name_edit);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				username = userNameEdit.getText().toString().trim();
				if(username.equals("")){
					Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_LONG).show();
				}else{
					Intent intent = new Intent(getActivity(), FaceCameraActivity.class);
					intent.putExtra("user", username);
					startActivity(intent);
				}
			}
		});

		button_rebuilt = (Button)view.findViewById(R.id.button_1);
		button_rebuilt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				username = userNameEdit.getText().toString().trim();
				if(username.equals("")){
					Toast.makeText(getActivity(), "请输入用户名", Toast.LENGTH_LONG).show();
				}else{
					thread = new Thread(runnable);
					thread.start();
				}
			}
		});
		
		return view;
	}
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			JSONObject result = null;
			HttpRequests httpRequests = new HttpRequests("270ff76b7c212533a3079aa0be82a3c5","hsWv6TPO6nc_PteGNxAPMwihBEBQQpYz");
			try {
				result = httpRequests.personDelete(new PostParameters().setPersonName(username));
				try {
					if(result.getBoolean("success")){
						getActivity().runOnUiThread(new Runnable() {

							public void run() {
								Toast.makeText(getActivity().getApplication(), "删除成功，请重新建模", Toast.LENGTH_LONG).show();
							}
						});
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (FaceppParseException e) {
				e.printStackTrace();
			}

		}
	};

}
