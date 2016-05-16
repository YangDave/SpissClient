package com.spiss.spissclient.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class MyJSONUtils {
	
	public static Map<String,Object> jsonToMap(JSONObject json) {
		Map<String,Object> map = new HashMap<>();
		@SuppressWarnings("unchecked")
		Iterator<String> keys = json.keys();
		while (keys.hasNext()) {
			String string = (String) keys.next();
			map.put(string, json.opt(string));
		}
		
		return map;
	}
	
	public static List<Map<String,Object>> jsonArrayToList(JSONArray ja){
		List<Map<String,Object>> list = new ArrayList<>();
		for (int i = 0; i < ja.length(); i++) {
			
			Map<String,Object> map = jsonToMap(ja.optJSONObject(i));
			list.add(map);
		}
		
		return list;
	}

}
