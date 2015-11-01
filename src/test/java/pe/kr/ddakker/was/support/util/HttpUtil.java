package pe.kr.ddakker.was.support.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class HttpUtil {
	
	public static Map<String, Object> get(String getUrl) {
		return get(getUrl, null);
	}
	
	public static Map<String, Object> get(String getUrl, Map<String, String> headers) {
		Map<String, Object> resultMap = new HashMap<>();
		
		String body = "";
		int code = -1;
		BufferedReader br = null;
		try {
			URL url = new URL(getUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setUseCaches(false);
			
			if (headers != null) {
				for(Entry<String, String> entry : headers.entrySet()) {
				    con.setRequestProperty(entry.getKey(), entry.getValue());
				}

			}
			code = con.getResponseCode();
			if (code == 200) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			
			String inputLine;
			while ((inputLine = br.readLine()) != null) { 
				body += inputLine + "\n";
			}
		} catch (IOException e) {
			System.err.println("E: " + e);
			body = null;
		} finally {
			if (br != null) {
				try { br.close(); } catch (IOException e) { }
			}
		}
		
		resultMap.put("code", code);
		resultMap.put("body", body);
		return resultMap;
	}
}
