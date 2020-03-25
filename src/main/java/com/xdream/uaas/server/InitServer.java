package com.xdream.uaas.server;

import org.apache.log4j.Logger;

import com.xdream.kernel.ServerSupport;

public class InitServer extends ServerSupport{

	Logger logger = Logger.getLogger(getClass());
//	MemClient systemCache;
//	ISystemParamService systemParamService;
	@Override
	public void doStart() throws Exception {
//		//System.out.println("aaaaaaaaaa");
//		systemCache = (MemClient)SpringContextUtil.getBean("systemCache");
//		systemParamService = (ISystemParamService)SpringContextUtil.getBean("systemParamService");
//		System.out.println("aaa:"+systemParamService.findProductImageUrl());
		
	}

	@Override
	public void doStop() throws Exception {
		//System.out.println("bbbbbbbbbbbbbbb");
		
	}
}
