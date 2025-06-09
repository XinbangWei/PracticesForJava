package main.model.document;

import main.model.user.User;
import java.time.LocalDate;

/**
 * 内部资料类
 * 仅限授权用户访问的科研报告、技术文档等
 */
public class InternalDocument extends Document {
    
    private String securityLevel; // 安全级别
    private String researchField; // 研究领域
    private String projectCode; // 项目代码
    private String approvalNumber; // 批准文号
    private LocalDate classificationDate; // 定密日期
    private String classifiedBy; // 定密人
    
    /**
     * 构造方法
     */
    public InternalDocument(String documentId, String title, String author, String publisher,
                           String category, String description, LocalDate publishDate,
                           String securityLevel, String researchField, String projectCode,
                           String approvalNumber, String classifiedBy) {
        super(documentId, title, author, publisher, category, description, publishDate);
        this.securityLevel = securityLevel;
        this.researchField = researchField;
        this.projectCode = projectCode;
        this.approvalNumber = approvalNumber;
        this.classificationDate = LocalDate.now();
        this.classifiedBy = classifiedBy;
    }
    
    @Override
    public String getDocumentType() {
        return "内部资料";
    }
    
    @Override
    public String getAccessLevel() {
        return "INTERNAL";
    }
    
    @Override
    public boolean checkAccessPermission(User user) {
        // 只有授权用户可以访问内部资料
        return user.hasAccessPermission("INTERNAL");
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("【内部资料】%s - %s | 安全级别: %s | 研究领域: %s | 项目: %s", 
                           getTitle(), getAuthor(), securityLevel, researchField, projectCode);
    }
    
    // 特有方法
    
    /**
     * 检查用户是否有特定项目的访问权限
     * @param user 用户
     * @param userProjectCode 用户所属项目代码
     * @return 是否有权限
     */
    public boolean checkProjectAccess(User user, String userProjectCode) {
        if (!checkAccessPermission(user)) {
            return false;
        }
        
        // 检查项目代码是否匹配
        return projectCode.equals(userProjectCode) || "ALL".equals(projectCode);
    }
    
    /**
     * 记录访问日志
     * @param userId 访问用户ID
     * @param accessType 访问类型
     */
    public void logAccess(String userId, String accessType) {
        System.out.println(String.format("[内部资料访问日志] 时间: %s | 用户: %s | 文档: %s | 操作: %s", 
                                        LocalDate.now(), userId, getTitle(), accessType));
    }
    
    // Getter和Setter方法
    
    public String getSecurityLevel() {
        return securityLevel;
    }
    
    public String getResearchField() {
        return researchField;
    }
    
    public String getProjectCode() {
        return projectCode;
    }
    
    public String getApprovalNumber() {
        return approvalNumber;
    }
    
    public LocalDate getClassificationDate() {
        return classificationDate;
    }
    
    public String getClassifiedBy() {
        return classifiedBy;
    }
    
    @Override
    public String toString() {
        return String.format("InternalDocument{id='%s', title='%s', author='%s', " +
                           "securityLevel='%s', field='%s', project='%s', available=%s}", 
                           getDocumentId(), getTitle(), getAuthor(), 
                           securityLevel, researchField, projectCode, isAvailable());
    }
}