package pe.kr.ddakker.was.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import pe.kr.ddakker.was.support.util.HttpUtil;

public class HttpServer1Test {
	public static boolean isRunning = false;
	
	@BeforeClass
	public static void before() throws Exception {
		isRunning = true;
		
		Thread wasThread = new Thread(new Runnable() {
			public void run() {
				try {
					new HttpServer(null, -1).start();	// 설정 파일 정보
					
					while (true) {
						if (isRunning == false) {
							break;
						}
						try { Thread.sleep(1000); } catch (InterruptedException e) { }
					}
					
					
				} catch (IOException e) {
					System.err.println("e: " + e);
				}
			}
		});
		wasThread.setDaemon(true);
		wasThread.start();
	}
	
	@AfterClass
	public static void after() throws Exception {
		isRunning = false;
	}
	
	@Test
	public void 일반_정적URI_테스트() {
		assertThat("/ 페이지", String.valueOf(HttpUtil.get("http://localhost:8080/").get("body")), containsString("<h1>index.html</h1>"));
		
		assertThat("/index 페이지", String.valueOf(HttpUtil.get("http://localhost:8080/index.html").get("body")), containsString("<h1>index.html</h1>"));
	}
	
	@Test
	public void 도메인별_정적URI_테스트() {
		// locsl dns(hosts) 수정 수 테스트 해야 하므로 주석 하였습니다.
		
		/*assertThat("/ 페이지", HttpUtil.get("http://a.com:8080/"), containsString("<h1>a.com index.html</h1>"));
		assertThat("/index 페이지", HttpUtil.get("http://a.com:8080/index.html"), containsString("<h1>a.com index.html</h1>"));
		assertThat("/ 페이지", HttpUtil.get("http://b.com:8080/"), containsString("<h1>b.com index.html</h1>"));
		assertThat("/index 페이지", HttpUtil.get("http://b.com:8080/index.html"), containsString("<h1>b.com index.html</h1>"));*/
	}
	
	@Test
	public void 상태코에_따른_결과_페이지_사용자_정의_페이지() {
		Map<String, Object> resultMap = HttpUtil.get("http://localhost:8080/index.html");
		assertThat("200 정상", String.valueOf(resultMap.get("code")), is("200"));
		
		resultMap = HttpUtil.get("http://localhost:8080/test/a.exe");
		assertThat("403 보안 정책 위반 code 1", String.valueOf(resultMap.get("code")), is("403"));
		assertThat("403 보안 정책 위반 body 1", String.valueOf(resultMap.get("body")), containsString("보안에 정책 오류입니다."));
		
		resultMap = HttpUtil.get("http://localhost:8080/test/../a.html");
		assertThat("403 보안 정책 위반 code 2", String.valueOf(resultMap.get("code")), is("403"));
		assertThat("403 보안 정책 위반  body 2", String.valueOf(resultMap.get("body")), containsString("보안에 정책 오류입니다."));
		
		resultMap = HttpUtil.get("http://localhost:8080/asdf.html");
		assertThat("404 페이지 없음", String.valueOf(resultMap.get("code")), is("404"));
		assertThat("404 페이지 없음", String.valueOf(resultMap.get("body")), containsString("페이지를 찾을 수 없습니다."));
		
		resultMap = HttpUtil.get("http://localhost:8080/service.Hello?errorCode=500");
		assertThat("500 처리 중 오류", String.valueOf(resultMap.get("code")), is("500"));
		assertThat("500 처리 중 오류", String.valueOf(resultMap.get("body")), containsString("비지니스 처리 중 오류가 발생하였습니다."));
	}

	@Test
	public void 서블릿_테스트() {
		Map<String, Object> resultMap = HttpUtil.get("http://localhost:8080/service.Hello");
		assertThat("service.Htllo.java 케이스", String.valueOf(resultMap.get("body")), containsString("/service.Hello 호출 한 케이스임"));
		
		resultMap = HttpUtil.get("http://localhost:8080/Hello");
		assertThat("Htllo.java 케이스", String.valueOf(resultMap.get("body")), containsString("/Hello 호출 한 케이스임"));
		
		resultMap = HttpUtil.get("http://localhost:8080/NowDate");
		assertThat("현재 시각 케이스", String.valueOf(resultMap.get("body")), containsString("<h1>현재시각</h1>"));
	}
}
