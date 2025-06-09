package main.service;

import main.interfaces.Auditable;
import main.model.system.SystemConfig;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 审计日志服务
 * 实现系统操作的完整日志记录
 */
public class AuditService implements Auditable {
    
    private static AuditService instance;
    private Map<String, List<String>> userOperationHistory;
    private Map<String, List<String>> resourceAccessHistory;
    private DateTimeFormatter formatter;
    
    private AuditService() {
        this.userOperationHistory = new HashMap<>();
        this.resourceAccessHistory = new HashMap<>();
        this.formatter = DateTimeFormatter.ofPattern(SystemConfig.DATETIME_FORMAT);
    }
    
    /**
     * 获取单例实例
     */
    public static synchronized AuditService getInstance() {
        if (instance == null) {
            instance = new AuditService();
        }
        return instance;
    }
    
    @Override
    public void logOperation(String userId, String operation, String resourceId, 
                           LocalDateTime timestamp, String details) {
        String logEntry = String.format("[%s] 用户: %s | 操作: %s | 资源: %s | 详情: %s", 
                                       timestamp.format(formatter), userId, operation, 
                                       resourceId != null ? resourceId : "N/A", details);
        
        // 添加到用户操作历史
        userOperationHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(logEntry);
        
        // 如果有资源ID，也添加到资源访问历史
        if (resourceId != null) {
            resourceAccessHistory.computeIfAbsent(resourceId, k -> new ArrayList<>()).add(logEntry);
        }
        
        // 写入日志文件
        writeToLogFile(logEntry, SystemConfig.AUDIT_LOG_FILE);
        
        // 控制台输出（开发调试用）
        System.out.println("【审计日志】" + logEntry);
    }
    
    @Override
    public void logLogin(String userId, LocalDateTime timestamp, String ipAddress, boolean success) {
        String status = success ? "成功" : "失败";
        String logEntry = String.format("[%s] 登录%s | 用户: %s | IP: %s", 
                                       timestamp.format(formatter), status, userId, ipAddress);
        
        userOperationHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(logEntry);
        writeToLogFile(logEntry, SystemConfig.AUDIT_LOG_FILE);
        
        System.out.println("【登录日志】" + logEntry);
    }
    
    @Override
    public void logAccess(String userId, String resourceId, LocalDateTime timestamp, String accessType) {
        String logEntry = String.format("[%s] 访问资源 | 用户: %s | 资源: %s | 类型: %s", 
                                       timestamp.format(formatter), userId, resourceId, accessType);
        
        userOperationHistory.computeIfAbsent(userId, k -> new ArrayList<>()).add(logEntry);
        resourceAccessHistory.computeIfAbsent(resourceId, k -> new ArrayList<>()).add(logEntry);
        writeToLogFile(logEntry, SystemConfig.AUDIT_LOG_FILE);
        
        System.out.println("【访问日志】" + logEntry);
    }
    
    @Override
    public String getOperationHistory(String userId) {
        List<String> history = userOperationHistory.get(userId);
        if (history == null || history.isEmpty()) {
            return "用户 " + userId + " 暂无操作记录";
        }
        
        StringBuilder sb = new StringBuilder("用户 " + userId + " 的操作历史：\n");
        for (String entry : history) {
            sb.append(entry).append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String getAccessHistory(String resourceId) {
        List<String> history = resourceAccessHistory.get(resourceId);
        if (history == null || history.isEmpty()) {
            return "资源 " + resourceId + " 暂无访问记录";
        }
        
        StringBuilder sb = new StringBuilder("资源 " + resourceId + " 的访问历史：\n");
        for (String entry : history) {
            sb.append(entry).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * 写入日志文件
     */
    private void writeToLogFile(String logEntry, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("写入日志文件失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取系统统计信息
     */
    public String getSystemStatistics() {
        int totalUsers = userOperationHistory.size();
        int totalResources = resourceAccessHistory.size();
        int totalOperations = userOperationHistory.values().stream()
                                                 .mapToInt(List::size)
                                                 .sum();
        
        return String.format("系统统计信息：\n" +
                           "- 活跃用户数: %d\n" +
                           "- 被访问资源数: %d\n" +
                           "- 总操作次数: %d\n", 
                           totalUsers, totalResources, totalOperations);
    }
    
    /**
     * 清空指定用户的操作历史
     */
    public void clearUserHistory(String userId) {
        userOperationHistory.remove(userId);
        System.out.println("已清空用户 " + userId + " 的操作历史");
    }
    
    /**
     * 清空指定资源的访问历史
     */
    public void clearResourceHistory(String resourceId) {
        resourceAccessHistory.remove(resourceId);
        System.out.println("已清空资源 " + resourceId + " 的访问历史");
    }
}
