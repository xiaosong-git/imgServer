//package com.common;
package com.xdream.goldccm.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.NamedNodeMap;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

//import android.util.Log;

public class XmlParserXfordoc {

	public static List<String> XmlParserfordoc(String result) {
		boolean parse_status = false;// xml解析出错
		String recon_status = "";// 识别出错代码或证件类型
		String recon_error = "";// 识别出错原因
		String parse_error = "";// xml解析出错原因
		int length = 0;// 识别的行数
		List<String> rtnList = new ArrayList<String>();
		List<String> rowdescList = new ArrayList<String>();
		Map<String,String> map=new HashMap<String,String>();
		try {
				
				//----------------------------开始解析---------------------------------
				//1.读取xml文档，返回Document对象
				SAXReader reader = new SAXReader();
				Document doc = reader.read(new StringReader(result));
				Element rooElem = doc.getRootElement();//获取root
				
				Element card = (Element) rooElem.selectSingleNode("/data/cardsinfo/card");
				List list = card.elements(); // 得到card元素下的子元素rowitem集合  
				length = list.size();
				/* 
		         * 循环遍历集合中的每一个元素 
		         */
				for(int i=0;i<length;i++){
					Element element = (Element) list.get(i);  
					Attribute rowdesc=element.attribute(0);
					String rowdescToString=rowdesc.getStringValue();//得到属性rowdesc的值
					rowdescList.add(rowdescToString);
//					System.out.println(rowdescToString);//row0
					
					StringBuffer sb=new StringBuffer();
					
					List charitemlist=element.element("rowContext").elements("charitem");// 得到当前节点下的rowContext元素下的子元素charitem集合
					//---------------------------把这一行的字添加到sb------------------------------
					for(int j=0;j<charitemlist.size();j++){//遍历该行的每一个字节点
						Element charitem = (Element) charitemlist.get(j);  
						for(Iterator<Element> it = charitem.elementIterator();it.hasNext();){//遍历所有charitem的子元素，找到charValue节点的值
							Element e = it.next();
							if("charValue".equalsIgnoreCase(e.getName())){
								String str=e.getText().replaceAll("\n", "").replaceAll("\t", "").trim();
								sb.append(str);
							}
						}
					}
					//---------------------------将行与行的文字作为键值对存入map------------------------------
					map.put(rowdescToString, sb.toString());
//					System.out.println(sb.toString());
				}
				parse_status = true;
		} catch (Exception e) {
			parse_error = e.getMessage();
		}
		
		
		if (parse_status) {
			if (length > 0) {
				for(int i=0;i<rowdescList.size();i++){//遍历key集合
					String key=rowdescList.get(i);
//					String value = (key+"："+map.get(key)).replaceAll("\"", "&quot;").replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
					String value = (map.get(key)).replaceAll("\"", "&quot;").replaceAll("&","&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
					rtnList.add(value);
				}
			}else {
				System.out.println("错误编码：" + recon_status);
				System.out.println("错误描述：" + recon_error);
			} 
		}else {
			System.out.println("解析xml出错：" + parse_error);
		}
		
		return rtnList;
	}


}