package com.chekn.service.img;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class C extends HttpServlet {
	
	String imgRepo;
	RedisDM rdm;

	@Override
	public void init() throws ServletException {
		super.init();
		Map<String,String> cfs=Resources.init("redis");
		imgRepo=cfs.get("img-repo").contains(File.separator) ? cfs.get("img-repo") : ("c:\\"+cfs.get("img-repo").replace("/", File.separator));
		rdm=new RedisDM(cfs.get("host"), Integer.parseInt(cfs.get("port")), cfs.get("auth"));
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url=request.getParameter("url");
		System.out.println(url);
		if(StringUtils.isBlank(url))
			throw new CustRuntimeException("your url is not fit rule", response);
		

		//String dir=this.getServletContext().getRealPath("/img-repo")+"/";
		String uniqueCode=UUID.randomUUID().toString();
		String fn=null;
		rdm.urlFlowQueue(url.trim(), uniqueCode);
		try{fn=rdm.urlLocalGet(url.trim(), uniqueCode, imgRepo);}catch(Exception e) {
			throw new CustRuntimeException(e.getMessage(), response);
		}
		
		String[] fnp=fn.split("#");
		RspUtils.directWriteStream(response, fnp[1].replace("-", "/"), new FileInputStream(new File(imgRepo+fn)));
	}
	
	protected void service(HttpServletRequest req, HttpServletResponse resp)
	        throws ServletException, IOException {
		try{
			super.service(req, resp);
		}catch (Throwable e) {

			if(e instanceof CustRuntimeException) {
				handleException((CustRuntimeException)e);
			} else {
				throw e;
			}
			
		}
	}

	
	public void handleException(CustRuntimeException e) {
		try {
			RspUtils.directWriteText(e.getResponse(), e.getMessage());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
}
