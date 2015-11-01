package pe.kr.ddakker.was.server.servlet.http.wrapper;


import java.util.HashMap;
import java.util.Map;

import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;
import pe.kr.ddakker.was.server.write.PrintWriter;

public class HttpServletResponseWrapper implements HttpServletResponse {
	public static final String CONTENT_TYPE 	= "Content-type";
	public static final String CONTENT_LENGTH 	= "Content-length";
	
	private Map<String, String> header = null;
	private PrintWriter body = null;
	
	public HttpServletResponseWrapper() {
		header = new HashMap<>();
		body = new PrintWriter();
	}

	@Override
	public PrintWriter getWriter() {
		return body;
	}

	@Override
	public String getContentType() {
		return header.get(CONTENT_TYPE);
	}

	@Override
	public void setContentType(String type) {
		header.put(CONTENT_TYPE, type);
	}

	@Override
	public int getContentLength() {
		return getWriter().getData().getBytes().length;
	}

}
