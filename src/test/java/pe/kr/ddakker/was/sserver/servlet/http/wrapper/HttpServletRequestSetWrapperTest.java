package pe.kr.ddakker.was.sserver.servlet.http.wrapper;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import pe.kr.ddakker.was.server.servlet.http.wrapper.HttpServletRequestSetWrapper;

public class HttpServletRequestSetWrapperTest {
	@Test
	public void test_문자열_관련_테스트() {
		String value = "a.com:80";
		assertThat("2 겠지?", value.split(":").length, is(2));
		
		value = "a.com";
		assertThat("1 겠지?", value.split(":").length, is(1));
		
		value = "GET /index.html HTTP/1.1";
		assertThat("3 겠지?", value.split(" ").length, is(3));
		
		new HttpServletRequestSetWrapper().setProtocal(value);
	}
}
