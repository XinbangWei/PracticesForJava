package main.exception;

/**
 * 图书馆系统的基础异常类
 * 提供系统内所有异常的共同基础
 */
public class LibraryException extends Exception {
    private String errorCode;
    
    public LibraryException(String message) {
        super(message);
    }
    
    public LibraryException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public LibraryException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public LibraryException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
}
