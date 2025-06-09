package main.model.document;

import java.time.LocalDate;

/**
 * 实体书类
 * 需要到馆借阅的纸质图书
 */
public class PhysicalBook extends PublicBook {
    
    private String location; // 存放位置
    private String shelfNumber; // 书架号
    private boolean isDamaged; // 是否损坏
    private String condition; // 图书状态描述
    
    /**
     * 构造方法
     */
    public PhysicalBook(String documentId, String title, String author, String publisher,
                       String category, String description, LocalDate publishDate,
                       String isbn, int totalPages, String language,
                       String location, String shelfNumber) {
        super(documentId, title, author, publisher, category, description, publishDate,
              isbn, totalPages, language);
        this.location = location;
        this.shelfNumber = shelfNumber;
        this.isDamaged = false;
        this.condition = "良好";
    }
    
    @Override
    public String getDocumentType() {
        return "实体书";
    }
    
    @Override
    public String getDisplayInfo() {
        return String.format("%s | 位置: %s-%s | 状态: %s", 
                           super.getDisplayInfo(), location, shelfNumber, condition);
    }
    
    // 特有方法
    
    /**
     * 报告图书损坏
     * @param damageDescription 损坏描述
     */
    public void reportDamage(String damageDescription) {
        this.isDamaged = true;
        this.condition = "损坏: " + damageDescription;
        System.out.println(String.format("图书 %s 报告损坏：%s", getTitle(), damageDescription));
    }
    
    /**
     * 修复图书
     * @param repairDescription 修复描述
     */
    public void repair(String repairDescription) {
        this.isDamaged = false;
        this.condition = "已修复: " + repairDescription;
        System.out.println(String.format("图书 %s 已修复：%s", getTitle(), repairDescription));
    }
    
    /**
     * 更换位置
     * @param newLocation 新位置
     * @param newShelfNumber 新书架号
     */
    public void relocate(String newLocation, String newShelfNumber) {
        String oldLocation = this.location + "-" + this.shelfNumber;
        this.location = newLocation;
        this.shelfNumber = newShelfNumber;
        System.out.println(String.format("图书 %s 从 %s 移动到 %s-%s", 
                                        getTitle(), oldLocation, newLocation, newShelfNumber));
    }
    
    // Getter和Setter方法
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getShelfNumber() {
        return shelfNumber;
    }
    
    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }
    
    public boolean isDamaged() {
        return isDamaged;
    }
    
    public String getCondition() {
        return condition;
    }
    
    public void setCondition(String condition) {
        this.condition = condition;
    }
    
    @Override
    public String toString() {
        return String.format("PhysicalBook{id='%s', title='%s', author='%s', " +
                           "location='%s-%s', condition='%s', available=%s}", 
                           getDocumentId(), getTitle(), getAuthor(), 
                           location, shelfNumber, condition, isAvailable());
    }
}
