package pe.kr.ddakker.was.support.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {
	public static String getFileContent(File file) {
		String resultStr = "";
		try {
			if (!file.exists()) {
				throw new RuntimeException("파일이 존재하지 않습니다." + file);
			}
			
			resultStr = getFileContent(new FileInputStream(file));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return resultStr;
	}
	
	public static String getFileContent(InputStream is) {
		BufferedReader 	br = null;
		StringBuilder 	sb = new StringBuilder();

		String line;
		try {
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (br != null) {
				try { br.close(); } catch (IOException e) { }
			}
		}
		return sb.toString();
	}
	
}
