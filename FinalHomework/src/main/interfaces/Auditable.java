package main.interfaces;

import java.time.LocalDateTime;

/**
 * 可审计接口
 * 定义了系统操作日志记录的标准方法
 */
public interface Auditable {
    
    /**
     * 记录操作日志
     * @param userId 用户ID
     * @param operation 操作类型
     * @param resourceId 资源ID
     * @param timestamp 时间戳
     * @param details 详细信息
     */
    void logOperation(String userId, String operation, String resourceId, 
                     LocalDateTime timestamp, String details);
    
    /**
     * 记录登录日志
     * @param userId 用户ID
     * @param timestamp 登录时间
     * @param ipAddress IP地址
     * @param success 是否成功
     */
    void logLogin(String userId, LocalDateTime timestamp, String ipAddress, boolean success);
    
    /**
     * 记录访问日志
     * @param userId 用户ID
     * @param resourceId 访问的资源ID
     * @param timestamp 访问时间
     * @param accessType 访问类型（查看、下载等）
     */
    void logAccess(String userId, String resourceId, LocalDateTime timestamp, String accessType);
    
    /**
     * 获取用户操作历史
     * @param userId 用户ID
     * @return 操作历史记录
     */
    String getOperationHistory(String userId);
    
    /**
     * 获取资源访问历史
     * @param resourceId 资源ID
     * @return 访问历史记录
     */
    String getAccessHistory(String resourceId);
}
