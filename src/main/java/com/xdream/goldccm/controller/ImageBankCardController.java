package com.xdream.goldccm.controller;

import com.xdream.JsonUtils;
import com.xdream.goldccm.service.IBankCardDiscernService;
import com.xdream.goldccm.service.IPictureService;
import com.xdream.goldccm.third.ImageConfig;
import com.xdream.goldccm.util.ResponseBase;
import com.xdream.goldccm.util.ResponseData;
import com.xdream.goldccm.util.ResponseObj;
import com.xdream.kernel.util.ResponseUtil;
import com.xdream.uaas.server.tools.ResponseBank;
import com.xdream.uaas.server.tools.ResponseJson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
//LL
@SuppressWarnings("serial")
@Controller
@RequestMapping(value = "/goldccm/bankimage")
public class ImageBankCardController {
	
	private IPictureService pictureService;

	@Resource(name = "bankCardDiscernService")
	private IBankCardDiscernService bankCardDiscernService;


	/**
	 * 银行卡,身份证，图片
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainData", method = RequestMethod.POST)
	public void upBankNumber(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		System.out.println("开始上传");
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");// 1:银行卡 2.身份证 3.普通图片
		String ad=request.getParameter("ad");//广告
		boolean isSucc = true;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multipartRequest.getFileNames();
		ResponseObj obj = new ResponseObj();
		ResponseBase base = new ResponseBase();
		ResponseData data = new ResponseData();
		MultipartFile myfile = null;
		String json = "";
		String realFileName = "";
		String path = "user" + File.separator + userId;
		
		
		//String userPath=
		File files = null;
		try {
			if (iter.hasNext()) {
				String key = iter.next();
				myfile = multipartRequest.getFile(key);

				if (myfile.isEmpty()) {
		
				} else {
				
					String originalFilename = myfile.getOriginalFilename();
					if (!originalFilename.matches(".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$")) {
						isSucc = false;
					} else {
						String realPath =null;
						
						if(!StringUtils.isBlank(ad)){
							path = "ad";
							//realPath = ImageConfig.imageSaveDir + path;//paramsService.findByParamName("imageSaveDir").getParamText()  + path;
						}
//						else{
//							realPath = //paramsService.findByParamName("imageSaveDir").getParamText() +path;
//						}
					realPath = ImageConfig.imageSaveDir + path;
				//测试	realPath ="E://imagecs";
					  
						
                   	
						File file = new File(realPath);
                      // 	File file = new File(realPath);//测试
						if (!file.exists()) {
							file.mkdirs();
						}
						// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的

						String newFileName = System.currentTimeMillis() + "";
						int index = originalFilename.lastIndexOf(".");

						String fileNameType = originalFilename.substring(index,originalFilename.length());
						realFileName = newFileName + fileNameType;
						files = new File(realPath, realFileName);
						FileUtils.copyInputStreamToFile(myfile.getInputStream(), files);
						
					}

				}

			}
			realFileName = path + File.separator + realFileName;
			if (isSucc) {							
				if ("1".equals(type)) {
					// 银行卡
					System.out.println("进入银行卡.............");
					String bankCardData = bankCardDiscernService.getBankCardDiscern(files.getAbsolutePath());
					System.out.println("bankCardData:" + bankCardData);
					ResponseBank bankObj = (ResponseBank) JsonUtils.toObj(bankCardData, ResponseBank.class);
					if (bankObj.getCardNumber() != null) {
						data.setBankCardNo(bankObj.getCardNumber());
						data.setBank(bankObj.getBankName());
						base.setSign("success");
						base.setDesc("提交成功");
						data.setImageFileName(realFileName);
						obj.setData(data);
						obj.setVerify(base);

					} else {
						base.setSign("ocr");
						base.setDesc("识别失败");
						data.setImageFileName(realFileName);
						obj.setData(data);
						obj.setVerify(base);
					}
				}

				//身份证
				if ("2".equals(type)) {
					System.out.println("进入身份证识别："+files.getAbsolutePath());
					String pictureData = pictureService.getCardData(files.getAbsolutePath());
					System.out.println("idNo:"+pictureData);
					ResponseJson pictureObj = (ResponseJson) JsonUtils.toObj(pictureData, ResponseJson.class);
					if(pictureObj.getCards().size()>0){
						if (pictureObj.getCards().get(0).getId_card_number() != null) {
							base.setSign("success");
							base.setDesc("提交成功");
							obj.setVerify(base);
							data.setImageFileName(realFileName);
							data.setIdNo(pictureObj.getCards().get(0).getId_card_number());
							data.setName(pictureObj.getCards().get(0).getName());
							data.setAddress(pictureObj.getCards().get(0).getAddress());
							obj.setData(data);
						} else {
							base.setSign("ocr");
							base.setDesc("识别失败");
							data.setImageFileName(realFileName);
							obj.setData(data);
							obj.setVerify(base);
						}
					}else{						
						base.setSign("ocr");
						base.setDesc("识别失败");
						data.setImageFileName(realFileName);
						obj.setData(data);
						obj.setVerify(base);
					}
					
				
				}
				
				//普通图片
				if("3".equals(type)){
					System.out.println("进入普通图片........");
            		base.setSign("success");
					base.setDesc("提交成功");
					obj.setVerify(base);
					data.setImageFileName(realFileName);
					obj.setData(data);
				}
               
			} else {
				base.setSign("fail");
				base.setDesc("提交失败");
				obj.setVerify(base);
			}

		} catch (Exception e) {
			e.printStackTrace();
			base.setSign("fail");
			base.setDesc("提交失败");
			obj.setVerify(base);
		} finally {
			json = JsonUtils.toJson(obj);
			System.out.println("json:"+json);
			ResponseUtil.responseJson(response, json);
		}

	}
     

}
