package com.xdream.goldccm.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 输入输出流工具类
 * @author y1iag
 *
 */
public class IoStreamUtils {
	
	private IoStreamUtils(){
	}
	/**
	 * 将输入流中内容传输到输出流
	 * @param src 输入流
	 * @param dest 输出流
	 * @throws IOException
	 */
	public static long copy(InputStream src,OutputStream dest,int buffuerSize) throws IOException{
		
		BufferedInputStream biStream=null;
		BufferedOutputStream boStream=null;
		if( !(src instanceof BufferedInputStream) ){
			biStream=new BufferedInputStream(src);
		} else {
			biStream=(BufferedInputStream)src;
		}
		if( !(dest instanceof BufferedOutputStream) ){
			boStream=new BufferedOutputStream(dest);
		}else {
			boStream=(BufferedOutputStream)dest;
		}
		long totalBytes=0;
		byte[] buffer=new byte[buffuerSize];
		int realRead;
		while((realRead=biStream.read(buffer, 0, buffer.length))!=-1){
			 
			 boStream.write(buffer, 0, realRead);
			 totalBytes+=realRead;
		}
		boStream.flush();
		return totalBytes;
	}
	/**
	 * 缓冲设置
	 * @author y1iag
	 *
	 */
	public static class Buffer{
		public static int min=512;
		public static int middle=2048;
		public static int max=4096;
	}
}
