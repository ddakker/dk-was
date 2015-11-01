package pe.kr.ddakker.was.server;


import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pe.kr.ddakker.was.support.util.StringUtils;

/**
 * Created by cybaek on 15. 5. 22..
 * Modyfy by ddakker on 15. 10. 30..
 */
public class HttpServer {
	private static Logger log = LoggerFactory.getLogger(HttpServer.class);
	
	public static final String SERVER_NAME = "DK Servlet Server 1.0";
	
    private static final int NUM_THREADS 	= 50;
    private static final String INDEX_FILE 	= "index.html";
    
    private final File webroot;
    private final int port;
    private final String indexFile;

    public HttpServer(File webroot, Integer port) throws IOException {
    	if (webroot == null || !webroot.exists()) {
    		try {
    			webroot = new File(HttpServer.class.getResource("/").getPath() + ConfigHelper.get("webroot"));
    			if (!webroot.isDirectory()) {
    				webroot = new File(ConfigHelper.get("webroot"));
    			}
			} catch (Exception e) {
				webroot = new File(ConfigHelper.get("webroot"));
			}
    	}
    	if (port == -1) {
    		port = ConfigHelper.get("port", Integer.class);
    	}
    	
        if (webroot == null || !webroot.isDirectory()) {
			throw new IOException("webroot 디렉토리가 존재하지 않습니다. webroot: " + webroot);
		}
		if (port == null || port <= 0 || (port <= 0 || port > 65535)) {
			throw new RuntimeException("포트 번호가 정상적이지 않습니다. port: " + port);
		}
        this.webroot = webroot;
        this.port = port;
        this.indexFile = ConfigHelper.get("indexFile")==null?INDEX_FILE:ConfigHelper.get("indexFile");
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);
        try (ServerSocket server = new ServerSocket(port)) {
        	log.info("Accepting connections on port {}", server.getLocalPort());
        	log.info("Document Root: {}", webroot);
            while (true) {
                try {
                    Socket request = server.accept();
                    Runnable r = new RequestProcessor(webroot, indexFile, request);
                    pool.submit(r);
                } catch (IOException ex) {
                    log.error("Error accepting connection", ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        File webroot = null;
        Integer port = -1;
        try {
        	if (args.length > 0) {
        		if ("show".equals(StringUtils.getParam(args, "--help"))) {
        			throw new ArrayIndexOutOfBoundsException();
        		}
        		
	        	webroot = new File(StringUtils.getParam(args, "-webroot"));
	        	port = Integer.parseInt(StringUtils.getParam(args, "-port"));
        	}
        } catch (Exception e) {
        	if (e instanceof NullPointerException) {
        		System.err.println(e);
        	}
            System.out.println("Usage: \t==== DK-WAS ====");
            System.out.println("\t한글이 문제가 된다면 JVM Parameter -Dfile.encoding=UTF-8 을 추가 해주세요.");
            System.out.println("\tjava -jar war.jar --help");
            System.out.println("\tjava -jar war.jar (별도의 파라미터 정보가 없을 시 classpath:/config.json 의 webroot, port 정보를 바라봅니다.)");
            System.out.println("\tjava -jar war.jar -webroot=[dir] -port[0~65535]");
            return;
        }
        
        serverStart(webroot, port);
    }
    

	private static void serverStart(File webroot, Integer port) {
		try {
            HttpServer webserver = new HttpServer(webroot, port);
            webserver.start();
        } catch (IOException ex) {
            log.error("Server could not start", ex);
        }
	}
}