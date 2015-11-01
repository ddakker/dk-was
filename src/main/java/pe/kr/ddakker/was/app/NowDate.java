package pe.kr.ddakker.was.app;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.server.servlet.SimpleServlet;
import pe.kr.ddakker.was.server.servlet.http.HttpServletRequest;
import pe.kr.ddakker.was.server.servlet.http.HttpServletResponse;
import pe.kr.ddakker.was.server.write.PrintWriter;

public class NowDate implements SimpleServlet {
	private static Logger log = LoggerFactory.getLogger(NowDate.class);
	
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy년  MM월 dd일 hh시 mm분 ss초");
        Date date = new Date();
        String today = df.format(date);
        
        log.debug("현재 시간은: {}", today);
	        
		PrintWriter out = response.getWriter();
		out.print("<html>                  ");
		out.print("	<head>                 ");
		out.print("		<title>tit</title> ");
		out.print("	</head>                ");
		out.print("	<body>                 ");
		out.print("		<h1>현재시각</h1>");
		out.print("		<div><strong>" + today + "</strong></div>");
		out.print("	</body>                ");
		out.print("</html>                 ");
		
		if ("500".equals(request.getParameter("errorCode"))) {
			throw new Exception("일부로 내보자");
		}
	}
}
