package pe.kr.ddakker.was.server.exception;

/**
 * 500 에러 케이스를 모아보자
 * @author ddakker 2015. 10. 30.
 */
public class ServletException extends Exception {

    public ServletException() {
        super();
    }

    public ServletException(String message) {
        super(message);
    }
    
    public ServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ServletException(Throwable rootCause) {
        super(rootCause);
    }

    public Throwable getRootCause() {
        return getCause();
    }
}
