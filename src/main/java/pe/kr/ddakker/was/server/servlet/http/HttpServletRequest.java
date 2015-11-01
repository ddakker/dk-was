package pe.kr.ddakker.was.server.servlet.http;

import java.util.Enumeration;
import java.util.Map;

public interface HttpServletRequest {
	public String getProtocol();
	public String getRemoteAddr();
	public String getHeader(String key);
	public Enumeration<String> getHeaderNames();
	public String getRequestURI();
	public StringBuffer getRequestURL();
	public String getMethod();
	public String getServerName();
	public int getServerPort();
	public boolean isSecure();
	public String getParameter(String key);
	public String [] getParameterValues(String key);
	public Map<String, String[]> getParameterMap();
	
}
