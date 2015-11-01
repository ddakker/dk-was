package pe.kr.ddakker.was.server;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import pe.kr.ddakker.was.support.util.FileUtils;
import pe.kr.ddakker.was.support.util.JsonUtils;

public class ConfigHelper {
	private static final String CONFIG_PATH = "/config.json";
	
	private static Map<String, Object> configMap = null;
	
	static {
		System.out.println("=========== Config Init " + CONFIG_PATH + " Road ============");
		if (new File(CONFIG_PATH).exists()) {
			throw new RuntimeException(CONFIG_PATH + " 설정파일이 존재하지 않습니다.");
		}
		
		InputStream is = ConfigHelper.class.getResourceAsStream(CONFIG_PATH);
		configMap = JsonUtils.toJson(FileUtils.getFileContent(is), HashMap.class);
	}
	
	
	
	public static final String ERROR_PAGE_403 = get("errorPage_403", String.class);
	public static final String ERROR_PAGE_404 = get("errorPage_404", String.class);
	public static final String ERROR_PAGE_500 = get("errorPage_500", String.class);
	
	
	
	public static <T> T get(String key, Class<T> clz) {
		return (T) configMap.get(key);
	}
	
	public static String get(String key) {
		return get(key, String.class);
	}
	
	public static int getInt(String key) {
		return get(key, Integer.class);
	}
}
