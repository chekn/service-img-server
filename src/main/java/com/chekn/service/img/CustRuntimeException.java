package com.chekn.service.img;

import javax.servlet.http.HttpServletResponse;

public class CustRuntimeException extends RuntimeException {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1890750336889518793L;
	private HttpServletResponse response;

	public CustRuntimeException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CustRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}
	
	public CustRuntimeException(String message, HttpServletResponse response) {
		super(message);
		this.setResponse(response);
		// TODO Auto-generated constructor stub
	}
	
	public CustRuntimeException(String message, Throwable cause,
			HttpServletResponse response) {
		super(message, cause);
		this.setResponse(response);
		// TODO Auto-generated constructor stub
	}

	public CustRuntimeException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CustRuntimeException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CustRuntimeException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
