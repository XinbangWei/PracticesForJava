package main.model.user;

/**
 * 普通用户类
 * 一般工作人员，权限最基本
 */
public class RegularUser extends User {
    
    private static final int MAX_BORROW_COUNT = 5;
    private static final int MAX_BORROW_DAYS = 30;
    
    /**
     * 构造方法
     */
    public RegularUser(String userId, String username, String password, String realName,
                      String email, String phone, String department) {
        super(userId, username, password, realName, email, phone, department);
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
        // 普通用户只能访问公开资源
        return "PUBLIC".equals(resourceType);
    }
    
    @Override
    public String getUserTypeDescription() {
        return "普通用户";
    }
}
