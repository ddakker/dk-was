package pe.kr.ddakker.was.server;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.URLConnection;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.server.exception.ServletException;
import pe.kr.ddakker.was.server.servlet.SimpleServlet;
import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;
import pe.kr.ddakker.was.server.servlet.http.wrapper.HttpServletRequestSetWrapper;
import pe.kr.ddakker.was.server.servlet.http.wrapper.HttpServletResponseSetWrapper;
import pe.kr.ddakker.was.server.servlet.request.security.SecurityHelper;
import pe.kr.ddakker.was.server.view.PageView;

public class RequestProcessor implements Runnable {
	private static Logger log = LoggerFactory.getLogger(RequestProcessor.class);
	
	private static final String SERVLET_COMMANDER = "pe.kr.ddakker.was.server.servlet.commender.ServletCommander";
	
	public static final byte CR = '\r';
	public static final byte LF = '\n';
	
    private File rootDirectory;
    private String indexFileName = "index.html";
    private Socket connection;

    public RequestProcessor(File rootDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException(
                    "rootDirectory must be a directory, not a file");
        }
        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException ex) {
        }
        this.rootDirectory = rootDirectory;
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.connection = connection;
    }

    @Override
	public void run() {
		OutputStream ops 	= null;
		try {
			ops 	= new BufferedOutputStream(connection.getOutputStream());
			
			HttpServletRequest request 		= getRequest();
			HttpServletResponse response 	= getResponse();
			PageView view 					= new PageView(rootDirectory.getPath());

			log.info("===== PROTOCAL \t{}", request.getProtocol());
			log.info("===== METHOD \t{}", request.getMethod());
			log.info("===== URI \t{}", request.getRequestURI());
			log.info("===== IP \t{}", request.getRemoteAddr());
			log.info("===== Host \t{}", request.getServerName());


			String method = request.getMethod();
			int httpStatusCode = -1;
			
			if (SecurityHelper.isSecurityDanger(request.getRequestURI())) {
				log.error("보안에 정책 오류입니다. {}", request.getRequestURI());
				view.browserPrint(request, response, HttpServletResponse.SC_FORBIDDEN, ops, true);
			} else {
				if (method.equals(SimpleServlet.METHOD_GET)) {
					
					String host		= request.getServerName();
					String uri 		= request.getRequestURI();
					
					String staticPath = "";
					if (!host.equals("localhost")) 	staticPath += "/" + host;
	                if (uri.equals("/")) 			staticPath += "/" + indexFileName;
	                else							staticPath += uri;
	                
	                
	                String contentType = URLConnection.getFileNameMap().getContentTypeFor(staticPath);
	                File viewStaticFilepath = new File(rootDirectory, staticPath.substring(1, staticPath.length()));
	                if (viewStaticFilepath.exists()) {
	                	view.browserPrint(request, response, HttpServletResponse.SC_OK, ops, false, viewStaticFilepath);
	                } else {
	                	Class clazz = Class.forName(SERVLET_COMMANDER);
	                	Object commander = clazz.newInstance();
	                	Class[] methodParamClass = new Class[] {HttpServletRequest.class, HttpServletResponse.class};
	                	Method classMethod = clazz.getMethod(request.getMethod().toLowerCase(), methodParamClass);
	                	
	                	try {
	                		classMethod.invoke(commander, new Object[]{request, response});
	                		httpStatusCode = HttpServletResponse.SC_OK;
	                	} catch (InvocationTargetException e) {
	                		if (e.getCause() instanceof ClassNotFoundException) {
	                			log.error("페이지를 찾을 수 없습니다.", e.getCause());
	                			httpStatusCode = HttpServletResponse.SC_NOT_FOUND;
	                		} else if(e.getCause() instanceof ServletException) {
	                			log.error("비지니스에서 오류가 발생하였습니다.", e.getCause());
	                			httpStatusCode = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
	                		}
	                	}
	                	view.browserPrint(request, response, httpStatusCode, ops, true);
	                }
					
				} else {
					// 우선 GET만...
				}
			}
			
		} catch (Exception e) {
			log.error("Error {}", connection.getRemoteSocketAddress(), e);
		} finally {
			try { connection.close(); } catch (IOException ex) { }
			try { ops.close(); } catch (IOException ex) { }
		}
	}

	private void sendHeader(Writer out, String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP test 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }
    
    
	private HttpServletRequest getRequest() throws IOException {
		HttpServletRequestSetWrapper requestSetWrapper = new HttpServletRequestSetWrapper();
		requestSetWrapper.setRemoteAddr(connection.getRemoteSocketAddress());

		InputStream in = connection.getInputStream();
		int oneInt = -1;
		byte oldByte = (byte) -1;
		StringBuilder sb = new StringBuilder();
		int lineNumber = 0;
		while (-1 != (oneInt = in.read())) {
			byte thisByte = (byte) oneInt;
			if (thisByte == LF && oldByte == CR) {
				String oneLine = sb.substring(0, sb.length() - 1);
				lineNumber++;

				if (lineNumber == 1) {
					requestSetWrapper.setProtocal(oneLine);
				} else {
					requestSetWrapper.setMessageHeader(oneLine);
				}
				if (oneLine.length() <= 0) {
					break;
				}
				sb.setLength(0);
			} else {
				sb.append((char) thisByte);
			}
			oldByte = (byte) oneInt;
		}
		return requestSetWrapper;
	}
	
	private HttpServletResponse getResponse() {
		HttpServletResponse response = new HttpServletResponseSetWrapper();
		return response;
	}
}