package main.model.user;

/**
 * 授权用户类
 * 科研人员、项目负责人等，有更高权限
 */
public class AuthorizedUser extends User {
    
    private static final int MAX_BORROW_COUNT = 10;
    private static final int MAX_BORROW_DAYS = 45;
    
    private String securityLevel; // 安全等级
    private String researchField; // 研究领域
    
    /**
     * 构造方法
     */
    public AuthorizedUser(String userId, String username, String password, String realName,
                         String email, String phone, String department, 
                         String securityLevel, String researchField) {
        super(userId, username, password, realName, email, phone, department);
        this.securityLevel = securityLevel;
        this.researchField = researchField;
    }
    
    @Override
    public int getMaxBorrowCount() {
        return MAX_BORROW_COUNT;
    }
    
    @Override
    public int getMaxBorrowDays() {
        return MAX_BORROW_DAYS;
    }
    
    @Override
    public boolean hasAccessPermission(String resourceType) {
        // 授权用户可以访问公开资源和内部资料
        return "PUBLIC".equals(resourceType) || "INTERNAL".equals(resourceType);
    }
    
    @Override
    public String getUserTypeDescription() {
        return "授权用户";
    }
    
    // 特有方法
    
    public String getSecurityLevel() {
        return securityLevel;
    }
    
    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }
    
    public String getResearchField() {
        return researchField;
    }
    
    public void setResearchField(String researchField) {
        this.researchField = researchField;
    }
    
    @Override
    public String toString() {
        return String.format("AuthorizedUser{userId='%s', username='%s', realName='%s', " +
                           "department='%s', securityLevel='%s', researchField='%s', borrowedCount=%d}", 
                           getUserId(), getUsername(), getRealName(), getDepartment(), 
                           securityLevel, researchField, getCurrentBorrowCount());
    }
}
