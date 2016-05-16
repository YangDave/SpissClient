package com.spiss.spissclient.application;


import android.app.Application;
import android.content.Context;

/**
 * @author Charles
 *
 */
public class MyApplication extends Application{


	public static Context applicationContext;

	private String user_id ;
	private String username ;
	private String userm_id;
//	保存MainActivity的fragment索引
	private int fragIndex = 0;
	
	public int getFragIndex() {
		return fragIndex;
	}
	public void setFragIndex(int fragIndex) {
		this.fragIndex = fragIndex;
	}
	
	
	public String getUserm_id() {
		return userm_id;
	}
	public void setUserm_id(String userm_id) {
		this.userm_id = userm_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}


	public static interface Constant{

		int env=1;
	}

	@Override
	public void onCreate(){

		super.onCreate();
		applicationContext=getApplicationContext();

	}

}
