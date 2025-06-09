package main.service;

import main.model.document.Document;
import main.model.user.User;
import main.model.system.SystemConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * 借阅服务
 * 支持多线程并发处理借阅请求
 */
public class BorrowingService {
    
    private final Map<String, Document> documentRepository;
    private final Map<String, User> userRepository;
    private final AuditService auditService;
    private final ExecutorService threadPool;
    private final Map<String, ReentrantLock> documentLocks;
    
    public BorrowingService() {
        this.documentRepository = new ConcurrentHashMap<>();
        this.userRepository = new ConcurrentHashMap<>();
        this.auditService = AuditService.getInstance();
        this.threadPool = Executors.newFixedThreadPool(10); // 10个工作线程
        this.documentLocks = new ConcurrentHashMap<>();
    }
    
    /**
     * 添加文档到借阅库
     */
    public void addDocument(Document document) {
        documentRepository.put(document.getDocumentId(), document);
        documentLocks.put(document.getDocumentId(), new ReentrantLock());
    }
    
    /**
     * 添加用户到用户库
     */
    public void addUser(User user) {
        userRepository.put(user.getUserId(), user);
    }
    
    /**
     * 异步借阅处理
     */
    public Future<BorrowResult> borrowAsync(String userId, String documentId) {
        return threadPool.submit(() -> {
            return processBorrow(userId, documentId);
        });
    }
    
    /**
     * 同步借阅处理
     */
    public BorrowResult borrowSync(String userId, String documentId) {
        return processBorrow(userId, documentId);
    }
    
    /**
     * 处理借阅请求的核心逻辑
     */
    private BorrowResult processBorrow(String userId, String documentId) {
        // 获取文档锁，确保线程安全
        ReentrantLock lock = documentLocks.get(documentId);
        if (lock == null) {
            return new BorrowResult(false, "文档不存在");
        }
        
        lock.lock();
        try {
            Thread.sleep(100); // 模拟处理时间
            
            User user = userRepository.get(userId);
            Document document = documentRepository.get(documentId);
            
            if (user == null) {
                return new BorrowResult(false, "用户不存在");
            }
            
            if (document == null) {
                return new BorrowResult(false, "文档不存在");
            }
            
            // 检查借阅权限和条件
            if (!document.isAvailableFor(user)) {
                String reason = "借阅失败：";
                if (!document.isAvailable()) {
                    reason += "文档已被借出";
                } else if (!document.checkAccessPermission(user)) {
                    reason += "权限不足";
                } else if (!user.canBorrowMore()) {
                    reason += "已达到借阅上限";
                }
                return new BorrowResult(false, reason);
            }
            
            // 执行借阅
            LocalDate borrowDate = LocalDate.now();
            boolean success = document.borrow(user, borrowDate);
            
            if (success) {
                // 记录审计日志
                auditService.logOperation(userId, SystemConfig.OperationTypes.BORROW, 
                                         documentId, LocalDateTime.now(), 
                                         String.format("借阅成功，到期日：%s", document.getDueDate()));
                
                return new BorrowResult(true, String.format("借阅成功！文档：%s，到期日：%s", 
                                                           document.getTitle(), document.getDueDate()));
            } else {
                return new BorrowResult(false, "借阅处理失败");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new BorrowResult(false, "借阅处理被中断");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 异步归还处理
     */
    public Future<BorrowResult> returnAsync(String userId, String documentId) {
        return threadPool.submit(() -> {
            return processReturn(userId, documentId);
        });
    }
    
    /**
     * 同步归还处理
     */
    public BorrowResult returnSync(String userId, String documentId) {
        return processReturn(userId, documentId);
    }
    
    /**
     * 处理归还请求的核心逻辑
     */
    private BorrowResult processReturn(String userId, String documentId) {
        ReentrantLock lock = documentLocks.get(documentId);
        if (lock == null) {
            return new BorrowResult(false, "文档不存在");
        }
        
        lock.lock();
        try {
            Thread.sleep(50); // 模拟处理时间
            
            User user = userRepository.get(userId);
            Document document = documentRepository.get(documentId);
            
            if (user == null || document == null) {
                return new BorrowResult(false, "用户或文档不存在");
            }
            
            // 检查是否是当前借阅者
            if (document.getCurrentBorrower() == null || 
                !document.getCurrentBorrower().getUserId().equals(userId)) {
                return new BorrowResult(false, "您没有借阅此文档");
            }
            
            // 执行归还
            LocalDate returnDate = LocalDate.now();
            boolean success = document.returnResource(user, returnDate);
            
            if (success) {
                // 检查是否逾期
                String message = "归还成功！";
                if (document.getDueDate() != null && returnDate.isAfter(document.getDueDate())) {
                    long overdueDays = returnDate.toEpochDay() - document.getDueDate().toEpochDay();
                    message += String.format(" 逾期 %d 天", overdueDays);
                }
                
                // 记录审计日志
                auditService.logOperation(userId, SystemConfig.OperationTypes.RETURN, 
                                         documentId, LocalDateTime.now(), message);
                
                return new BorrowResult(true, message);
            } else {
                return new BorrowResult(false, "归还处理失败");
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new BorrowResult(false, "归还处理被中断");
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 续借处理
     */
    public BorrowResult extendBorrow(String userId, String documentId, int extendDays) {
        ReentrantLock lock = documentLocks.get(documentId);
        if (lock == null) {
            return new BorrowResult(false, "文档不存在");
        }
        
        lock.lock();
        try {
            User user = userRepository.get(userId);
            Document document = documentRepository.get(documentId);
            
            if (user == null || document == null) {
                return new BorrowResult(false, "用户或文档不存在");
            }
            
            // 检查是否是当前借阅者
            if (document.getCurrentBorrower() == null || 
                !document.getCurrentBorrower().getUserId().equals(userId)) {
                return new BorrowResult(false, "您没有借阅此文档");
            }
            
            // 执行续借
            boolean success = document.extend(user, extendDays);
            
            if (success) {
                auditService.logOperation(userId, SystemConfig.OperationTypes.EXTEND, 
                                         documentId, LocalDateTime.now(), 
                                         String.format("续借 %d 天，新到期日：%s", extendDays, document.getDueDate()));
                
                return new BorrowResult(true, String.format("续借成功！延长 %d 天，新到期日：%s", 
                                                           extendDays, document.getDueDate()));
            } else {
                return new BorrowResult(false, "续借失败，可能已逾期或达到续借次数上限");
            }
            
        } finally {
            lock.unlock();
        }
    }
    
    /**
     * 获取用户的借阅列表
     */
    public List<Document> getUserBorrowedDocuments(String userId) {
        User user = userRepository.get(userId);
        if (user == null) {
            return new ArrayList<>();        }
        
        List<Document> borrowedDocs = new ArrayList<>();
        for (String resourceId : user.getCurrentBorrowedDocumentIds()) {
            Document doc = documentRepository.get(resourceId);
            if (doc != null) {
                borrowedDocs.add(doc);
            }
        }
        return borrowedDocs;
    }
    
    /**
     * 获取逾期文档列表
     */
    public List<Document> getOverdueDocuments() {
        List<Document> overdueList = new ArrayList<>();
        for (Document doc : documentRepository.values()) {
            if (doc.isOverdue()) {
                overdueList.add(doc);
            }
        }
        return overdueList;
    }
    
    /**
     * 关闭服务
     */
    public void shutdown() {
        threadPool.shutdown();
    }
    
    /**
     * 借阅结果类
     */
    public static class BorrowResult {
        private final boolean success;
        private final String message;
        
        public BorrowResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() {
            return success;
        }
        
        public String getMessage() {
            return message;
        }
        
        @Override
        public String toString() {
            return String.format("BorrowResult{success=%s, message='%s'}", success, message);
        }
    }
}
