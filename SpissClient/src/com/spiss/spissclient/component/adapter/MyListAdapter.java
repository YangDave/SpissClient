package com.spiss.spissclient.component.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class MyListAdapter<T> extends BaseAdapter{
	
	protected List<T> list;
	protected Context context;
	
	public MyListAdapter(Context context,List<T> list){
		this.context = context;
		this.list = list;
	}
	
	public List<T> getList(){
		return list;
	}
	
	public void setList(List<T> list){
		this.list = list;
	}
	
	public boolean removeList(){
		return list.removeAll(list);
	}
	
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public abstract View getView(int position, View convertView, ViewGroup parent);

}
