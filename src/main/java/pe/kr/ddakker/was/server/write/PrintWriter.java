package pe.kr.ddakker.was.server.write;

import java.io.IOException;

public class PrintWriter {
	private StringBuffer sb = new StringBuffer();
	
	public void clear() throws IOException {
		sb = new StringBuffer();
	}
	
	public void print(String value) throws IOException {
		println(value);
	}
	
	public void println(String value) throws IOException {
		sb.append(value);
	}
	
	public String getData() {
		return sb.toString();
	}
}
