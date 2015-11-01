package pe.kr.ddakker.was.server.servlet.http;

import java.io.IOException;

import pe.kr.ddakker.was.server.write.PrintWriter;

public interface HttpServletResponse {
	public static final int SC_OK 						= 200;
	public static final int SC_FORBIDDEN 				= 403;
    public static final int SC_NOT_FOUND 				= 404;
    public static final int SC_INTERNAL_SERVER_ERROR 	= 500;
    
    public String getContentType();
    public void setContentType(String type);
    public int getContentLength();
	public PrintWriter getWriter() throws IOException;
}
