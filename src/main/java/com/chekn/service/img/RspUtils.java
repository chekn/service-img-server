package com.chekn.service.img;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class RspUtils {
	
	//模拟ajax请求标签
	public static final String SIMULATION_AJAX_REQUEST="simulationAjax";
	
	/**
	 * 编码url 异常用默认值
	 * @param url
	 * @param encode
	 * @param defExVal
	 * @return
	 */
	public static String encodeUrlExp(String url,String encode){
		String rev="url_encode_error";
		try {
			rev=URLEncoder.encode(url,encode);
		} catch (Exception e1) {}
		return rev;
	}
	
	/**
	 * 解码url 异常用默认值
	 * @param url
	 * @param encode
	 * @param defExVal
	 * @return
	 */
	public static String decodeUrlExp(String url,String encode){
		String rev="url_decode_error";
		try {
			rev=URLDecoder.decode(url,encode);
		} catch (Exception e1) {}
		return rev;
	}
	
	/**
	 * 判断请求类型
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request){
		Boolean acceptFlag=request.getHeader("accept").indexOf("application/json")>-1;
		Boolean _xRequestWithFlag=(request.getHeader("X-Requested-With")!=null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest")>-1);
		
		return acceptFlag||_xRequestWithFlag;
	}
	
	/**
	 * 响应用json
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 */
	public static void directWriteJson(HttpServletResponse response,String jsonStr) throws IOException{
		directWriteStr(response,"application/json",jsonStr);
	}
	
	/**
	 * 响应用文本
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 */
	public static void directWriteText(HttpServletResponse response,String textStr) throws IOException{
		directWriteStr(response,"text/html",textStr);
	}
	
	/**
	 * 响应字符
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 */
	protected static void directWriteStr(HttpServletResponse response, String mime, String str) throws IOException{
        response.setContentType(mime+"; charset=UTF-8");
        response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");		// Set standard HTTP/1.1 no-cache headers.
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");							// Set IE extended HTTP/1.1 no-cache headers.
        response.setHeader("Pragma", "no-cache");													// Set standard HTTP/1.0 no-cache header.
        PrintWriter pw= response.getWriter();
        pw.write(str);
        pw.flush();
        pw.close();
	}
	
	/**
	 * 响应字符
	 * @param response
	 * @param jsonStr
	 * @throws IOException
	 */
	protected static void directWriteStream(HttpServletResponse response, String mime, InputStream ins) throws IOException{
        response.setContentType(mime);
        response.setHeader("Cache-Control","no-store, max-age=0, no-cache, must-revalidate");		// Set standard HTTP/1.1 no-cache headers.
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");							// Set IE extended HTTP/1.1 no-cache headers.
        response.setHeader("Pragma", "no-cache");	
        OutputStream ops=response.getOutputStream();
        IOUtils.copy(ins, ops);
        ins.close();
        ops.close();
	}
	
	public static String getCurrentRequestURL(HttpServletRequest request){
		String serverPath = getServerPath(request);
		
		String uri=serverPath+request.getRequestURI();
		String parameter=request.getQueryString();
		return (parameter==null)?uri:(uri+"?"+parameter);
	}
	
	public static String getServerPath(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
	}

}
