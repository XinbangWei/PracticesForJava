package main.model.document;

import main.model.user.User;
import main.model.user.ArchiveManager;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

/**
 * 档案资料类
 * 重要历史档案、机密文件，仅档案管理员可管理和授权访问
 */
public class ArchiveDocument extends Document {
    
    private String archiveLevel; // 档案级别 (机密、绝密等)
    private String storageLocation; // 存储位置
    private String archiveNumber; // 档案编号
    private LocalDate archiveDate; // 归档日期
    private String archivedBy; // 归档人
    private boolean requiresSpecialPermission; // 是否需要特殊权限
    private Map<String, String> authorizedUsers; // 被授权的用户及授权原因
    
    /**
     * 构造方法
     */
    public ArchiveDocument(String documentId, String title, String author, String publisher,
                          String category, String description, LocalDate publishDate,
                          String archiveLevel, String storageLocation, String archiveNumber,
                          String archivedBy, boolean requiresSpecialPermission) {
        super(documentId, title, author, publisher, category, description, publishDate);
        this.archiveLevel = archiveLevel;
        this.storageLocation = storageLocation;
        this.archiveNumber = archiveNumber;
        this.archiveDate = LocalDate.now();
        this.archivedBy = archivedBy;
        this.requiresSpecialPermission = requiresSpecialPermission;
        this.authorizedUsers = new HashMap<>();
    }
    
    @Override
    public String getDocumentType() {
        return "档案资料";
    }
    
    @Override
    public String getAccessLevel() {
        return "ARCHIVE";
    }
    
    @Override
    public boolean checkAccessPermission(User user) {
        // 只有档案管理员可以直接访问
        if (user instanceof ArchiveManager) {
            return true;
        }
        
        // 或者被特别授权的用户
        return authorizedUsers.containsKey(user.getUserId());
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("【档案资料】%s - %s | 档案级别: %s | 编号: %s | 位置: %s", 
                           getTitle(), getAuthor(), archiveLevel, archiveNumber, storageLocation);
    }
    
    // 特有方法
    
    /**
     * 档案管理员授权用户访问
     * @param manager 档案管理员
     * @param userId 被授权用户ID
     * @param reason 授权原因
     * @return 是否授权成功
     */
    public boolean authorizeAccess(ArchiveManager manager, String userId, String reason) {
        if (manager == null) {
            return false;
        }
        
        authorizedUsers.put(userId, reason);
        System.out.println(String.format("档案管理员 %s 授权用户 %s 访问档案 %s，原因：%s", 
                                        manager.getRealName(), userId, getTitle(), reason));
        return true;
    }
    
    /**
     * 撤销用户访问授权
     * @param manager 档案管理员
     * @param userId 用户ID
     * @return 是否撤销成功
     */
    public boolean revokeAccess(ArchiveManager manager, String userId) {
        if (manager == null) {
            return false;
        }
        
        String removedReason = authorizedUsers.remove(userId);
        if (removedReason != null) {
            System.out.println(String.format("档案管理员 %s 撤销用户 %s 对档案 %s 的访问权限", 
                                            manager.getRealName(), userId, getTitle()));
            return true;
        }
        return false;
    }
    
    /**
     * 记录详细的访问日志
     * @param userId 访问用户ID
     * @param accessType 访问类型
     * @param ipAddress IP地址
     */
    public void logDetailedAccess(String userId, String accessType, String ipAddress) {
        System.out.println(String.format("[档案访问日志] 时间: %s | 用户: %s | 档案: %s | 操作: %s | IP: %s | 级别: %s", 
                                        LocalDate.now(), userId, getTitle(), accessType, 
                                        ipAddress, archiveLevel));
    }
    
    /**
     * 升级档案级别
     * @param manager 档案管理员
     * @param newLevel 新级别
     * @param reason 升级原因
     */
    public void upgradeArchiveLevel(ArchiveManager manager, String newLevel, String reason) {
        if (manager == null) {
            return;
        }
        
        String oldLevel = this.archiveLevel;
        this.archiveLevel = newLevel;
        System.out.println(String.format("档案 %s 级别由 %s 升级为 %s，操作人：%s，原因：%s", 
                                        getTitle(), oldLevel, newLevel, manager.getRealName(), reason));
    }
    
    /**
     * 获取授权用户列表
     * @return 授权用户信息
     */
    public String getAuthorizedUsersInfo() {
        if (authorizedUsers.isEmpty()) {
            return "无特别授权用户";
        }
        
        StringBuilder sb = new StringBuilder("授权用户列表：\n");
        for (Map.Entry<String, String> entry : authorizedUsers.entrySet()) {
            sb.append(String.format("- 用户: %s, 原因: %s\n", entry.getKey(), entry.getValue()));
        }
        return sb.toString();
    }
    
    // Getter方法
    
    public String getArchiveLevel() {
        return archiveLevel;
    }
    
    public String getStorageLocation() {
        return storageLocation;
    }
    
    public String getArchiveNumber() {
        return archiveNumber;
    }
    
    public LocalDate getArchiveDate() {
        return archiveDate;
    }
    
    public String getArchivedBy() {
        return archivedBy;
    }
    
    public boolean isRequiresSpecialPermission() {
        return requiresSpecialPermission;
    }
    
    @Override
    public String toString() {
        return String.format("ArchiveDocument{id='%s', title='%s', author='%s', " +
                           "level='%s', number='%s', location='%s', authorized=%d, available=%s}", 
                           getDocumentId(), getTitle(), getAuthor(), 
                           archiveLevel, archiveNumber, storageLocation, 
                           authorizedUsers.size(), isAvailable());
    }
}