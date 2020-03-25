package com.xdream;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class JsonUtils {

	public static String toJson(Object obj) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		
		return mapper.writeValueAsString(obj);
	}
	
	public static Object toObj(String json,Class cls) throws Exception{
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return mapper.readValue(json, cls);
		
	}

	public static void main(String[] args) throws Exception{
		Map<String,String> vo = new HashMap<String,String>();
		
		vo.put("1", "aaaaaaaaaa");
		vo.put("2", "bbb");
		vo.put("3", "cccc");
		
		String json = JsonUtils.toJson(vo);
		System.out.println("aa:"+json);
		
		Map<String,String> vo1 = (Map<String,String>)JsonUtils.toObj(json, Map.class);
		
		for (Map.Entry<String, String> entry : vo1.entrySet()) {
			   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
	    }
	}
}
