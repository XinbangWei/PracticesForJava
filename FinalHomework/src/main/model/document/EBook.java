package main.model.document;

import java.time.LocalDate;

/**
 * 电子书类
 * 可以在线阅读或下载的电子图书
 */
public class EBook extends PublicBook {
    
    private String fileFormat; // 文件格式 (PDF, EPUB, etc.)
    private long fileSize; // 文件大小 (字节)
    private String downloadUrl; // 下载链接
    private int downloadCount; // 下载次数
    private boolean allowDownload; // 是否允许下载
    private boolean allowPrint; // 是否允许打印
    
    /**
     * 构造方法
     */
    public EBook(String documentId, String title, String author, String publisher,
                String category, String description, LocalDate publishDate,
                String isbn, int totalPages, String language,
                String fileFormat, long fileSize, String downloadUrl) {
        super(documentId, title, author, publisher, category, description, publishDate,
              isbn, totalPages, language);
        this.fileFormat = fileFormat;
        this.fileSize = fileSize;
        this.downloadUrl = downloadUrl;
        this.downloadCount = 0;
        this.allowDownload = true;
        this.allowPrint = true;
    }
    
    @Override
    public String getDocumentType() {
        return "电子书";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("%s | 格式: %s | 大小: %s | 下载次数: %d", 
                           super.getDisplayInfo(), fileFormat, 
                           formatFileSize(fileSize), downloadCount);
    }
    
    // 特有方法
    
    /**
     * 在线阅读
     * @param userId 用户ID
     * @return 阅读链接
     */
    public String readOnline(String userId) {
        System.out.println(String.format("用户 %s 开始在线阅读电子书：%s", userId, getTitle()));
        return downloadUrl + "?action=read&userId=" + userId;
    }
    
    /**
     * 下载电子书
     * @param userId 用户ID
     * @return 下载链接，如果不允许下载则返回null
     */
    public String download(String userId) {
        if (!allowDownload) {
            System.out.println(String.format("电子书 %s 不允许下载", getTitle()));
            return null;
        }
        
        downloadCount++;
        System.out.println(String.format("用户 %s 下载电子书：%s (第%d次下载)", 
                                        userId, getTitle(), downloadCount));
        return downloadUrl + "?action=download&userId=" + userId;
    }
    
    /**
     * 打印电子书
     * @param userId 用户ID
     * @param pages 页面范围
     * @return 是否允许打印
     */
    public boolean print(String userId, String pages) {
        if (!allowPrint) {
            System.out.println(String.format("电子书 %s 不允许打印", getTitle()));
            return false;
        }
        
        System.out.println(String.format("用户 %s 打印电子书 %s 的页面：%s", 
                                        userId, getTitle(), pages));
        return true;
    }
    
    /**
     * 格式化文件大小显示
     * @param bytes 字节数
     * @return 格式化后的大小字符串
     */
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    // Getter和Setter方法
    
    public String getFileFormat() {
        return fileFormat;
    }
    
    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }
    
    public long getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public int getDownloadCount() {
        return downloadCount;
    }
    
    public boolean isAllowDownload() {
        return allowDownload;
    }
    
    public void setAllowDownload(boolean allowDownload) {
        this.allowDownload = allowDownload;
    }
    
    public boolean isAllowPrint() {
        return allowPrint;
    }
    
    public void setAllowPrint(boolean allowPrint) {
        this.allowPrint = allowPrint;
    }
    
    @Override
    public String toString() {
        return String.format("EBook{id='%s', title='%s', author='%s', " +
                           "format='%s', size='%s', downloads=%d, available=%s}", 
                           getDocumentId(), getTitle(), getAuthor(), 
                           fileFormat, formatFileSize(fileSize), downloadCount, isAvailable());
    }
}
