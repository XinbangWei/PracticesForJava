package main.model.document;

import java.time.LocalDate;

/**
 * 文档工厂类
 * 使用工厂设计模式创建不同类型的文档
 */
public class DocumentFactory {
    
    /**
     * 创建实体书
     */
    public static PhysicalBook createPhysicalBook(String documentId, String title, String author,
                                                 String publisher, String category, String description,
                                                 LocalDate publishDate, String isbn, int totalPages,
                                                 String language, String location, String shelfNumber) {
        return new PhysicalBook(documentId, title, author, publisher, category, description,
                               publishDate, isbn, totalPages, language, location, shelfNumber);
    }
    
    /**
     * 创建电子书
     */
    public static EBook createEBook(String documentId, String title, String author,
                                   String publisher, String category, String description,
                                   LocalDate publishDate, String isbn, int totalPages,
                                   String language, String fileFormat, long fileSize,
                                   String downloadUrl) {
        return new EBook(documentId, title, author, publisher, category, description,
                        publishDate, isbn, totalPages, language, fileFormat, fileSize, downloadUrl);
    }
    
    /**
     * 创建内部资料
     */
    public static InternalDocument createInternalDocument(String documentId, String title, String author,
                                                         String publisher, String category, String description,
                                                         LocalDate publishDate, String securityLevel,
                                                         String researchField, String projectCode,
                                                         String approvalNumber, String classifiedBy) {
        return new InternalDocument(documentId, title, author, publisher, category, description,
                                   publishDate, securityLevel, researchField, projectCode,
                                   approvalNumber, classifiedBy);
    }
    
    /**
     * 创建档案资料
     */
    public static ArchiveDocument createArchiveDocument(String documentId, String title, String author,
                                                       String publisher, String category, String description,
                                                       LocalDate publishDate, String archiveLevel,
                                                       String storageLocation, String archiveNumber,
                                                       String archivedBy, boolean requiresSpecialPermission) {
        return new ArchiveDocument(documentId, title, author, publisher, category, description,
                                  publishDate, archiveLevel, storageLocation, archiveNumber,
                                  archivedBy, requiresSpecialPermission);
    }
    
    /**
     * 根据类型字符串创建文档
     */
    public static Document createDocument(String type, String documentId, String title, String author,
                                         String publisher, String category, String description,
                                         LocalDate publishDate, Object... additionalParams) {
        switch (type.toUpperCase()) {
            case "PHYSICAL":
                if (additionalParams.length >= 5) {
                    return createPhysicalBook(documentId, title, author, publisher, category, description,
                                            publishDate, (String)additionalParams[0], (Integer)additionalParams[1],
                                            (String)additionalParams[2], (String)additionalParams[3], (String)additionalParams[4]);
                }
                break;
            case "EBOOK":
                if (additionalParams.length >= 5) {
                    return createEBook(documentId, title, author, publisher, category, description,
                                     publishDate, (String)additionalParams[0], (Integer)additionalParams[1],
                                     (String)additionalParams[2], (String)additionalParams[3], 
                                     (Long)additionalParams[4], (String)additionalParams[5]);
                }
                break;
            case "INTERNAL":
                if (additionalParams.length >= 4) {
                    return createInternalDocument(documentId, title, author, publisher, category, description,
                                                publishDate, (String)additionalParams[0], (String)additionalParams[1],
                                                (String)additionalParams[2], (String)additionalParams[3], (String)additionalParams[4]);
                }
                break;
            case "ARCHIVE":
                if (additionalParams.length >= 4) {
                    return createArchiveDocument(documentId, title, author, publisher, category, description,
                                               publishDate, (String)additionalParams[0], (String)additionalParams[1],
                                               (String)additionalParams[2], (String)additionalParams[3], (Boolean)additionalParams[4]);
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的文档类型: " + type);
        }
        throw new IllegalArgumentException("参数不足，无法创建 " + type + " 类型的文档");
    }
}