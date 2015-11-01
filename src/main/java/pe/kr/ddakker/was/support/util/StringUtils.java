package pe.kr.ddakker.was.support.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
	
	/**
	 * 메인 메소드 파리미터를 분석 리턴한다.
	 * @param args
	 * @param key
	 * @return
	 * @auther ddakker 2015. 10. 30.
	 */
	public static String getParam(String [] args, String key) {
		String value = "";
    	if (args != null) {
	    	for (String param : args) {
	    		if (param.contains(key)) {
	    			if (key.equals("--help")) {
	    				value = "show";
	    			} else {
	    				value = param.replaceAll(key, "").replaceAll("=", "");
	    			}
	    			break;
	    		}
	    	}
    	}
    	return value.trim();
    }

	/**
	 * 쿼리 스트링 Map으로 변환
	 * @param queryString
	 * @return
	 * @auther ddakker 2015. 10. 30.
	 */
	public static Map<String, String[]> queryToMap(String queryString) {
		if (queryString == null) {
			return new HashMap<String, String[]>(0);
		}

		HashMap<String, Object> map1 = new HashMap<String, Object>();
		int p = 0;
		while (p < queryString.length()) {
			int p0 = p;
			while (p < queryString.length() && queryString.charAt(p) != '=' && queryString.charAt(p) != '&') {
				p++;
			}
			
			String name = urlDecode(queryString.substring(p0, p));
			if (p < queryString.length() && queryString.charAt(p) == '=') {
				p++;
			}
			
			p0 = p;
			while (p < queryString.length() && queryString.charAt(p) != '&') {
				p++;
			}
			
			String value = urlDecode(queryString.substring(p0, p));
			if (p < queryString.length() && queryString.charAt(p) == '&') {
				p++;
			}
			
			Object x = map1.get(name);
			if (x == null) {
				map1.put(name, value);
			} else if (x instanceof String) {
				ArrayList<String> a = new ArrayList<String>();
				a.add((String) x);
				a.add(value);
				map1.put(name, a);
			} else {
				ArrayList<String> a = (ArrayList<String>) x;
				a.add(value);
			}
		}
		HashMap<String, String[]> map2 = new HashMap<String, String[]>(map1.size());
		for (Map.Entry<String, Object> e : map1.entrySet()) {
			String name = e.getKey();
			Object x = e.getValue();
			String[] v;
			if (x instanceof String) {
				v = new String[] { (String) x };
			} else {
				ArrayList<String> a = (ArrayList<String>) x;
				v = new String[a.size()];
				v = a.toArray(v);
			}
			map2.put(name, v);
		}
		return map2;
	}

	private static String urlDecode(String s) {
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error in urlDecode.", e);
		}
	}
}
