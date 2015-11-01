package pe.kr.ddakker.was.app.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.server.servlet.SimpleServlet;
import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;
import pe.kr.ddakker.was.server.write.PrintWriter;

public class Hello implements SimpleServlet {
	private static Logger log = LoggerFactory.getLogger(Hello.class);

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.debug("call /service.Hello");
		
		PrintWriter out = response.getWriter();
		out.print("<html>                  ");
		out.print("	<head>                 ");
		out.print("		<title>tit</title> ");
		out.print("	</head>                ");
		out.print("	<body>                 ");
		out.print("		<h1>/service.Hello 호출 한 케이스임</h1>");
		out.print("	</body>                ");
		out.print("</html>                 ");
		
		if ("500".equals(request.getParameter("errorCode"))) {
			throw new Exception("일부로 내보자");
		}
	}
}
