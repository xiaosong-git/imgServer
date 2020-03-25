package com.xdream.goldccm.util;

import com.xdream.JsonUtils;
import org.codehaus.jettison.json.JSONObject;

import java.util.Iterator;

public class ResponseObj {

	private ResponseBase verify;
	
	private ResponseData data;

	public ResponseObj(){

	}

	public ResponseObj(ResponseBase verify) {
		this.verify = verify;
	}

	public ResponseBase getVerify() {
		return verify;
	}

	public void setVerify(ResponseBase verify) {
		this.verify = verify;
	}

	public ResponseData getData() {
		return data;
	}

	public void setData(ResponseData data) {
		this.data = data;
	}
	

	public static void main(String[] args) throws Exception{

		ResponseBase base = new ResponseBase();
		ResponseObj obj = new ResponseObj();
		base.setSign("fail");
		base.setDesc("提交成功");
		System.out.println(obj.toString());
		obj.setVerify(base);
//		ResponseData data = new ResponseData();
//		data.setImageFileName("dddddddddddddd");
//		obj.setData(data);

		String json = JsonUtils.toJson(obj);
		JSONObject jsonObject=new JSONObject(json);
		jsonObject.put("desc","提交成功");
		jsonObject.put("sign","success");
		jsonObject.put("code","0");

		jsonObject.remove("data");
		String json1=jsonObject.toString();
		Iterator iterator = jsonObject.keys();
		while (iterator.hasNext()){

			System.out.println((String) iterator.next());
		}
		System.out.println("Json:"+json);
		System.out.println("Json1:"+json1);

		String test = "111.jpg";
		int index = test.lastIndexOf(".");
		if (index<0){
			System.out.println("1111111111111111111111");
			return;
		}
		System.out.println("index:"+index);
		String name = test.substring(index+1,test.length());
		System.out.println("name:"+name);
	}
	
}
