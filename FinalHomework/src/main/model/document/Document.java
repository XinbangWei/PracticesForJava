package main.model.document;

import main.interfaces.Borrowable;
import main.model.user.User;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 文档抽象基类
 * 定义了所有文档资源的公共属性和方法
 */
public abstract class Document implements Borrowable, Serializable {
    private static final long serialVersionUID = 1L;
    
    // 私有属性 - 体现封装特性
    private String documentId;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private String description;
    private LocalDate publishDate;
    private LocalDateTime createTime;
    private boolean isAvailable;
    
    // 借阅相关属性
    private User currentBorrower;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    
    /**
     * 构造方法
     */
    public Document(String documentId, String title, String author, String publisher,
                   String category, String description, LocalDate publishDate) {
        this.documentId = documentId;
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.category = category;
        this.description = description;
        this.publishDate = publishDate;
        this.createTime = LocalDateTime.now();
        this.isAvailable = true;
    }
    
    // 抽象方法 - 不同文档类型有不同的实现（体现多态）
    
    /**
     * 获取文档类型
     * @return 文档类型
     */
    public abstract String getDocumentType();
    
    /**
     * 获取访问权限级别
     * @return 权限级别（PUBLIC, INTERNAL, ARCHIVE）
     */
    public abstract String getAccessLevel();
    
    /**
     * 检查用户是否有权限访问此文档
     * @param user 用户
     * @return 是否有权限访问
     */
    public abstract boolean checkAccessPermission(User user);
    
    /**
     * 获取文档的显示信息
     * @return 显示信息
     */
    public abstract String getDisplayInfo();
    
    // 实现Borrowable接口的方法
    
    @Override
    public boolean borrow(User user, LocalDate borrowDate) {
        if (!isAvailableFor(user)) {
            return false;
        }
          this.currentBorrower = user;
        this.borrowDate = borrowDate;
        this.dueDate = borrowDate.plusDays(user.getMaxBorrowDays());
        this.isAvailable = false;
        
        // 添加到用户的借阅记录
        user.addBorrowRecord(this.documentId, this.title, borrowDate, this.dueDate);
        
        return true;
    }
    
    @Override
    public boolean returnResource(User user, LocalDate returnDate) {
        if (currentBorrower == null || !currentBorrower.getUserId().equals(user.getUserId())) {
            return false;
        }
          this.currentBorrower = null;
        this.borrowDate = null;
        this.dueDate = null;
        this.isAvailable = true;
        
        // 标记用户的借阅记录为已归还
        user.returnDocument(this.documentId, returnDate);
        
        return true;
    }
    
    @Override
    public boolean extend(User user, int extendDays) {
        if (currentBorrower == null || !currentBorrower.getUserId().equals(user.getUserId())) {
            return false;
        }
        
        if (isOverdue()) {
            return false; // 已逾期不能续借
        }
        
        this.dueDate = this.dueDate.plusDays(extendDays);
        return true;
    }
    
    @Override
    public boolean isAvailableFor(User user) {
        return isAvailable && checkAccessPermission(user) && user.canBorrowMore();
    }
    
    @Override
    public User getCurrentBorrower() {
        return currentBorrower;
    }
    
    @Override
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    @Override
    public boolean isOverdue() {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }
    
    // 公共方法
    
    /**
     * 搜索匹配
     * @param keyword 关键词
     * @return 是否匹配
     */
    public boolean matches(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return false;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        return title.toLowerCase().contains(lowerKeyword) ||
               author.toLowerCase().contains(lowerKeyword) ||
               category.toLowerCase().contains(lowerKeyword) ||
               description.toLowerCase().contains(lowerKeyword);
    }
    
    /**
     * 关键词匹配方法（为演示接口功能）
     * @param keyword 关键词
     * @return 是否匹配
     */
    public boolean matchesKeyword(String keyword) {
        return matches(keyword);
    }
    
    /**
     * 获取可搜索内容（为演示接口功能）
     * @return 可搜索的文本内容
     */
    public String getSearchableContent() {
        return String.format("%s %s %s %s %s", 
                           title, author, category, description, publisher);
    }
    
    /**
     * 获取审计信息（为演示接口功能）
     * @return 审计信息字符串
     */
    public String getAuditInfo() {
        return String.format("文档ID: %s, 标题: %s, 创建时间: %s, 当前状态: %s", 
                           documentId, title, createTime.toString(), 
                           isAvailable ? "可借阅" : "已借出");
    }
      /**
     * 文档归还方法（简化版，为演示接口功能）
     * @return 归还是否成功
     */
    public boolean returnDocument() {
        if (currentBorrower != null) {
            return returnResource(currentBorrower, LocalDate.now());
        }
        return false;
    }
    
    // Getter和Setter方法
    
    public String getDocumentId() {
        return documentId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDate getPublishDate() {
        return publishDate;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    @Override
    public String toString() {
        return String.format("Document{id='%s', title='%s', author='%s', type='%s', " +
                           "level='%s', available=%s}", 
                           documentId, title, author, getDocumentType(), 
                           getAccessLevel(), isAvailable);
    }
}
