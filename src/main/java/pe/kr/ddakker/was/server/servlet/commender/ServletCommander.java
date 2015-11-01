package pe.kr.ddakker.was.server.servlet.commender;

import java.lang.reflect.Constructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.server.exception.ServletException;
import pe.kr.ddakker.was.server.servlet.SimpleServlet;
import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;

public class ServletCommander {
	private static Logger log = LoggerFactory.getLogger(ServletCommander.class);
	
	private static final String APP_ROOT = "pe.kr.ddakker.was.app";
	
	public void get(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, Exception {
		exec(request, response);
	}
	
	/*public void post(HttpServletRequest request, HttpServletResponse response) {
		exec(request, response);
	}*/
	
	public void exec(HttpServletRequest request, HttpServletResponse response) throws ClassNotFoundException, ServletException, Exception {
		try {
			String uri = request.getRequestURI();

			String forName = "";
			if (uri.contains("?")) {
				forName = APP_ROOT + "." + uri.substring(1, uri.indexOf("?"));
			} else {
				forName = APP_ROOT + "." + uri.substring(1);
			}
			
			Class clazz = Class.forName(forName);
			Constructor <?> constructor = clazz.getConstructor();
			SimpleServlet servlet = (SimpleServlet) constructor.newInstance();
			
			try {
				servlet.service(request, response);
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} catch (ClassNotFoundException e) {
			throw e;	// 404 에러
		}  catch (ServletException e) {
			throw e;	// 500 에러
		} catch (Exception e) {
			throw e;
		}
	}
}
