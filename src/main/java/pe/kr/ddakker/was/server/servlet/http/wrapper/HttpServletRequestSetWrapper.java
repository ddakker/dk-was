package pe.kr.ddakker.was.server.servlet.http.wrapper;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.support.util.StringUtils;

/**
 * Requeset 요청 정보를 Setting 한다.
 * @author ddakker 2015. 10. 30.
 */
public class HttpServletRequestSetWrapper extends HttpServletRequestWrapper {
	private static Logger log = LoggerFactory.getLogger(HttpServletRequestSetWrapper.class);
	
	/**
	 * 헤더 정보를 가공 및 셋팅한다.
	 * @param key
	 * @param value
	 * @auther ddakker 2015. 10. 30.
	 */
	public void setMessageHeader(String messageHeaderStr) {
		String messageHeaderStrArr [] = messageHeaderStr.split(": ");
		if (messageHeaderStrArr != null && messageHeaderStrArr.length == 2) {
			String key 		= messageHeaderStrArr[0];
			String value 	= messageHeaderStrArr[1];
			log.debug("===== Header {}=[{}]", key, value);
			
			header.put(key, value);
			
			if ("host".equals(key.toLowerCase())) {
				String valueArr [] = value.split(":");
				
				if (valueArr.length == 1) {
					serverName = valueArr[0];
					serverPort = 80;
				} else if (valueArr.length == 2) {
					serverName = valueArr[0];
					serverPort = Integer.parseInt(valueArr[1]);
				}
			}
		}
	}
	
	public void setProtocal(String protocalStr) {
		log.debug("===== Protocal {}", protocalStr);
		String protocalStrArr [] = protocalStr.split("\\s+");
		if (protocalStrArr != null) {
			if (protocalStrArr.length > 0) {
				method = protocalStrArr[0];
			}
			if (protocalStrArr.length > 1) {
				requestURI = protocalStrArr[1];
			}
			if (protocalStrArr.length > 2) {
				protocol = protocalStrArr[2];
			}
		}
		
		if (requestURI.contains("?") && requestURI.contains("=")) {
			parameterMap = StringUtils.queryToMap(requestURI.substring(requestURI.indexOf("?") + 1));
		}
	}
	
	public void setRemoteAddr(SocketAddress socketAddress) {
		this.remoteAddr = socketAddress.toString().split(":")[0].substring(1);
	}
	
}
