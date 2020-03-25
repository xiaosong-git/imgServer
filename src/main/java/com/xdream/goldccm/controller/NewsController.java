package com.xdream.goldccm.controller;

import com.xdream.JsonUtils;
import com.xdream.goldccm.third.ImageConfig;
import com.xdream.goldccm.util.ResponseBase;
import com.xdream.goldccm.util.ResponseData;
import com.xdream.goldccm.util.ResponseObj;
import com.xdream.kernel.util.ResponseUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;

@SuppressWarnings("serial")
@Controller
@RequestMapping(value = "/goldccm/news")
public class NewsController {
	/**
	 * 信用卡申请,新闻
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
	public void upCardNumber(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String type = request.getParameter("type");// news：新闻;creditApply:信用卡申请
	
		boolean isSucc = true;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multipartRequest.getFileNames();
		ResponseObj obj = new ResponseObj();
		ResponseBase base = new ResponseBase();
		ResponseData data = new ResponseData();
		MultipartFile myfile = null;
		String json = "";
		String realFileName = "";
		String realPath =null;
		File files = null;
		String path=null;
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
						if("news".equals(type)){
							path = "news";
							//realPath = ImageConfig.imageSaveDir + path;//paramsService.findByParamName("imageSaveDir").getParamText()  + path;
						}else if("creditApply".equals(type)){
							
							path = "creditApply";
						}else if("shareroom".equals(type)){
							path = "shareroom";
						} else{
							path = "creditLoan";
						}

						realPath = ImageConfig.imageSaveDir + path;
					  
						
                       System.out.println("realPath:"+realPath);
						
						File file = new File(realPath);
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
        		base.setSign("success");
				base.setDesc("提交成功");
				obj.setVerify(base);
				data.setImageFileName(realFileName);
				obj.setData(data);

               
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
