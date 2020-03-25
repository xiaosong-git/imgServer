package com.xdream.goldccm.service.impl;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

import com.xdream.JsonUtils;
import com.xdream.goldccm.service.IBankcardService;
import com.xdream.goldccm.third.BankCardConfig;
import com.xdream.goldccm.util.JsonUtil;
import com.xdream.goldccm.util.XmlParserX;
import com.xdream.uaas.server.tools.ResponseBank;

@Service("bankcardService")
public class BankcardService implements IBankcardService {
	public String getBankCardData(String filepath) throws Exception {
		File file = new File(filepath);
		String key = BankCardConfig.key;//"MVvEKf6qoGUh75Lpon4THK"; // 用户ocrKey
		String secret = BankCardConfig.secret;//"87dc6e02d13b4fbe8bcb6aed3b1fab7c"; // 用户ocrSecret
		String typeId = "17";								//证件类型(例如:二代证正面为"2"。详见文档说明)
//		String format = "xml";
		String format = "json"; //(返回的格式可以是xml，也可以是json)
		String url = BankCardConfig.url;//"https://netocr.com/api/recog.do";		//http接口调用地址

		String resultback = doPost(url, file, key, secret, typeId, format); 
		List<String> list=new ArrayList<String>();
		System.out.println("返回数据："+resultback);						//控制台打印输出识别结果
		String json=null;
		
		//解析
		if ("xml".equalsIgnoreCase(format)) {// 如果是xml，则解析xml并打印输出
			list=XmlParserX.XmlParser(resultback);//解析xml字符串
//			list=XmlParserXfordoc.XmlParserfordoc(resultback);//解析xml字符串(文档)
			for(int i=0;i<list.size();i++){
				System.out.println(list.get(i));
			}
		} else if ("json".equalsIgnoreCase(format)) { // 如果是json，则解析json并打印输出
				list=JsonUtil.printjson(resultback);						//解析json字符串
//				list=JsonUtil.printjsonfordoc(resultback);
				//解析json字符串(文档)
				
				ResponseBank bank=new ResponseBank();	
				for(int i=0;i<list.size();i++){
					String  temp[]=list.get(i).split(":");
					
					if("卡号".equals(temp[0])){
						bank.setCardNumber(temp[1]);
						
					}
						
					if("银行卡类型".equals(temp[0])){
						bank.setBankCardType(temp[1]);
						
					}
					
					if("银行卡名称".equals(temp[0])){
						bank.setBankCradName(temp[1]);
						
					}
					if("银行名称".equals(temp[0])){
						bank.setBankName(temp[1]);
				
					}
					if("银行编号".equals(temp[0])){
						bank.setBankNumber(temp[1]);
					
					}
					
				}			
					 json=JsonUtils.toJson(bank);
		}
		
						return json;
	}
	

	
	public  String doPost(String url, File file, String key,String secret, String typeId, String format) throws Exception {
				String result = null;
		

				CloseableHttpClient client = HttpClients.createDefault(); 										// 1.创建httpclient对象
				HttpPost post = new HttpPost(url); 																// 2.通过url创建post方法
				
				if("json".equalsIgnoreCase(format)){
					post.setHeader("accept", "application/json");
				}else if("xml".equalsIgnoreCase(format)||"".equalsIgnoreCase(format)) {
					post.setHeader("accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
				}
				
				//***************************************<向post方法中封装实体>************************************//3.向post方法中封装实体
				/* post方式实现文件上传则需要使用multipart/form-data类型表单，httpclient4.3以后需要使用MultipartEntityBuilder来封装
				 * 对应的html页面表单：
					 <form name="input" action="http://netocr.com/api/recog.do" method="post" enctype="multipart/form-data">
				        	请选择要上传的文件<input  type="file" NAME="file"><br />
							key:<input type="text" name="key" value="W8Nh5A**********duwkzEuc" />	<br />
							secret:<input type="text" name="secret" value="9646d012210*********ba16737d6f69f" /><br />
							typeId:<input type="text" name="typeId" value="2"/><br />
							format:<input type="text" name="format" value=""/><br />
							<input type="submit" value="提交">
					</form>
				 */
				
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();									//实例化实体构造器
				builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);												//设置浏览器兼容模式
	
				builder.addPart("file", new FileBody(file));														//添加"file"字段及其值；此处注意字段名称必须是"file"
				builder.addPart("key", new StringBody(key, ContentType.create("text/plain", Consts.UTF_8)));		//添加"key"字段及其值
				builder.addPart("secret", new StringBody(secret, ContentType.create("text/plain", Consts.UTF_8)));	//添加"secret"字段及其值
				builder.addPart("typeId", new StringBody(typeId, ContentType.create("text/plain", Consts.UTF_8)));	//添加"typeId"字段及其值
				builder.addPart("format", new StringBody(format, ContentType.create("text/plain", Consts.UTF_8)));	//添加"format"字段及其值
	
				HttpEntity reqEntity = builder.setCharset(CharsetUtils.get("UTF-8")).build();						//设置请求的编码格式，并构造实体
				
	
				post.setEntity(reqEntity);
				//**************************************</向post方法中封装实体>************************************
	
				CloseableHttpResponse response = client.execute(post);												 // 4.执行post方法，返回HttpResponse的对象
				if (response.getStatusLine().getStatusCode() == 200) {		// 5.如果返回结果状态码为200，则读取响应实体response对象的实体内容，并封装成String对象返回
					result = EntityUtils.toString(response.getEntity(), "UTF-8"); 
				} else {
					 System.out.println("服务器返回异常");
				}
	               
				    
					HttpEntity e = response.getEntity();					 // 6.关闭资源
					try {
						if (e != null) {
							InputStream instream = e.getContent();
							instream.close();
						}
					} catch (Exception e2) {
						e2.printStackTrace();
			            System.out.println("银行卡异常");
					}finally{

						response.close();
						
					}
					return result;														//7.返回识别结果
		
	}
	
/*	public static void main(String[] args) {
		String bankmess="{\"message\":{\"status\":0,\"value\":\"识别完成\"},\"cardsinfo\":[{\"type\":\"17\",\"items\":[{\"nID\":null,\"index\":null,\"desc\":\"卡号\",\"content\":\"6217001820002381188\"},{\"nID\":null,\"index\":null,\"desc\":\"银行卡类型\",\"content\":\"借记卡\"},{\"nID\":null,\"index\":null,\"desc\":\"银行卡名称\",\"content\":\"龙卡通\"},{\"nID\":null,\"index\":null,\"desc\":\"银行名称\",\"content\":\"建设银行\"},{\"nID\":null,\"index\":null,\"desc\":\"银行编号\",\"content\":\"01050000\"}]}]}";
	     
		try {
	    	List<String> list=new ArrayList<String>();
			list=JsonUtil.printjson(bankmess);
			
			ResponseBank bank=new ResponseBank();
			
			
			for(int i=0;i<list.size();i++){
				String  temp[]=list.get(i).split(":");
				
				if("卡号".equals(temp[0])){
					bank.setCardNumber(temp[1]);;
					
				}
					
				if("银行卡类型".equals(temp[0])){
					bank.setBankCardType(temp[1]);
					
				}
				
				if("银行卡名称".equals(temp[0])){
					bank.setBankCradName(temp[1]);
					
				}
				if("银行名称".equals(temp[0])){
					bank.setBankName(temp[1]);
			
				}
				if("银行编号".equals(temp[0])){
					bank.setBankNumber(temp[1]);
				
				}
			
			}
			
			String json=JsonUtils.toJson(bank);
			System.out.println(json);
				
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
	}*/
}
