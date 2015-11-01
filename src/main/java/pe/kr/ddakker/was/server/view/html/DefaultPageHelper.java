package pe.kr.ddakker.was.server.view.html;

public class DefaultPageHelper {
	public static String get403Page () {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>                  ");
		sb.append("	<head>                 ");
		sb.append("		<title>403 Error</title> ");
		sb.append("	</head>                ");
		sb.append("	<body>                 ");
		sb.append("		<h1>403 Error</h1>");
		sb.append("		<div><strong>Default Page</strong></div>");
		sb.append("		<div>보안에 정책 오류입니다.</div>");
		sb.append("	</body>                ");
		sb.append("</html>                 ");
		return sb.toString();
	}
	
	public static String get404Page () {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>                  ");
		sb.append("	<head>                 ");
		sb.append("		<title>404 Error</title> ");
		sb.append("	</head>                ");
		sb.append("	<body>                 ");
		sb.append("		<h1>404 Error</h1>");
		sb.append("		<div><strong>Default Page</strong></div>");
		sb.append("		<div>페이지를 찾을 수 없습니다.</div>");
		sb.append("	</body>                ");
		sb.append("</html>                 ");
		return sb.toString();
	}
	
	public static String get500Page () {
		StringBuffer sb = new StringBuffer();
		sb.append("<html>                  ");
		sb.append("	<head>                 ");
		sb.append("		<title>500 Error</title> ");
		sb.append("	</head>                ");
		sb.append("	<body>                 ");
		sb.append("		<h1>500 Error</h1>");
		sb.append("		<div><strong>Default Page</strong></div>");
		sb.append("		<div>비지니스 처리 중 오류가 발생하였습니다.</div>");
		sb.append("	</body>                ");
		sb.append("</html>                 ");
		return sb.toString();
	}
}
