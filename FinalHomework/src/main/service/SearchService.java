package main.service;

import main.interfaces.Searchable;
import main.model.document.Document;
import main.model.user.User;
import main.model.system.SystemConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 搜索服务
 * 实现文档搜索功能，支持权限控制
 */
public class SearchService implements Searchable {
    
    private List<Document> documentRepository;
    private AuditService auditService;
    
    public SearchService() {
        this.documentRepository = new ArrayList<>();
        this.auditService = AuditService.getInstance();
    }
    
    /**
     * 添加文档到搜索库
     */
    public void addDocument(Document document) {
        documentRepository.add(document);
    }
    
    /**
     * 移除文档
     */
    public void removeDocument(String documentId) {
        documentRepository.removeIf(doc -> doc.getDocumentId().equals(documentId));
    }
    
    /**
     * 根据用户权限过滤搜索结果
     */
    private List<Object> filterByPermission(List<Document> documents, User user) {
        return documents.stream()
                       .filter(doc -> doc.checkAccessPermission(user))
                       .limit(SystemConfig.MAX_SEARCH_RESULTS)
                       .collect(Collectors.toList());
    }
    
    @Override
    public List<Object> searchByKeyword(String keyword) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.matches(keyword))
                                                  .collect(Collectors.toList());
        return new ArrayList<>(results);
    }
    
    /**
     * 带权限控制的关键词搜索
     */
    public List<Object> searchByKeyword(String keyword, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.matches(keyword))
                                                  .collect(Collectors.toList());
        
        // 记录搜索操作
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "关键词搜索: " + keyword);
        
        return filterByPermission(results, user);
    }
    
    @Override
    public List<Object> searchByTitle(String title) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getTitle().toLowerCase()
                                                               .contains(title.toLowerCase()))
                                                  .collect(Collectors.toList());
        return new ArrayList<>(results);
    }
    
    /**
     * 带权限控制的标题搜索
     */
    public List<Object> searchByTitle(String title, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getTitle().toLowerCase()
                                                               .contains(title.toLowerCase()))
                                                  .collect(Collectors.toList());
        
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "标题搜索: " + title);
        
        return filterByPermission(results, user);
    }
    
    @Override
    public List<Object> searchByAuthor(String author) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getAuthor().toLowerCase()
                                                               .contains(author.toLowerCase()))
                                                  .collect(Collectors.toList());
        return new ArrayList<>(results);
    }
    
    /**
     * 带权限控制的作者搜索
     */
    public List<Object> searchByAuthor(String author, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getAuthor().toLowerCase()
                                                               .contains(author.toLowerCase()))
                                                  .collect(Collectors.toList());
        
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "作者搜索: " + author);
        
        return filterByPermission(results, user);
    }
    
    @Override
    public List<Object> searchByCategory(String category) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getCategory().toLowerCase()
                                                               .contains(category.toLowerCase()))
                                                  .collect(Collectors.toList());
        return new ArrayList<>(results);
    }
    
    /**
     * 带权限控制的分类搜索
     */
    public List<Object> searchByCategory(String category, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getCategory().toLowerCase()
                                                               .contains(category.toLowerCase()))
                                                  .collect(Collectors.toList());
        
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "分类搜索: " + category);
        
        return filterByPermission(results, user);
    }
    
    @Override
    public List<Object> advancedSearch(String title, String author, String category, String keyword) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> {
                                                      boolean matches = true;
                                                      if (title != null && !title.isEmpty()) {
                                                          matches &= doc.getTitle().toLowerCase()
                                                                       .contains(title.toLowerCase());
                                                      }
                                                      if (author != null && !author.isEmpty()) {
                                                          matches &= doc.getAuthor().toLowerCase()
                                                                       .contains(author.toLowerCase());
                                                      }
                                                      if (category != null && !category.isEmpty()) {
                                                          matches &= doc.getCategory().toLowerCase()
                                                                       .contains(category.toLowerCase());
                                                      }
                                                      if (keyword != null && !keyword.isEmpty()) {
                                                          matches &= doc.matches(keyword);
                                                      }
                                                      return matches;
                                                  })
                                                  .collect(Collectors.toList());
        return new ArrayList<>(results);
    }
    
    /**
     * 带权限控制的高级搜索
     */
    public List<Object> advancedSearch(String title, String author, String category, 
                                      String keyword, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> {
                                                      boolean matches = true;
                                                      if (title != null && !title.isEmpty()) {
                                                          matches &= doc.getTitle().toLowerCase()
                                                                       .contains(title.toLowerCase());
                                                      }
                                                      if (author != null && !author.isEmpty()) {
                                                          matches &= doc.getAuthor().toLowerCase()
                                                                       .contains(author.toLowerCase());
                                                      }
                                                      if (category != null && !category.isEmpty()) {
                                                          matches &= doc.getCategory().toLowerCase()
                                                                       .contains(category.toLowerCase());
                                                      }
                                                      if (keyword != null && !keyword.isEmpty()) {
                                                          matches &= doc.matches(keyword);
                                                      }
                                                      return matches;
                                                  })
                                                  .collect(Collectors.toList());
        
        String searchCriteria = String.format("标题:%s, 作者:%s, 分类:%s, 关键词:%s", 
                                             title, author, category, keyword);
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "高级搜索: " + searchCriteria);
        
        return filterByPermission(results, user);
    }
    
    /**
     * 按文档类型搜索
     */
    public List<Object> searchByDocumentType(String documentType, User user) {
        List<Document> results = documentRepository.stream()
                                                  .filter(doc -> doc.getDocumentType().equals(documentType))
                                                  .collect(Collectors.toList());
        
        auditService.logOperation(user.getUserId(), SystemConfig.OperationTypes.SEARCH, 
                                 null, java.time.LocalDateTime.now(), 
                                 "文档类型搜索: " + documentType);
        
        return filterByPermission(results, user);
    }
    
    /**
     * 获取用户可访问的所有文档
     */
    public List<Object> getAllAccessibleDocuments(User user) {
        List<Document> allDocuments = new ArrayList<>(documentRepository);
        return filterByPermission(allDocuments, user);
    }
    
    /**
     * 获取搜索统计信息
     */
    public String getSearchStatistics() {
        return String.format("搜索库统计：\n" +
                           "- 总文档数: %d\n" +
                           "- 公开文档: %d\n" +
                           "- 内部资料: %d\n" +
                           "- 档案资料: %d\n", 
                           documentRepository.size(),
                           documentRepository.stream().mapToInt(doc -> 
                               "PUBLIC".equals(doc.getAccessLevel()) ? 1 : 0).sum(),
                           documentRepository.stream().mapToInt(doc -> 
                               "INTERNAL".equals(doc.getAccessLevel()) ? 1 : 0).sum(),
                           documentRepository.stream().mapToInt(doc -> 
                               "ARCHIVE".equals(doc.getAccessLevel()) ? 1 : 0).sum());
    }
}
