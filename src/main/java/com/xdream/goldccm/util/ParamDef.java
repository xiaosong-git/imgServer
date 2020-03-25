package com.xdream.goldccm.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Properties;

public class  ParamDef {

	protected static Properties bankCardProp = null;
	
	protected static Properties idNOProp = null;
	
	protected static Properties imgProp = null;

	protected static Properties bankProp = null;

	protected static Properties db40Prop = null;

	protected static Properties fileProp = null;

	public static String findBankCardByName(String key){
		String value = null;
		try{
			if (bankCardProp==null){
				//载入
				bankCardProp = new Properties();
	            Resource resource = new ClassPathResource("bankCardConfig.properties");
	        	InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
	        	bankCardProp.load(in);
			}
			value = bankCardProp.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			return value;
		}
	}
	public static String findIdNoByName(String key){
		String value = null;
		try{
			if (idNOProp==null){
				//载入
				idNOProp = new Properties();
	            Resource resource = new ClassPathResource("idNoConfig.properties");
	        	InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
	        	idNOProp.load(in);
			}
			value = idNOProp.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
	public static String findImgByName (String key){
		String value = null;
		try{
			if (imgProp==null){
				//载入
				imgProp = new Properties();
				Resource resource = new ClassPathResource("imageConfig.properties");
				InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
				imgProp.load(in);
			}
			value = imgProp.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}

	
	
	public static String findBankByName(String key){
		String value = null;
		try{
			if (bankProp==null){
				//载入
				bankProp = new Properties();
	            Resource resource = new ClassPathResource("bankConfig.properties");
	        	InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
	        	bankProp.load(in);
			}
			value = bankProp.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();			
		}finally{
			return value;
		}
	}
	public static String findDB40ByName(String key){
		String value = null;
		try{
			if (db40Prop==null){
				//载入
				db40Prop = new Properties();
				Resource resource = new ClassPathResource("db40Config.properties");
				InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
				db40Prop.load(in);
			}
			value = db40Prop.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}
	public static String findFileByName(String key){
		String value = null;
		try{
			if (fileProp==null){
				//载入
				fileProp = new Properties();
				Resource resource = new ClassPathResource("fileConfig.properties");
				InputStream in = resource.getInputStream();//ClassLoader.getSystemResourceAsStream(propPath);
				fileProp.load(in);
			}
			value = fileProp.getProperty(key);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			return value;
		}
	}

}
