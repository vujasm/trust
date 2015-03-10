package com.inn.common;

import java.util.HashMap;

import com.google.common.collect.Maps;

public class ValuesHolder {
	
	private HashMap<String, Object> hashMap = Maps.newHashMap();
	
	public void setValue(String key, Object value){
		hashMap.put(key, value);
	}
	
	public Object getValue(String key, Object ifnullValue){
		Object val = hashMap.get(key);
		if (val == null)
			return ifnullValue;
		else
			return val;
	}
	

}
