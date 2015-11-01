package pe.kr.ddakker.was.server.servlet.request.security;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class SecurityHelperTest {
	@Test
	public void test_보안_위배_URI_테스트() {
		assertThat("케이스 1-1", SecurityHelper.isSecurityDanger("/test/text.exe"), is(true));
		
		assertThat("케이스 2-1", SecurityHelper.isSecurityDanger("/test/textexe"), is(false));
		

		assertThat("케이스 3-1", SecurityHelper.isSecurityDanger("/test/../textexe"), is(true));
		assertThat("케이스 3-1", SecurityHelper.isSecurityDanger("/test/../../test"), is(true));
		assertThat("케이스 3-1", SecurityHelper.isSecurityDanger("/test/../tex/../texe"), is(true));
	}
}
