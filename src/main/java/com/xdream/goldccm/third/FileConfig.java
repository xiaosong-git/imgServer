package com.xdream.goldccm.third;
/**
 *文件上传地址
 *update by cwf  2019/8/29 9:25
 */
public class FileConfig {

	/**
	 *
	 * @author cwf
	 * @date 2019/8/29 9:27
	 */



	/**根据来源分配地址前缀
	 */
	// /usr/java/
	public static String prefix;
	// /usr/java/img/
	public static String prefixImg;
	//来源于app的文件存放前缀 如/usr/java/usr/app
	public static String prefixApp;
	//来源于运营管理平台的文件存放前缀 如/java/usr/Operation
	public static String prefixOperation;
	//来源于公司管理平台的文件存放前缀 如/java/usr/Company
	public static String prefixCompany;
	//来源于上位机的文件存放前缀 如/java/usr/Upper
	public static String prefixUpper;

	/**根据文件类型分配地址后缀
	 */
	//图片上传地址如/image
	public static String suffixImage;
	//文档上传地址如/Document
	public static String suffixDocument;
	//视频上传地址如/video
	public static String suffixVideo;
	//音频上传地址如/audio
	public static String suffixAudio;
	//其他文件上传地址如/other
	public static String suffixOther;

}
