package main.model.user;

/**
 * 系统管理员类
 * IT管理员，负责系统维护和用户管理，不直接借阅图书
 */
public class Administrator extends User {
    
    private static final int MAX_BORROW_COUNT = 0; // 管理员不借阅图书
    private static final int MAX_BORROW_DAYS = 0;
    
    private String adminLevel; // 管理员级别
    private String systemPermissions; // 系统权限描述
    
    /**
     * 构造方法
     */
    public Administrator(String userId, String username, String password, String realName,
                        String email, String phone, String department, 
                        String adminLevel, String systemPermissions) {
        super(userId, username, password, realName, email, phone, department);
        this.adminLevel = adminLevel;
        this.systemPermissions = systemPermissions;
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
        // 系统管理员有查看权限但不借阅
        return true;
    }
    
    @Override
    public String getUserTypeDescription() {
        return "系统管理员";
    }
    
    // 特有的管理方法
    
    /**
     * 创建新用户
     * @param userType 用户类型
     * @param userInfo 用户信息
     * @return 是否创建成功
     */
    public boolean createUser(String userType, String userInfo) {
        System.out.println(String.format("系统管理员 %s 创建新用户：类型=%s, 信息=%s", 
                                        getRealName(), userType, userInfo));
        return true;
    }
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUser(String userId) {
        System.out.println(String.format("系统管理员 %s 删除用户：%s", 
                                        getRealName(), userId));
        return true;
    }
    
    /**
     * 重置用户密码
     * @param userId 用户ID
     * @param newPassword 新密码
     * @return 是否重置成功
     */
    public boolean resetUserPassword(String userId, String newPassword) {
        System.out.println(String.format("系统管理员 %s 重置用户 %s 的密码", 
                                        getRealName(), userId));
        return true;
    }
    
    /**
     * 系统备份
     * @return 是否备份成功
     */
    public boolean backupSystem() {
        System.out.println(String.format("系统管理员 %s 执行系统备份", getRealName()));
        return true;
    }
    
    public String getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(String adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    public String getSystemPermissions() {
        return systemPermissions;
    }
    
    public void setSystemPermissions(String systemPermissions) {
        this.systemPermissions = systemPermissions;
    }
    
    @Override
    public String toString() {
        return String.format("Administrator{userId='%s', username='%s', realName='%s', " +
                           "department='%s', adminLevel='%s', permissions='%s'}", 
                           getUserId(), getUsername(), getRealName(), getDepartment(), 
                           adminLevel, systemPermissions);
    }
}
