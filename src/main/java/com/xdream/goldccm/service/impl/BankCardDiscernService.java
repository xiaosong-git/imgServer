package com.xdream.goldccm.service.impl;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xdream.JsonUtils;
import com.xdream.goldccm.service.IBankCardDiscernService;
import com.xdream.goldccm.third.BankConfig;
import com.xdream.uaas.server.tools.ResponseBank;

//LL
@Service("bankCardDiscernService")
public class BankCardDiscernService implements IBankCardDiscernService {



	public String getBankCardDiscern(String filepath) throws Exception {

		String key =BankConfig.key; //"aB11SPj0CkserfQApOoA4QaBbGC6dZKk";// 用户ocrKey
		String secret =BankConfig.secret;// "EUavFiqRwGx96c5Dj0uburiNadruzmxU";// // 用户ocrSecret
		String url =BankConfig.url;// "https://api-cn.faceplusplus.com/cardpp/beta/ocrbankcard";//
		String json = null;
		String time_used = "";
		String request_id = "";
		String error_message = "";
		String bank_cards = "";
		String cardsStr = "";
		String cardNumber = "";
		String bankName = "";
		String resultback = "";
		File file = new File(filepath);
		resultback = doPost(url, file, key, secret, filepath);

		System.out.println("返回数据：" + resultback);

		if(StringUtils.isBlank(resultback) || "[]".equals(resultback) ){
			
			ResponseBank bank = new ResponseBank();
			json = JsonUtils.toJson(bank);
            return json;
		}
			

		JSONObject jsonObject = JSON.parseObject(resultback);		
		
		time_used = jsonObject.getString("time_used");

		request_id = jsonObject.getString("request_id");

		error_message = jsonObject.getString("error_message");

		if (StringUtils.isBlank(error_message)) {
			System.out.println("识别成功");

			System.out.println("======================");

			bank_cards = jsonObject.getJSONArray("bank_cards").toJSONString();// bank_card信息

			System.out.println("卡信息：" + bank_cards);

			if (StringUtils.isBlank(bank_cards) || "[]".equals(bank_cards)) {
				System.out.println("bank_cards信息为空");
				ResponseBank bank = new ResponseBank();
				json = JsonUtils.toJson(bank);
	            return json;

			}

			cardsStr = bank_cards.substring(1, bank_cards.length() - 1);

			Map<String, String> cardMap = new HashMap<String, String>();
			cardMap = (Map<String, String>) JSON.parse(cardsStr);

			cardNumber = cardMap.get("number");
			bankName = cardMap.get("bank");

			System.out.println("银行卡号：" + cardNumber);
			System.out.println("银行名称：" + bankName);

			ResponseBank bank = new ResponseBank();

			bank.setBankName(bankName);
			bank.setCardNumber(cardNumber);
			json = JsonUtils.toJson(bank);
			return json;

		} else {
			System.out.println("识别失败");
			ResponseBank bank = new ResponseBank();
			json = JsonUtils.toJson(bank);

			return json;
		}

	}

	public String doPost(String url, File file, String key, String secret,
			String filepath) throws Exception {
		String result = null;

		CloseableHttpClient client = HttpClients.createDefault(); // 1.创建httpclient对象
		HttpPost post = new HttpPost(url); // 2.通过url创建post方法

		MultipartEntityBuilder builder = MultipartEntityBuilder.create(); // 实例化实体构造器
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE); // 设置浏览器兼容模式

		builder.addPart("image_file", new FileBody(file)); // 添加"file"字段及其值；此处注意字段名称必须是"file"
		builder.addPart("api_key",new StringBody(key, ContentType.create("text/plain",
						Consts.UTF_8))); // 添加"key"字段及其值
		builder.addPart(
				"api_secret",
				new StringBody(secret, ContentType.create("text/plain",
						Consts.UTF_8))); // 添加"secret"字段及其值


		HttpEntity reqEntity = builder.setCharset(CharsetUtils.get("UTF-8"))
				.build(); // 设置请求的编码格式，并构造实体

		post.setEntity(reqEntity);
		// **************************************</向post方法中封装实体>************************************

		CloseableHttpResponse response = client.execute(post); // 4.执行post方法，返回HttpResponse的对象

		System.out.println("返回码：" + response.getStatusLine().getStatusCode());
		if (response.getStatusLine().getStatusCode() == 200) { // 5.如果返回结果状态码为200，则读取响应实体response对象的实体内容，并封装成String对象返回
			result = EntityUtils.toString(response.getEntity(), "UTF-8");
		} else {
			System.out.println("服务器返回异常");
		}

		HttpEntity e = response.getEntity(); // 6.关闭资源
		try {
			if (e != null) {
				InputStream instream = e.getContent();
				instream.close();
			}
		} catch (Exception e2) {
			e2.printStackTrace();
			System.out.println("银行卡异常");
		} finally {

			response.close();

		}
		return result;// 7.返回识别结果

	}

}
