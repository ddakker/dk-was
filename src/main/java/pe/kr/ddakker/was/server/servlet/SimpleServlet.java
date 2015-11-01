package pe.kr.ddakker.was.server.servlet;

import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;

public interface SimpleServlet {
	public static final String METHOD_DELETE 	= "DELETE";
	public static final String METHOD_HEAD 		= "HEAD";
	public static final String METHOD_GET 		= "GET";
	public static final String METHOD_OPTIONS 	= "OPTIONS";
	public static final String METHOD_POST 		= "POST";
	public static final String METHOD_PUT 		= "PUT";
	public static final String METHOD_TRACE 	= "TRACE";
    
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception;
}