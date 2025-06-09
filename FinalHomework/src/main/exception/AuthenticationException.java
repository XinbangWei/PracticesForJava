package main.exception;

/**
 * 用户认证相关异常
 */
public class AuthenticationException extends LibraryException {
    public AuthenticationException(String message) {
        super("AUTH_ERROR", message);
    }
    
    public AuthenticationException(String message, Throwable cause) {
        super("AUTH_ERROR", message, cause);
    }
}
