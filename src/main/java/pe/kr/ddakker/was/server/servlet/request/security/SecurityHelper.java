package pe.kr.ddakker.was.server.servlet.request.security;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityHelper {
	public static final String SECURITY_DANGER_CASE [] = new String[]{".+\\.(exe)$", ".*\\.\\./.*"};
	
	public static boolean isSecurityDanger(String uri) {
		boolean isDanger = false;
		try {
			for (String val : SECURITY_DANGER_CASE) {
				Pattern p = Pattern.compile(val);
			    Matcher m = p.matcher(uri);
			    isDanger = m.matches();
			    if (isDanger) {
			    	break;
			    }
			}
		} catch (Exception e) {
			System.err.println(e);
		}
		return isDanger;
	}
}
