package pe.kr.ddakker.was.server.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLConnection;
import java.util.Date;

import pe.kr.ddakker.was.server.ConfigHelper;
import pe.kr.ddakker.was.server.HttpServer;
import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;
import pe.kr.ddakker.was.server.view.html.DefaultPageHelper;
import pe.kr.ddakker.was.support.util.FileUtils;

/**
 * 브라우저에 출력하는 부분을 담당한다.
 * @author ddakker 2015. 10. 30.
 */
public class PageView {
	private String rootpath;
	public PageView(String rootpath) {
		this.rootpath = rootpath;
	}

	public void browserPrint(HttpServletRequest request, HttpServletResponse response, int httpStatusCode, OutputStream ops, boolean isServlet) throws IOException {
		browserPrint(request, response, httpStatusCode, ops, isServlet, null);
	}
	public void browserPrint(HttpServletRequest request, HttpServletResponse response, int httpStatusCode, OutputStream ops, boolean isServlet, File viewStaticFile) throws IOException {
		settingResponse(request, response, httpStatusCode, isServlet, viewStaticFile);
		pageView(request, response, httpStatusCode, ops);
	}
	
	/**
	 * 응답 정보 뷰어
	 * @param request
	 * @param response
	 * @param httpStatusCode
	 * @param ops
	 * @throws IOException
	 * @auther ddakker 2015. 10. 30.
	 */
	private void pageView(HttpServletRequest request, HttpServletResponse response, int httpStatusCode, OutputStream ops) throws IOException {
		
		String httpStatusMessage = "";
		if (httpStatusCode == HttpServletResponse.SC_OK) {
			httpStatusMessage = "200 OK";
		} else if (httpStatusCode == HttpServletResponse.SC_FORBIDDEN) {
			httpStatusMessage = "403 Forbidden";
		} else if (httpStatusCode == HttpServletResponse.SC_NOT_FOUND) {
			httpStatusMessage = "404 Not Found";
		} else if (httpStatusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
			httpStatusMessage = "500 Internal Server Error";
		}
		
		Writer out = null;
		try {
			out = new OutputStreamWriter(ops);
			Date now = new Date();
			
			StringBuffer headerSb = new StringBuffer();
			headerSb.append("HTTP/1.0 " + httpStatusCode + " " + httpStatusMessage + "\r\n");
			headerSb.append("Date: " + now + "\r\n");
			headerSb.append("Server: " + HttpServer.SERVER_NAME + "\r\n");
			headerSb.append("Content-length: " + response.getContentLength() + "\r\n");
			headerSb.append("Content-type: " + response.getContentType() + "\r\n\r\n");
			
			out.write(headerSb.toString());
			out.write(response.getWriter().getData());
			out.flush();
			
		} catch (Exception e) {
			throw e;
		} finally {
			try { out.close(); } catch (IOException ex) { }
		}
	}
	
	/**
	 * 응답 부분 설정
	 * @param request
	 * @param response
	 * @param httpStatusCode
	 * @param viewStaticFilepath
	 * @throws IOException
	 * @auther ddakker 2015. 10. 30.
	 */
	private void settingResponse(HttpServletRequest request, HttpServletResponse response, int httpStatusCode, boolean isServlet, File viewStaticFile) throws IOException {
		
		String contentBody = null;
		String contentType = "text/html; charset=UTF8";
		if (httpStatusCode == HttpServletResponse.SC_OK) {
			if (!isServlet) {
				contentBody = FileUtils.getFileContent(viewStaticFile);
				response.getWriter().print(contentBody);
			}
		} else {
			response.getWriter().clear();
			String defaultBody 	= "";
			String pagPath 		= "";
			if (httpStatusCode == HttpServletResponse.SC_NOT_FOUND) {
				pagPath 	= ConfigHelper.ERROR_PAGE_404;
				defaultBody = DefaultPageHelper.get404Page();
			} else if (httpStatusCode == HttpServletResponse.SC_FORBIDDEN) {
				pagPath 	= ConfigHelper.ERROR_PAGE_403;
				defaultBody = DefaultPageHelper.get403Page();
			} else if (httpStatusCode == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
				pagPath 	= ConfigHelper.ERROR_PAGE_500;
				defaultBody = DefaultPageHelper.get500Page();
			}
			
			if (pagPath != null) {
				File fileName = new File(pagPath);
				if (!fileName.exists()) fileName = new File(rootpath + pagPath);
				if (fileName.exists()) {
					contentType = URLConnection.getFileNameMap().getContentTypeFor(rootpath + pagPath);
					InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName));
					contentType += "; " + "charset=" + isr.getEncoding();
					contentBody = FileUtils.getFileContent(fileName);
				}
			} else {
				if (contentBody == null) {
					contentBody = defaultBody;
				}
			}
			response.getWriter().print(contentBody);
		}
		response.setContentType(contentType);
	}
	
}
