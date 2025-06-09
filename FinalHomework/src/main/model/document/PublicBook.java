package main.model.document;

import main.model.user.User;
import java.time.LocalDate;

/**
 * 公开图书抽象类
 * 所有用户都可以访问的图书资源基类
 */
public abstract class PublicBook extends Document {
    
    private String isbn;
    private int totalPages;
    private String language;
    
    /**
     * 构造方法
     */
    public PublicBook(String documentId, String title, String author, String publisher,
                     String category, String description, LocalDate publishDate,
                     String isbn, int totalPages, String language) {
        super(documentId, title, author, publisher, category, description, publishDate);
        this.isbn = isbn;
        this.totalPages = totalPages;
        this.language = language;
    }
    
    @Override
    public String getAccessLevel() {
        return "PUBLIC";
    }
    
    @Override
    public boolean checkAccessPermission(User user) {
        // 所有用户都可以访问公开图书
        return user.hasAccessPermission("PUBLIC");
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("【公开图书】%s - %s (%s) | ISBN: %s | %d页 | %s", 
                           getTitle(), getAuthor(), getPublisher(), isbn, totalPages, language);
    }
    
    // Getter和Setter方法
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public int getTotalPages() {
        return totalPages;
    }
    
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
}
