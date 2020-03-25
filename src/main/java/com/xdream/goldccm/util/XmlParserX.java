//package com.common;
package com.xdream.goldccm.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import android.util.Log;

public class XmlParserX {

	public boolean parse_status = false;// xml解析出错

	public String parse_error = "";// xml解析出错原因

	public String recon_status = "";// 识别出错代码或证件类型

	public String recon_error = "";// 识别出错原因

	public int length = 0;// 识别的item个数

	public String fieldname[] = new String[30];

	public String fieldvalue[] = new String[30];

	public XmlParserX(InputStream inputStream) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(inputStream);
			Element element = document.getDocumentElement();
			parse_status = getItemValue(element);
		} catch (Exception e) {
			parse_error = e.getMessage();
		}

	}

	private boolean getItemValue(Element element) {
		boolean value = false;
		try {
			recon_status = element.getElementsByTagName("status").item(0).getFirstChild().getNodeValue();
			recon_error = element.getElementsByTagName("value").item(0).getFirstChild().getNodeValue();

			NodeList nodelist = element.getElementsByTagName("item");
			if (nodelist != null) {
				length = nodelist.getLength();
				for (int i = 0; i < length; i++) {
					// 对每个item的处理
					fieldname[i] = "";
					fieldvalue[i] = "";
					Node node = nodelist.item(i);

					// 打印属性中的名称
					NamedNodeMap nnm = node.getAttributes();
					if (nnm != null) {
						Node nodea = nnm.getNamedItem("desc");
						if (nodea != null) {
							fieldname[i] = nodea.getNodeValue();
						}
					}

					// 打印名称对应的值
					Node nodevalue = node.getFirstChild();
					if (nodevalue != null) {
						fieldvalue[i] = nodevalue.getNodeValue();
					}
				}
			}
			value = true;
		} catch (Exception e) {
			parse_error = e.getMessage();
		}
		return value;
	}
	public static List<String> XmlParser(String result) {
		ByteArrayInputStream is = null;
		List<String> rtnList = new ArrayList<String>();
		try {
			is = new ByteArrayInputStream(result.getBytes("UTF-8"));
			XmlParserX xmlParser = new XmlParserX(is);
			if (xmlParser.parse_status) {
				if (xmlParser.length > 0) {
					// System.out.println("证件类型：" + xmlParser.recon_status);
					for (int i = 0; i < xmlParser.length; i++) {
						String value = (xmlParser.fieldname[i] + "：" + xmlParser.fieldvalue[i])
								.replaceAll("\"", "&quot;").replaceAll("&",
										"&amp;").replaceAll("<", "&lt;")
								.replaceAll(">", "&gt;");
						rtnList.add(value);
//						rtnList.add("");
					}
				} else {
					System.out.println("错误编码：" + xmlParser.recon_status);
					System.out.println("错误描述：" + xmlParser.recon_error);
				}
			} else {
				System.out.println("解析xml出错：" + xmlParser.parse_error);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rtnList;
	}
	
	/**
	 * @param args
	 */
	/*	public static void main(String[] args) {

	 // 版本6.3
	 String str = "<?xml version='1.0' encoding='UTF-8'?>"
	 + "<data>"
	 + "<message>"
	 + "<status>2</status>"
	 + "<value>识别成功</value>"
	 + "</message>"
	 + "<cardsinfo>"
	 + "<card type=\"2\">"
	 + "<item desc=\"姓名\">尹枣俊</item>"
	 + "<item desc=\"性别\"></item>"
	 + "<item desc=\"民族\">汉</item>"
	 + "<item desc=\"出生\">1976-08-09</item>"
	 + "<item desc=\"住址\">北京市昌平区回龙观镇龙腾苑</item>"
	 + "<item desc=\"公民身份号码\">510212197608</item>"
	 + "<item desc=\"头像

\">/9j/4AAQSkZJRgABAQEBLAEsAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMj

IyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAEPANIDASIAAhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQAAAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo

0NTY3ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWmp6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEAAwEBAQEBAQEBAQAAAAAAAAECAwQFBgcI

CQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSExBhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElKU1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqs

rO0tba3uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDpGHIpdu3HPWgn5fej

+GgYBu3f1pVOAQOKDyB6Um0HBPWmIXODk8UpBx/Sm8njvTgMdTQSJjPuabjBp209jxSHGOaYDGBP1pCo/GnHJ9hTRgZycUCFDH0/KjaSOuPakDAEgCgM2TQSLtweT1o4B4oGCOeaQ4zTEIWO/pTjubHakDAE04MMUANCncc5/OgBuSDil3gZoDDAoEKVP

XNIBgE85pxPFIT0FAAhKjHrSqT0xSg5alwM0hoazcY7mkI4AoKkt9KRScj0pgDLnj1prxgYFSAnqR1pAcgn8qQDMj1NFO8se1FAx5K

+maRSTx2pVHGaQ9cj8ak2uOHHU80LQORQcDrzTJFzg8UhYfUignI44puNre1AheW5oKgDJoJwODUUj470xC788U1iO/NVZrpYcl2AArDvPEKxkiH5j60WEdG0qrzUD30SNy4H41xVzrVzKDmQjPpVB7mR8ksST6mnYR6EuoQt/wAtF/OpRcRuOGB/GvNR

O47mpFvpkIKyMMehosB6OHXHXNSxup6CuAtvENzAQGO9feuh03XYbg4LbXPY0hHRHBpCoP4VCswbBHSpA

+R7mmA8KMU0r3p2cCjOcCgQIfXrTi4FAPNHBOewoGH8PXk0Fc4FJ1Ofypw7k0AIQRxnrTRnIHpTyO/emEHp3oAXcPSik2+1FAC5GOtGeM9qjXg89acPc1Bu0P3EjAFLgd6RTnjFKSBQSHPemM4GRTzzULsB060xDTIV7cVlalq0VmuM5c9qdqmorZxEk/

Megrhru7eeUszZJ96aQi1d6nLcuS7nGeBVFpc1XZu5oDcdaoRIXyKYXPrxTSwHvSA0CHFiRim5xSHnjtRjFACgnPNOWVkOVJBFR55oJ5oEdBpWvywsscx3Ie9dpBOs0YcHgivLASvOa6PSvEC2sKxygketJoDt85704MVGe9ZlpqtvdAbHH0rRRgwz2pC

JQ3y04dgPxNMx8tKDhenNMCTuPak6k8ZFNDYXnrTtwzjv3oAaWO8DtSg4BJpxApv4UDE3H0opd1FADAoH1o2fNknNJk9vzpykD61BuO5oOO1HXrQTxxQIaWPQ1XuJVjjZieAM1K/TJNc74hv/AC7fyUbDN1+lNEs5/U71ry5di3AOBWU5Gae79R61Czc1

ZLEyM0fhSZpc8UCE7ilPPQUqxs3RalW1mbohpXKsyDnFBq4unXLdENP/ALKuNvK0XQcrM38aN1XX06dR92q7WsidVNFxWZHu7UoPFBjZT0pCCOtMRPBcyQurI5BrstD1r7TiKUjcP1rhQec1atbl4JQ6nBBoYj1RXBwc8VKDkZrH0m

+F5bK3GR1Faq8kGkIkxx6mjGB0oBxz3oJ4oATPb1pSfyppB4x1NOyVGOpoAOKKMNRQA0rxjoKTHoKceepo7cVB0CL70p5pDwM0c0xMimIVSSa4DWbkzXbknvxXZarP5Nm5zzivPbuXdI31qkQyuWHNRk5NLmkVdzCmJEkULSOFUda17bR8gF6taTYAIHY

cmtyOEDtWUpGyikjPg02NQPkFXEs41HQflVsIAadipGV1iAGQtDQjHSrYUdqQqCORQBQa3QjkVBLYxsPu1plRio2XJ4ouBhzaXGRwMVlahZCFMgV1zR5zkVlajCGjIxVRlqTJaHHE4NOQ80+4j2OeKiB7itjA6bw1feVcmJjwwruom3AGvKbaZopkdeCD

mvSdLuPtFpHJnqKlgaVGCRmmjnmn/wAqBDc7Rz1pwBHJ60mAeadjNA7BRS4FFADBwcY5paQjnrTgMVBsxrfdpCOKc3JAph70xMw/ELAWWM964C4Ylzj1ruPEzbbVR3Jrhpxgk

+tWiWRYJBrQ061aSQHHAqGys5LlgQDtz1rqLKyWBRxUSkaQh1L1sgSMAcVZQVUEmw9DipRc88Cs+Utst4NIAc81EtwCcEVMGzTEPUH8KDyKVelOpCK7cCmZFSyMFqo06d+tFh3JCap3S7kPFS

+evrTHIccdKEtQOQ1CPEzAVnAc10WpWvLMBXPsCrnit1sYSWo8HgV3fhacvp6qeqnFcCDj612PhCT5JUPrmhknYjmnio1PAqQdKBAKXpQKMc5oATB9aKdiikMbnnOKUGjtQak2EPNMbIGakxionOTgUCOV8UsSqDtXFzAl8V6Jr1oZ7M7RkiuEazmNwFK

kZOMmqvoKx1Oj2YisY

+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</

item>"
	 + "</card>" + "</cardsinfo>" + "</data>";

	 ByteArrayInputStream is = null;
	 try {
	 is = new ByteArrayInputStream(str.getBytes("UTF-8"));
	 XmlParserX xmlParser = new XmlParserX(is);
	 if (xmlParser.parse_status) {
	 if (xmlParser.length > 0) {
	 System.out.println("证件类型：" + xmlParser.recon_status);
	 for (int i = 0; i < xmlParser.length; i++) {
	 System.out.println(xmlParser.fieldname[i] + "："
	 + xmlParser.fieldvalue[i]);
	 }
	 } else {
	 System.out.println("错误编码：" + xmlParser.recon_status);
	 System.out.println("错误描述：" + xmlParser.recon_error);
	 }
	 } else {
	 System.out.println("解析xml出错：" + xmlParser.parse_error);
	 }
	 } catch (Exception e) {
	 e.printStackTrace();
	 }
	 try {
	 is.close();
	 } catch (IOException e) {
	 e.printStackTrace();
	 }
	 }*/
}