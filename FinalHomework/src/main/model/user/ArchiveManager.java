package main.model.user;

/**
 * 档案管理员类
 * 专门的档案管理人员，拥有最高权限
 */
public class ArchiveManager extends User {
    
    private static final int MAX_BORROW_COUNT = 20;
    private static final int MAX_BORROW_DAYS = 60;
    
    private String managementArea; // 管理区域
    private String certificationLevel; // 认证级别
    
    /**
     * 构造方法
     */
    public ArchiveManager(String userId, String username, String password, String realName,
                         String email, String phone, String department, 
                         String managementArea, String certificationLevel) {
        super(userId, username, password, realName, email, phone, department);
        this.managementArea = managementArea;
        this.certificationLevel = certificationLevel;
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
        // 档案管理员可以访问所有级别的资源
        return "PUBLIC".equals(resourceType) || 
               "INTERNAL".equals(resourceType) || 
               "ARCHIVE".equals(resourceType);
    }
    
    @Override
    public String getUserTypeDescription() {
        return "档案管理员";
    }
    
    // 特有方法
    
    /**
     * 授权其他用户访问档案资料
     * @param userId 用户ID
     * @param documentId 文档ID
     * @param reason 授权原因
     * @return 是否授权成功
     */
    public boolean authorizeArchiveAccess(String userId, String documentId, String reason) {
        // 这里可以添加具体的授权逻辑
        System.out.println(String.format("档案管理员 %s 授权用户 %s 访问档案 %s，原因：%s", 
                                        getRealName(), userId, documentId, reason));
        return true;
    }
    
    /**
     * 撤销档案访问授权
     * @param userId 用户ID
     * @param documentId 文档ID
     * @return 是否撤销成功
     */
    public boolean revokeArchiveAccess(String userId, String documentId) {
        System.out.println(String.format("档案管理员 %s 撤销用户 %s 对档案 %s 的访问权限", 
                                        getRealName(), userId, documentId));
        return true;
    }
    
    public String getManagementArea() {
        return managementArea;
    }
    
    public void setManagementArea(String managementArea) {
        this.managementArea = managementArea;
    }
    
    public String getCertificationLevel() {
        return certificationLevel;
    }
    
    public void setCertificationLevel(String certificationLevel) {
        this.certificationLevel = certificationLevel;
    }
    
    @Override
    public String toString() {
        return String.format("ArchiveManager{userId='%s', username='%s', realName='%s', " +
                           "department='%s', managementArea='%s', certificationLevel='%s', borrowedCount=%d}", 
                           getUserId(), getUsername(), getRealName(), getDepartment(), 
                           managementArea, certificationLevel, getCurrentBorrowCount());
    }
}
