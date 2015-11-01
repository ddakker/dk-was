구현 스펙
	구현 여부 및 해당 설명을 간략하게 정리 하였습니다.

0-0. 개발 환경
	Window 8, JDK 1.7, Eclipse
	
	
	
0-1. 빌드
	mvn clean package 시 logback 등 의존된 라이브러리 참조 문제로 package 시점에 copy-dependencies 를 추가 하였습니다.
	그래서 was.jar 외에 /dependency/*.jar 파일들이 생기게 되었습니다.
	꼭 단일의 파일로 묶어야 한다면 주석 해놓은 maven-assembly-plugin 부분과 교체하는 방법으로 할 수 있도록 하였습니다. 



0-2. 실행	
	classpath:/config.json 파일의 "webroot" 부분을 환경에 맞게 설정 해야 합니다.
	java -jar war.jar
		
	Usage:  ==== DK-WAS ====       
         한글이 문제가 된다면 JVM Parameter -Dfile.encoding=UTF-8 을 추가 해주세요.
    java -jar war.jar --help
    java -jar war.jar (별도의 파라미터 정보가 없을 시 classpath:/config.json 의 webroot, port 정보를 바라봅니다.)
    java -Dfile.encoding=UTF-8 -jar war.jar -webroot=[dir] -port[0~65535]
        
        
        
1. HTTP/1.1 의 Host 헤더를 해석하세요.
	- localhost 가 아닐 경우 webroot/[Host]/하위에서 정적 자원을 서비스 하도록 하였습니다.
	- local dns(hosts) 설정을 해야 하므로 테스트 케이스는 주석 처리 하였습니다.
	  pe.kr.ddakker.was.server.HttpServer1Test.도메인별_정적URI_테스트()	

	
	
2. 다음 사항을 설정 파일로 관리하세요.
	- classpath:/config.json 을 통하여 구동 시점에 jackson parse를 이용하여 사용하도록 하였습니다.
	- Host에 따라서 webroot/{Host}/하위에서 정적 자원을 찾도록 하였습니다.
	  local dns(hosts) 설정을 해야 하므로 테스트 케이스에서 주석 해 놓았습니다.
	  pe.kr.ddakker.was.server.HttpServer1Test.도메인별_정적URI_테스트()
	{
		"webroot": "D:\\dev\\workspace-mars\\dk-was\\src\\main\\resources\\META-INF\\webroot",
		"port": 8080,
		"indexFile": "index.html",
		"errorPage_403": "/error/403.html",
		"errorPage_404": "/error/404.html",
		"errorPage_500": "/error/500.html"
	}
	


3~4. 403, 404, 500 에러를 처리합니다.
	- HttpStatus Code에 다른 처리를 하였습니다.
	- 403에 대한 규칙 추가는 pe.kr.ddakker.was.server.servlet.request.security.SecurityHelper 케이스별 정규식 배열로 정의 하였습니다. 
	  pe.kr.ddakker.was.server.HttpServer1Test.상태코에_따른_결과_페이지_사용자_정의_페이지()
	- 사용자 정의형 에러 페이지를 지정 하지 않을 경우 Default 에러 메시지를 출력 하게 하였습니다.
	  pe.kr.ddakker.was.server.view.html.DefaultPageHelper	  
	  
	  
	  
5. logback 프레임워크 http://logback.qos.ch/를 이용하여 다음의 로깅 작업을 합니다.
	- classpath:/logback.xml 부분에 server 영역과 application 영역을 분리하여 출력 하도록 하였습니다.
	  appender=serverLog
	  appender=appLog
	- 구현체 변경 가능성을 위하여 slf4j 를 통해 logback 이 동작 할 수 있게 하였습니다.
		


6. 간단한 WAS 를 구현합니다.
	- Servlet 처리 및 현재 시간정보를 확인 할 수 있는 서블릿을 작성하였습니다.
	  /Hello
	  /service.Hello
	  /NowDate
	  pe.kr.ddakker.was.server.HttpServer1Test.서블릿_테스트()
	- 현재 상황에 필요한 최소 한의 요청/응답 정보를 담을 수 있는 객체를 활용 하였습니다.
	  pe.kr.ddakker.was.server.servlet.http.HttpServletRequest
	  pe.kr.ddakker.was.server.servlet.http.HttpServletResponse
	- 이 후 설정 파일에서 맵핑 할 수 있도록 reflect 을 하고, SimpleServlet 인터페이스를 활용하여 추가적인 서블릿 구현체를 동적으로 처리 할 수 있도록 하였습니다.
	  pe.kr.ddakker.was.server.servlet.commender.ServletCommander
	  


7. 현재 시각을 출력하는 SimpleServlet 구현체를 작성하세요.
	- /NowDate
	
	

8. 앞에서 구현한 여러 스펙을 검증하는 테스트 케이스를 JUnit4 를 이용해서 작성하세요.
	- pe.kr.ddakker.was.server.HttpServer1Test
	  pe.kr.ddakker.was.server.servlet.request.security.SecurityHelperTest
	  pe.kr.ddakker.was.sserver.servlet.http.wrapper.HttpServletRequestSetWrapperTest
	  pe.kr.ddakker.was.support.util.HttpUtil
	  pe.kr.ddakker.was.support.util.StringUtilsTest