package com.spiss.spissclient.base;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.spiss.spissclient.application.ExitApplication;
import com.spiss.spissclient.application.MyApplication;
import com.spiss.spissclient.utils.DeviceUtil;


public abstract class MyActivity extends Activity{
	
	protected String user_id;
	protected String userm_id;
	protected String username = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setLayout();
		ExitApplication.getInstance().addActivity(this);
		DeviceUtil.bePortrait(this);
        MyApplication ma = (MyApplication)this.getApplicationContext();
        user_id = ma.getUser_id();
        userm_id = ma.getUserm_id();
        username = ma.getUsername();
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
        
//        initView();
//        registerListener();
//        showContent();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(item.getItemId() == android.R.id.home){

//			执行返回操作
		 super.onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
	
//	public abstract void setLayout();
//	public abstract void initView();
//	public abstract void registerListener();
//	public abstract void showContent();
	
	
	
	

}
