package com.chekn.service.img;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class Resources {
	
	public static Map<String,String> init(String propertyName) { 
	    // 获得资源包  
	    ResourceBundle rb = ResourceBundle.getBundle(propertyName.trim());  
	    // 通过资源包拿到所有的key  
	    Enumeration<String> allKey = rb.getKeys();  
	    // 遍历key 得到 value  
	    Map<String,String> kvs = new HashMap<String,String>();  
	    while (allKey.hasMoreElements()) {  
	        String key = allKey.nextElement();  
	        String value = (String) rb.getString(key);  
	        kvs.put(key, value);  
	    }  
	    return kvs;  
	}
	
	public static void main(String[] args) {

	    ResourceBundle rb = ResourceBundle.getBundle("*");
	    System.out.println(rb.getString("remote"));
	}

}
