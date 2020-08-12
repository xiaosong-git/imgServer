package com.xdream.goldccm.controller;

import com.hj.jni.bean.HJFaceModel;
import com.xdream.JsonUtils;
import com.xdream.goldccm.service.IBankCardDiscernService;
import com.xdream.goldccm.service.IPictureService;
import com.xdream.goldccm.third.ImageConfig;
import com.xdream.goldccm.util.*;
import com.xdream.kernel.util.ResponseUtil;
import com.xdream.uaas.server.tools.ResponseBank;
import com.xdream.uaas.server.tools.ResponseJson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
@Controller
@RequestMapping(value = "/goldccm/image")
public class ImageController {
	@Resource(name = "pictureService")
	private IPictureService pictureService;

	@Resource(name ="bankCardDiscernService")
	private IBankCardDiscernService bankCardDiscernService;
	private static Logger logger= Logger.getLogger(ImageController.class);

	/**
	 * 图片上传（多个）
	 * 
	 * @param myfiles
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadMore")
	public void uploadMore(@RequestParam MultipartFile[] myfiles,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String userId = request.getParameter("userId");
		boolean isSucc = true;

		ResponseObj obj = new ResponseObj();
		ResponseBase base = new ResponseBase();
		String json = "";
		try {
			for (MultipartFile myfile : myfiles) {
				if (myfile.isEmpty()) {
					System.out.println("文件未上传");
				} else {
					System.out.println("文件长度: " + myfile.getSize());
					System.out.println("文件类型: " + myfile.getContentType());
					System.out.println("文件名称: " + myfile.getName());
					System.out.println("文件原名: " + myfile.getOriginalFilename());
					String originalFilename = myfile.getOriginalFilename();
					if (!originalFilename
							.matches(".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$")) {
						isSucc = false;
						break;
					} else {
						String  realPath = ImageConfig.imageSaveDir + File.separator + userId;//paramsService.findByParamName("imageSaveDir").getParamText() + File.separator + userId;
						
						File file = new File(realPath);
						if (!file.exists()) {
							file.mkdirs();
						}
						// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的

						String newFileName = myfile.getOriginalFilename();
						int index = originalFilename.lastIndexOf(".");
						if (index < 0) {

						}
						String fileNameType = originalFilename.substring(index);
						FileUtils.copyInputStreamToFile(
								myfile.getInputStream(), new File(realPath,
										newFileName + fileNameType));

						System.out.println("上传成功");
					}

				}

			}

			if (isSucc) {
				base.setSign("success");
				base.setDesc("提交成功");
//				ResponseData data = new ResponseData();
//				data.setImageFileName(realFileName);
//				obj.setData(data);
			} else {
				base.setSign("fail");
				base.setDesc("提交失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			base.setSign("fail");
			base.setDesc("提交失败");
		} finally {
			obj.setVerify(base);
			json = JsonUtils.toJson(obj);
			System.out.println(json);
			ResponseUtil.responseJson(response, json);
		}

	}

	/**
	 * 图片上传（单个）
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/uploadSing", method = RequestMethod.POST)
	public void uploadSing(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String userId = request.getParameter("userId");
		// ServletInputStream api_key= request.getInputStream();

		System.out.println("userId:" + userId);

		boolean isSucc = true;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multipartRequest.getFileNames();
		ResponseObj obj = new ResponseObj();
		ResponseBase base = new ResponseBase();
		MultipartFile myfile = null;
		String json = "";
		String realFileName = "";
		try {
			if (iter.hasNext()) {
				String key = iter.next();
				myfile = multipartRequest.getFile(key);

				if (myfile.isEmpty()) {
					System.out.println("文件未上传");
				} else {
					System.out.println("文件长度: " + myfile.getSize());
					System.out.println("文件类型: " + myfile.getContentType());
					System.out.println("文件名称: " + myfile.getName());
					System.out.println("文件原名: " + myfile.getOriginalFilename());
					String originalFilename = myfile.getOriginalFilename();
					if (!originalFilename
							.matches(".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$")) {
						isSucc = false;
					} else {

						String realPath =ImageConfig.imageSaveDir +File.separator + userId; //paramsService.findByParamName("imageSaveDir").getParamText()+ File.separator + userId;

						File file = new File(realPath);
						if (!file.exists()) {
							file.mkdirs();
						}
						// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的

						String newFileName = System.currentTimeMillis() + "";
						int index = originalFilename.lastIndexOf(".");

						String fileNameType = originalFilename.substring(index,
								originalFilename.length());
						realFileName = newFileName + fileNameType;
						File files = new File(realPath, realFileName);
						FileUtils.copyInputStreamToFile(
								myfile.getInputStream(), files);
						System.out.println("路径：" + files.getAbsolutePath());
						System.out.println("上传成功");
					}

				}

			}

			if (isSucc) {

				base.setSign("success");
				base.setDesc("提交成功");
				obj.setVerify(base);
				ResponseData data = new ResponseData();
				data.setImageFileName(realFileName);
				obj.setData(data);
			} else {
				base.setSign("fail");
				base.setDesc("提交失败");
			}

		} catch (Exception e) {
			e.printStackTrace();
			base.setSign("fail");
			base.setDesc("提交失败");
			obj.setVerify(base);
		} finally {
			json = JsonUtils.toJson(obj);
			System.out.println("jswon:" + json);
			ResponseUtil.responseJson(response, json);
		}

	}

	/**
	 * 银行图标
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/gainBankIcon", method = RequestMethod.POST)
	public void upCardNumber(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String iconName = request.getParameter("iconName");
	
		boolean isSucc = true;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Iterator<String> iter = multipartRequest.getFileNames();
		ResponseObj obj = new ResponseObj();
		ResponseBase base = new ResponseBase();
		ResponseData data = new ResponseData();
		MultipartFile myfile = null;
		String json = "";
		String realFileName = "";
		File files = null;
		try {
			if (iter.hasNext()) {
				String key = iter.next();
				myfile = multipartRequest.getFile(key);

				if (myfile.isEmpty()) {

				} else {
					String originalFilename = myfile.getOriginalFilename();
					if (!originalFilename
							.matches(".+(.JPEG|.jpeg|.JPG|.jpg|.GIF|.gif|.BMP|.bmp|.PNG|.png)$")) {
						isSucc = false;
					} else {

						String realPath =ImageConfig.imageSaveDir +"bank"; //paramsService.findByParamName("imageSaveDir").getParamText()+ File.separator + userId;
						File file = new File(realPath);
						if (!file.exists()) {
							file.mkdirs();
						}
						// 这里不必处理IO流关闭的问题，因为FileUtils.copyInputStreamToFile()方法内部会自动把用到的IO流关掉，我是看它的源码才知道的

						String newFileName =System.currentTimeMillis() + "";
						int index = originalFilename.lastIndexOf(".");
						String fileNameType = originalFilename.substring(index,originalFilename.length());
						realFileName = newFileName + fileNameType;
						files = new File(realPath, realFileName);
						FileUtils.copyInputStreamToFile(myfile.getInputStream(), files);
					}

				}

			}

			if (isSucc) {
				base.setSign("success");
				base.setDesc("提交成功");
				data.setImageFileName(realFileName);
				obj.setVerify(base);	
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
			System.out.println("jswon:" + json);
			ResponseUtil.responseJson(response, json);
		}

	}

	/**
	 * 银行卡,身份证，图片、人脸图片
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 *  update by cwf  2019/9/6 15:26 Reason: 增加一个type=4 其他图片
	 */
	@RequestMapping(value = "/gainData", method = RequestMethod.POST)
	public void upBankNumber(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String userId = request.getParameter("userId");
		String type = request.getParameter("type");// 1:银行卡 2.身份证 3.人脸图片 4.其他图片
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
				if ("1".equals(type)) {
					// 银行卡
					System.out.println("进入银行卡.............");
					String bankCardData = bankCardDiscernService.getBankCardDiscern(files.getAbsolutePath());
							//bankcardService.getBankCardData(files.getAbsolutePath());
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
				//人脸图片
				if("3".equals(type)){
					System.out.println("进入人脸图片........");

					//获取文件路径
					logger.info("文件路径:"+ImageConfig.imageSaveDir+realFileName);
					// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
					String pic64_1 = FilesUtils.ImageToBase64ByLocal(ImageConfig.imageSaveDir+realFileName);
                    RetMsg retMsg = FaceModuleUtil.genHjFaceModule(pic64_1);
//					//TODO 将人脸进行裁剪 裁剪成功后存数据库字段
					Object content = retMsg.getContent();
					if(content !=null){
						List<HJFaceModel> hjface=(List<HJFaceModel>)content;
						if(hjface.size()>0){
							int leftEyeX = hjface.get(0).getLeftEyeX();
							int rightEyeX = hjface.get(0).getRightEyeX();
							logger.info(realFileName+"——人脸眼间距为："+(rightEyeX-leftEyeX));
						}
					}
//            		//调用人像识别，判断是否符合
					if( retMsg.getResult_code()==0){
						base.setSign("success");
						base.setDesc("提交成功");
						obj.setVerify(base);
						data.setImageFileName(realFileName);
						obj.setData(data);
						//失败移除文件
					}else {
						logger.info(userId+"提交人脸失败原因："+retMsg.getResult_desc());
						base.setSign("fail");
						base.setDesc(retMsg.getResult_desc());
						obj.setVerify(base);
						data.setImageFileName(null);
						obj.setData(data);
//						files.delete();
//						logger.info("删除非人像："+realFileName);
					}

				}
				//普通图片
				if("4".equals(type)){
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
