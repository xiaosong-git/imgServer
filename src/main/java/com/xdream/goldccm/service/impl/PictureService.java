package com.xdream.goldccm.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicMatch;

import org.springframework.stereotype.Service;

import com.xdream.goldccm.service.IPictureService;
import com.xdream.goldccm.third.IdNoConfig;

@Service("pictureService")
public class PictureService implements IPictureService{
	  /** 
     * @param filepath
	 * @throws Exception 
     */  
    public  String getCardData(String filepath) throws Exception {  
     /*   String filepath = "E:\\self.jpg";  */
    	String api_key = IdNoConfig.api_key;//"aB11SPj0CkserfQApOoA4QaBbGC6dZKk";
    	String api_secret = IdNoConfig.api_secret;//"EUavFiqRwGx96c5Dj0uburiNadruzmxU";
        String urlStr = IdNoConfig.url;//"https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";  
        Map<String, String> textMap = new HashMap<String, String>();  
        textMap.put("name", "testname");  
        textMap.put("api_key", api_key);
        textMap.put("api_secret", api_secret);
        Map<String, String> fileMap = new HashMap<String, String>();  
        fileMap.put("image_file", filepath);  
        String ret = formUpload(urlStr, textMap, fileMap);  
        String  cardData=new String(ret);
   
        return cardData;
    }  
  
    /** 
     * 上传图片 
     * @param urlStr 
     * @param textMap 
     * @param fileMap 
     * @return 
     * @throws Exception 
     */  
    public  String formUpload(String urlStr, Map<String, String> textMap, Map<String, String> fileMap) throws Exception { 
        String res = "";  
        HttpURLConnection conn = null;  
        String BOUNDARY =getBoundary(); //boundary就是request头和上传文件内容的分隔符    
        try {  
            URL url = new URL(urlStr);  
            conn = (HttpURLConnection) url.openConnection();  
            conn.setConnectTimeout(5000);  
            conn.setReadTimeout(30000);  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setUseCaches(false);  
            conn.setRequestMethod("POST");  
            
            conn.setRequestProperty("Accept-Charset", "utf-8");  
            conn.setRequestProperty("contentType", "utf-8"); 
            
            conn.setRequestProperty("Connection", "Keep-Alive");  
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");  
            conn.setRequestProperty("Content-Type", "multipart/form-data; charset=UTF-8;boundary=" + BOUNDARY);  
            OutputStream out = new DataOutputStream(conn.getOutputStream());  
            // text    
            if (textMap != null) {  
                StringBuffer strBuf = new StringBuffer();  
                Iterator<Map.Entry<String, String>> iter = textMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry<String, String> entry = iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
              
                    strBuf.append(inputValue);  
                    
                }  
                out.write(strBuf.toString().getBytes("UTF-8"));  
           
             
            }  
  
            // file    
            if (fileMap != null) {  
                Iterator<Map.Entry<String, String>> iter = fileMap.entrySet().iterator();  
                while (iter.hasNext()) {  
                    Map.Entry<String, String> entry = iter.next();  
                    String inputName = (String) entry.getKey();  
                    String inputValue = (String) entry.getValue();  
                    if (inputValue == null) {  
                        continue;  
                    }  
                    File file = new File(inputValue);  
                    String filename = file.getName();  
                    MagicMatch match = Magic.getMagicMatch(file, false, true);  
                    String contentType = match.getMimeType();  
               
                    StringBuffer strBuf = new StringBuffer();  
                    strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");  
                    strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"; filename=\"" + encode(filename) + "\"\r\n");  
                   
                    strBuf.append("Content-Type:" + contentType + "\r\n\r\n"); 
                   
                    out.write(strBuf.toString().getBytes("UTF-8"));  
  
                    DataInputStream in = new DataInputStream(new FileInputStream(file));  
                    int bytes = 0;  
                    byte[] bufferOut = new byte[1024];  
                    while ((bytes = in.read(bufferOut)) != -1) {  
                        out.write(bufferOut, 0, bytes);  
                    }  
                    in.close();  
                }  
            }  
            
  
            byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("UTF-8");  
            out.write(endData);  
            out.flush();  
            out.close();  
  
            // 读取返回数据    
            StringBuffer strBuf = new StringBuffer();  
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));  
            String line = null;  
            while ((line = reader.readLine()) != null) {  
                strBuf.append(line).append("\n");  
            }  
            res = strBuf.toString();  
            reader.close();  
            reader = null;  
        } catch (Exception e) {  
            System.out.println("发送POST请求出错。" + urlStr);  
            e.printStackTrace();  
            throw e;
        } finally {  
            if (conn != null) {  
                conn.disconnect();  
                conn = null;  
            }  
        }          
        return res;  
    }  
    
    
    private  String getBoundary() {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < 32; ++i) {
            sb.append("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_-".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".length())));
        }
        return sb.toString();
    }
    
    private  String encode(String value) throws Exception{
        return URLEncoder.encode(value, "UTF-8");
    }
    
}
