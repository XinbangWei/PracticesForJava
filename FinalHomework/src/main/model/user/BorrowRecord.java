package main.model.user;

import main.model.document.Document;
import java.time.LocalDate;
import java.io.Serializable;

/**
 * 借阅记录类
 * 存储用户借阅文档的详细信息
 */
public class BorrowRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String documentId;
    private String documentTitle;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private boolean isReturned;
    private int renewalCount;
    
    /**
     * 构造方法
     */
    public BorrowRecord(String documentId, String documentTitle, LocalDate borrowDate, LocalDate dueDate) {
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.isReturned = false;
        this.renewalCount = 0;
    }
    
    /**
     * 标记为已归还
     */
    public void markAsReturned(LocalDate returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true;
    }
    
    /**
     * 续借
     */
    public void renew(LocalDate newDueDate) {
        this.dueDate = newDueDate;
        this.renewalCount++;
    }
    
    /**
     * 检查是否逾期
     */
    public boolean isOverdue() {
        if (isReturned) {
            return returnDate.isAfter(dueDate);
        }
        return LocalDate.now().isAfter(dueDate);
    }
    
    /**
     * 获取剩余天数
     */
    public long getDaysRemaining() {
        if (isReturned) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }
    
    // Getters and Setters
    public String getDocumentId() {
        return documentId;
    }
    
    public String getDocumentTitle() {
        return documentTitle;
    }
    
    public LocalDate getBorrowDate() {
        return borrowDate;
    }
    
    public LocalDate getDueDate() {
        return dueDate;
    }
    
    public LocalDate getReturnDate() {
        return returnDate;
    }
    
    public boolean isReturned() {
        return isReturned;
    }
    
    public int getRenewalCount() {
        return renewalCount;
    }
    
    @Override
    public String toString() {
        return String.format("借阅记录: %s (%s) - 借阅日期: %s, 到期日期: %s, 状态: %s", 
                           documentTitle, documentId, borrowDate, dueDate, 
                           isReturned ? "已归还" : "借阅中");
    }
}
