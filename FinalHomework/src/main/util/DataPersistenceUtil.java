package main.util;

import main.model.user.User;
import main.model.document.Document;
import main.model.system.SystemConfig;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

/**
 * 数据持久化工具类
 * 处理数据的序列化存储和读取
 */
public class DataPersistenceUtil {
    
    /**
     * 保存用户数据到文件
     */
    public static boolean saveUsers(List<User> users) {
        try {
            // 确保数据目录存在
            createDataDirectory();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SystemConfig.USERS_FILE))) {
                oos.writeObject(users);
                System.out.println("用户数据保存成功：" + users.size() + " 个用户");
                return true;
            }
        } catch (IOException e) {
            System.err.println("保存用户数据失败：" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从文件读取用户数据
     */
    @SuppressWarnings("unchecked")
    public static List<User> loadUsers() {
        File file = new File(SystemConfig.USERS_FILE);
        if (!file.exists()) {
            System.out.println("用户数据文件不存在，返回空列表");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SystemConfig.USERS_FILE))) {
            List<User> users = (List<User>) ois.readObject();
            System.out.println("用户数据加载成功：" + users.size() + " 个用户");
            return users;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载用户数据失败：" + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 保存文档数据到文件
     */
    public static boolean saveDocuments(List<Document> documents) {
        try {
            createDataDirectory();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(SystemConfig.DOCUMENTS_FILE))) {
                oos.writeObject(documents);
                System.out.println("文档数据保存成功：" + documents.size() + " 个文档");
                return true;
            }
        } catch (IOException e) {
            System.err.println("保存文档数据失败：" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从文件读取文档数据
     */
    @SuppressWarnings("unchecked")
    public static List<Document> loadDocuments() {
        File file = new File(SystemConfig.DOCUMENTS_FILE);
        if (!file.exists()) {
            System.out.println("文档数据文件不存在，返回空列表");
            return new ArrayList<>();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SystemConfig.DOCUMENTS_FILE))) {
            List<Document> documents = (List<Document>) ois.readObject();
            System.out.println("文档数据加载成功：" + documents.size() + " 个文档");
            return documents;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("加载文档数据失败：" + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    /**
     * 保存单个对象到文件
     */
    public static boolean saveObject(Object obj, String fileName) {
        try {
            createDataDirectory();
            
            try (ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(fileName))) {
                oos.writeObject(obj);
                System.out.println("对象保存成功到文件：" + fileName);
                return true;
            }
        } catch (IOException e) {
            System.err.println("保存对象到文件失败：" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 从文件读取单个对象
     */
    public static Object loadObject(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("文件不存在：" + fileName);
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(fileName))) {
            Object obj = ois.readObject();
            System.out.println("对象加载成功从文件：" + fileName);
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("从文件加载对象失败：" + e.getMessage());
            return null;
        }
    }
    
    /**
     * 创建数据目录
     */
    private static void createDataDirectory() {
        File dataDir = new File(SystemConfig.DATA_DIR);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdirs();
            if (created) {
                System.out.println("数据目录创建成功：" + SystemConfig.DATA_DIR);
            } else {
                System.err.println("数据目录创建失败：" + SystemConfig.DATA_DIR);
            }
        }
    }
    
    /**
     * 备份数据文件
     */
    public static boolean backupData() {
        try {
            String timestamp = java.time.LocalDateTime.now()
                                  .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            
            // 备份用户数据
            File usersFile = new File(SystemConfig.USERS_FILE);
            if (usersFile.exists()) {
                String backupUsersFile = SystemConfig.DATA_DIR + "users_backup_" + timestamp + ".dat";
                copyFile(usersFile, new File(backupUsersFile));
            }
            
            // 备份文档数据
            File documentsFile = new File(SystemConfig.DOCUMENTS_FILE);
            if (documentsFile.exists()) {
                String backupDocumentsFile = SystemConfig.DATA_DIR + "documents_backup_" + timestamp + ".dat";
                copyFile(documentsFile, new File(backupDocumentsFile));
            }
            
            System.out.println("数据备份完成，时间戳：" + timestamp);
            return true;
        } catch (IOException e) {
            System.err.println("数据备份失败：" + e.getMessage());
            return false;
        }
    }
    
    /**
     * 复制文件
     */
    private static void copyFile(File source, File destination) throws IOException {
        try (FileInputStream fis = new FileInputStream(source);
             FileOutputStream fos = new FileOutputStream(destination)) {
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        }
    }
    
    /**
     * 清空所有数据文件
     */
    public static boolean clearAllData() {
        boolean success = true;
        
        File usersFile = new File(SystemConfig.USERS_FILE);
        if (usersFile.exists() && !usersFile.delete()) {
            System.err.println("删除用户数据文件失败");
            success = false;
        }
        
        File documentsFile = new File(SystemConfig.DOCUMENTS_FILE);
        if (documentsFile.exists() && !documentsFile.delete()) {
            System.err.println("删除文档数据文件失败");
            success = false;
        }
        
        if (success) {
            System.out.println("所有数据文件已清空");
        }
        
        return success;
    }
    
    /**
     * 检查数据文件状态
     */
    public static String getDataFileStatus() {
        StringBuilder status = new StringBuilder("数据文件状态：\n");
        
        File usersFile = new File(SystemConfig.USERS_FILE);
        status.append(String.format("- 用户数据文件: %s (%s)\n", 
                                   usersFile.exists() ? "存在" : "不存在",
                                   usersFile.exists() ? formatFileSize(usersFile.length()) : "0 B"));
        
        File documentsFile = new File(SystemConfig.DOCUMENTS_FILE);
        status.append(String.format("- 文档数据文件: %s (%s)\n", 
                                   documentsFile.exists() ? "存在" : "不存在",
                                   documentsFile.exists() ? formatFileSize(documentsFile.length()) : "0 B"));
        
        File logsFile = new File(SystemConfig.LOGS_FILE);
        status.append(String.format("- 系统日志文件: %s (%s)\n", 
                                   logsFile.exists() ? "存在" : "不存在",
                                   logsFile.exists() ? formatFileSize(logsFile.length()) : "0 B"));
        
        return status.toString();
    }
    
    /**
     * 格式化文件大小
     */
    private static String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
}
