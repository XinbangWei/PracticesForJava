package main.model.user;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户抽象基类
 * 定义了所有用户的公共属性和方法
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // 私有属性 - 体现封装特性
    private String userId;
    private String username;
    private String password;
    private String realName;
    private String email;
    private String phone;
    private String department;
    private LocalDateTime createTime;
    private LocalDateTime lastLoginTime;
    private boolean isActive;
    
    // 用户借阅记录列表 - 改进的设计
    private List<BorrowRecord> borrowRecords;
      /**
     * 构造方法
     */
    public User(String userId, String username, String password, String realName, 
                String email, String phone, String department) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.realName = realName;
        this.email = email;
        this.phone = phone;
        this.department = department;
        this.createTime = LocalDateTime.now();
        this.isActive = true;
        this.borrowRecords = new ArrayList<>();
    }
    
    // 抽象方法 - 不同用户类型有不同的实现（体现多态）
    
    /**
     * 获取用户最大借阅数量
     * @return 最大借阅数量
     */
    public abstract int getMaxBorrowCount();
    
    /**
     * 获取用户最大借阅天数
     * @return 最大借阅天数
     */
    public abstract int getMaxBorrowDays();
    
    /**
     * 检查用户是否有权限访问指定类型的资源
     * @param resourceType 资源类型
     * @return 是否有权限访问
     */
    public abstract boolean hasAccessPermission(String resourceType);
    
    /**
     * 检查用户是否可以访问指定文档
     * @param document 文档对象
     * @return 是否可以访问
     */
    public boolean canAccess(main.model.document.Document document) {
        if (document == null) {
            return false;
        }
        
        // 基于文档类型检查权限
        String documentType = document.getClass().getSimpleName();
        
        // 公开图书所有用户都可以访问
        if (documentType.equals("PhysicalBook") || documentType.equals("EBook") || documentType.equals("PublicBook")) {
            return true;
        }
        
        // 内部文档需要授权用户级别
        if (documentType.equals("InternalDocument")) {
            return hasAccessPermission("INTERNAL");
        }
        
        // 档案文档需要档案管理员级别
        if (documentType.equals("ArchiveDocument")) {
            return hasAccessPermission("ARCHIVE");
        }
        
        return false;
    }
    
    /**
     * 获取用户类型描述
     * @return 用户类型描述
     */
    public abstract String getUserTypeDescription();
    
    // 公共方法
    
    /**
     * 验证密码
     * @param inputPassword 输入的密码
     * @return 密码是否正确
     */
    public boolean verifyPassword(String inputPassword) {
        return this.password.equals(inputPassword);
    }
    
    /**
     * 更新最后登录时间
     */
    public void updateLastLoginTime() {
        this.lastLoginTime = LocalDateTime.now();
    }
      /**
     * 添加借阅记录
     * @param documentId 文档ID
     * @param documentTitle 文档标题
     * @param borrowDate 借阅日期
     * @param dueDate 到期日期
     */
    public void addBorrowRecord(String documentId, String documentTitle, 
                               LocalDate borrowDate, LocalDate dueDate) {
        BorrowRecord record = new BorrowRecord(documentId, documentTitle, borrowDate, dueDate);
        borrowRecords.add(record);
    }
    
    /**
     * 归还文档
     * @param documentId 文档ID
     * @param returnDate 归还日期
     */
    public void returnDocument(String documentId, LocalDate returnDate) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getDocumentId().equals(documentId) && !record.isReturned()) {
                record.markAsReturned(returnDate);
                break;
            }
        }
    }
    
    /**
     * 续借文档
     * @param documentId 文档ID
     * @param newDueDate 新的到期日期
     */
    public void renewDocument(String documentId, LocalDate newDueDate) {
        for (BorrowRecord record : borrowRecords) {
            if (record.getDocumentId().equals(documentId) && !record.isReturned()) {
                record.renew(newDueDate);
                break;
            }
        }
    }
    
    /**
     * 获取当前借阅数量（未归还的）
     * @return 当前借阅数量
     */
    public int getCurrentBorrowCount() {
        return (int) borrowRecords.stream()
                                 .filter(record -> !record.isReturned())
                                 .count();
    }
    
    /**
     * 获取当前借阅的文档ID列表
     * @return 当前借阅的文档ID列表
     */
    public List<String> getCurrentBorrowedDocumentIds() {
        return borrowRecords.stream()
                           .filter(record -> !record.isReturned())
                           .map(BorrowRecord::getDocumentId)
                           .collect(Collectors.toList());
    }
    
    /**
     * 获取所有借阅记录
     * @return 借阅记录列表
     */
    public List<BorrowRecord> getBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }
    
    /**
     * 获取当前借阅记录（未归还的）
     * @return 当前借阅记录列表
     */
    public List<BorrowRecord> getCurrentBorrowRecords() {
        return borrowRecords.stream()
                           .filter(record -> !record.isReturned())
                           .collect(Collectors.toList());
    }
    
    /**
     * 获取逾期记录
     * @return 逾期记录列表
     */
    public List<BorrowRecord> getOverdueRecords() {
        return borrowRecords.stream()
                           .filter(record -> !record.isReturned() && record.isOverdue())
                           .collect(Collectors.toList());
    }
    
    /**
     * 检查是否可以继续借阅
     * @return 是否可以继续借阅
     */
    public boolean canBorrowMore() {
        return getCurrentBorrowCount() < getMaxBorrowCount();
    }
    
    // Getter和Setter方法
    
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }
    
    public boolean isActive() {
        return isActive;
    }
    
    public void setActive(boolean active) {
        isActive = active;
    }
      @Override
    public String toString() {
        return String.format("User{userId='%s', username='%s', realName='%s', " +
                           "type='%s', department='%s', borrowedCount=%d}", 
                           userId, username, realName, getUserTypeDescription(), 
                           department, getCurrentBorrowCount());
    }
}
