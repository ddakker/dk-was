package pe.kr.ddakker.was.server.servlet.http.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;

public class HttpServletRequestWrapper implements HttpServletRequest {
	public static final int HTTP_DEFAULT_PORT = 80;
	
	protected Map<String, String> header = new HashMap<>();
	protected Map<String, String[]> parameterMap = new HashMap<>();
	
	protected String remoteAddr;
	protected String protocol;
	protected String requestURI;
	protected String method;
	protected String serverName;
	protected int serverPort;
	protected boolean isSecure = false;

	@Override
	public String getHeader(String key) {
		return header.get(key);
	}

	@Override
	public Enumeration<String> getHeaderNames() {
		return Collections.enumeration(header.keySet());
	}
	
	@Override
	public String getRequestURI() {
		return requestURI;
	}

	@Override
	public StringBuffer getRequestURL() {
		StringBuffer url = new StringBuffer();
		url.append("http");
		url.append(isSecure?"s":"");
		url.append("://");
		url.append(serverName);
		url.append(serverPort==HTTP_DEFAULT_PORT?"":":"+serverPort);
		url.append(requestURI);
		return url;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public int getServerPort() {
		return serverPort;
	}

	@Override
	public boolean isSecure() {
		return isSecure;
	}

	@Override
	public String getProtocol() {
		return protocol;
	}

	@Override
	public String getRemoteAddr() {
		return remoteAddr;
	}

	@Override
	public String getParameter(String key) {
		String value [] = parameterMap.get(key);
		return (value!=null&&value.length==1)?value[0]:null;
	}
	
	@Override
	public String [] getParameterValues(String key) {
		return parameterMap.get(key);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return parameterMap;
	}
}
