package main.model.system;

/**
 * 系统配置类
 * 定义系统的各种配置参数
 */
public class SystemConfig {
    
    // 系统基本配置
    public static final String SYSTEM_NAME = "图书管理系统";
    public static final String VERSION = "1.0.0";
    public static final String ORGANIZATION = "科研院所";
    
    // 文件路径配置
    public static final String DATA_DIR = "data/";
    public static final String USERS_FILE = DATA_DIR + "users.dat";
    public static final String DOCUMENTS_FILE = DATA_DIR + "documents.dat";
    public static final String LOGS_FILE = DATA_DIR + "system.log";
    public static final String AUDIT_LOG_FILE = DATA_DIR + "audit.log";
    
    // 系统限制配置
    public static final int MAX_LOGIN_ATTEMPTS = 3;
    public static final int SESSION_TIMEOUT_MINUTES = 30;
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int MAX_SEARCH_RESULTS = 100;
      // 借阅配置
    public static final int DEFAULT_BORROW_DAYS = 30;
    public static final int MAX_EXTEND_TIMES = 2;
    public static final int OVERDUE_FINE_PER_DAY = 1; // 元/天
    public static final int MAX_EXTEND_DAYS = 15; // 最大续借天数
    public static final int REGULAR_USER_BORROW_LIMIT = 5; // 普通用户借阅限制
    public static final int AUTHORIZED_USER_BORROW_LIMIT = 10; // 授权用户借阅限制
    
    // 用户类型配置
    public static final class UserLimits {
        public static final int REGULAR_MAX_BORROW = 5;
        public static final int REGULAR_MAX_DAYS = 30;
        
        public static final int AUTHORIZED_MAX_BORROW = 10;
        public static final int AUTHORIZED_MAX_DAYS = 45;
        
        public static final int ARCHIVE_MANAGER_MAX_BORROW = 20;
        public static final int ARCHIVE_MANAGER_MAX_DAYS = 60;
    }
    
    // 安全级别配置
    public static final class SecurityLevels {
        public static final String PUBLIC = "公开";
        public static final String INTERNAL = "内部";
        public static final String CONFIDENTIAL = "机密";
        public static final String SECRET = "绝密";
    }
    
    // 文档类型配置
    public static final class DocumentTypes {
        public static final String PHYSICAL_BOOK = "实体书";
        public static final String EBOOK = "电子书";
        public static final String INTERNAL_DOC = "内部资料";
        public static final String ARCHIVE_DOC = "档案资料";
    }
    
    // 操作类型配置
    public static final class OperationTypes {
        public static final String LOGIN = "登录";
        public static final String LOGOUT = "登出";
        public static final String BORROW = "借阅";
        public static final String RETURN = "归还";
        public static final String EXTEND = "续借";
        public static final String SEARCH = "搜索";
        public static final String VIEW = "查看";
        public static final String DOWNLOAD = "下载";
        public static final String PRINT = "打印";
        public static final String AUTHORIZE = "授权";
        public static final String REVOKE = "撤销授权";
    }
    
    // 日期时间格式配置
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    
    // 私有构造函数，防止实例化
    private SystemConfig() {
        // 工具类不应被实例化
    }
    
    /**
     * 获取系统信息
     */
    public static String getSystemInfo() {
        return String.format("%s v%s - %s", SYSTEM_NAME, VERSION, ORGANIZATION);
    }
    
    /**
     * 验证密码强度
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= PASSWORD_MIN_LENGTH;
    }
    
    /**
     * 检查是否为有效的安全级别
     */
    public static boolean isValidSecurityLevel(String level) {
        return SecurityLevels.PUBLIC.equals(level) ||
               SecurityLevels.INTERNAL.equals(level) ||
               SecurityLevels.CONFIDENTIAL.equals(level) ||
               SecurityLevels.SECRET.equals(level);
    }
}