package pe.kr.ddakker.was.support.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	private static ObjectMapper om = new ObjectMapper();
	
	public static <T> T toJson(String jsonStr, Class<T> clz) {
    	try {
			return om.readValue(jsonStr, clz);
		} catch (IOException e) {
			throw new RuntimeException(jsonStr + " 문자열 변환 실패" + e);
		}
    }
}
