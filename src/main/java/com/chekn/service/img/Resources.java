package com.chekn.service.img;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Resources {

	public static Map<String, String> init(String propertyName) {
		// 获得资源包
		ResourceBundle rb = ResourceBundle.getBundle(propertyName.trim());
		ResourceBundle rb2=null ;
		try{rb2 =ResourceBundle.getBundle(propertyName.trim()+"-ext");}catch(Exception e){}

		Map<String, String> kvs = new HashMap<String, String>();
		pckKvFrRb(kvs, rb);
		pckKvFrRb(kvs, rb2);
		return kvs;
	}
	
	private static void pckKvFrRb(Map<String,String> kvs, ResourceBundle rb) {
		if(rb == null) return ;
			
		// 通过资源包拿到所有的key
		Enumeration<String> allKey = rb.getKeys();
		// 遍历key 得到 value
		while (allKey.hasMoreElements()) {
			String key = allKey.nextElement();
			String value = (String) rb.getString(key);
			kvs.put(key, value);
		}
		
	}

	public static void main(String[] args) {
		ResourceBundle rb = ResourceBundle.getBundle("redis-ext");
		System.out.println(rb.getString("auth")+ "-"+ Resources.init("redis").get("auth") );
	}

}
