package com.xdream.goldccm.third;

import com.xdream.goldccm.util.FaceModuleUtil;
import com.xdream.goldccm.util.ParamDef;
import com.xdream.kernel.ServerSupport;

public class ThirdServer extends ServerSupport{

	@Override
	public void doStart() throws Exception {
		//load zm config
		IdNoConfig.api_key = ParamDef.findIdNoByName("api_key");
		IdNoConfig.api_secret = ParamDef.findIdNoByName("api_secret");
		IdNoConfig.url = ParamDef.findIdNoByName("url");
		
		//load kft config
		BankCardConfig.key = ParamDef.findBankCardByName("key");
		BankCardConfig.secret = ParamDef.findBankCardByName("secret");
		BankCardConfig.url = ParamDef.findBankCardByName("url");	
		//load image.config
		ImageConfig.imageSaveDir = ParamDef.findImgByName("imageSaveDir");
		//load bankConfig
		BankConfig.key=ParamDef.findBankByName("key");
		BankConfig.secret=ParamDef.findBankByName("secret");
		BankConfig.url=ParamDef.findBankByName("url");
		//loadDB40Config
		DB40Config.DB40Dir=ParamDef.findDB40ByName("DB40Dir");
		//load fileConfig
		FileConfig.prefix=ParamDef.findFileByName("prefix");
		FileConfig.prefixImg=ParamDef.findFileByName("prefixImg");
		FileConfig.prefixApp=ParamDef.findFileByName("prefixApp");
		FileConfig.prefixOperation=ParamDef.findFileByName("prefixOperation");
		FileConfig.prefixCompany=ParamDef.findFileByName("prefixCompany");
		FileConfig.prefixUpper=ParamDef.findFileByName("prefixUpper");
		FileConfig.suffixImage=ParamDef.findFileByName("suffixImage");
		FileConfig.suffixDocument=ParamDef.findFileByName("suffixDocument");
		FileConfig.suffixVideo=ParamDef.findFileByName("suffixVideo");
		FileConfig.suffixAudio=ParamDef.findFileByName("suffixAudio");
		FileConfig.suffixOther=ParamDef.findFileByName("suffixOther");
		//print
		System.out.println("ImageConfig.imageSaveDir:"+ImageConfig.imageSaveDir);
		System.out.println("db40configpath:"+DB40Config.DB40Dir);

		System.out.println("HJ faceEngine start");
//		System.load(this.getClass().getClassLoader().getResource("DB40/FreeImage.dll").getPath());
		String osName = System.getProperty("os.name");
		try {
			System.out.println(osName);

		if (osName.contains("Linux")) {
			System.out.println(osName);
			System.load(DB40Config.DB40Dir+"/libJavaJNI.so");
//			System.load(DB40Config.DB40Dir+"/libHJFacePos.so");
//			System.load(DB40Config.DB40Dir+"/libHJFaceDetect.so");
//			System.load(DB40Config.DB40Dir+"/libHJFaceIdentify.so");
//			System.load(DB40Config.DB40Dir+"/libHJFaceEngine.so");

		}else{
			System.load(DB40Config.DB40Dir + "/FreeImage.dll");
			System.load(DB40Config.DB40Dir + "/HJFacePos.dll");
			System.load(DB40Config.DB40Dir + "/HJFaceDetect.dll");
			System.load(DB40Config.DB40Dir + "/HJFaceIdentify.dll");
			System.load(DB40Config.DB40Dir + "/HJFaceLive.dll");
			System.load(DB40Config.DB40Dir + "/HJFaceEngine.dll");
			System.load(DB40Config.DB40Dir + "/JavaJNI.dll");
		}
		FaceModuleUtil.initEngine(1);
		}catch (Throwable e){
			e.printStackTrace();
		}
		/**load face linux
		 */
//		System.load(DB40Config.DB40Dir+"/libJavaJNI.so");
//		System.load(DB40Config.DB40Dir+"/libHJFacePos.so");
//		System.load(DB40Config.DB40Dir+"/libHJFaceDetect.so");
//		System.load(DB40Config.DB40Dir+"/libHJFaceIdentify.so");
//		System.load(DB40Config.DB40Dir+"/libHJFaceEngine.so");

		System.out.println("HJ faceEngine end");
//		FaceModuleUtil.initEngine(1);
		System.out.println("HJ faceEngine init");
//		System.out.println("eyeCross：70，rollAngl：35，confidence：80");
//		FaceModuleUtil.initDetectEngine(1, 45, Constant.TEMPLATE_ROLL_ANGL, 80);
//		FaceModuleUtil.initFeatureEngine(1);


	}

	@Override
	public void doStop() throws Exception {
		//System.out.println("bbbbbbbbbbbbbbb");

	}
}
