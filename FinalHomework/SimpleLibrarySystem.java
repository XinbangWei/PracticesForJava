package main;

import java.util.*;
import java.time.LocalDate;

/**
 * 简化的图书管理系统 - 展示面向对象编程核心概念
 * 包含：封装、继承、多态、抽象类、接口、集合框架使用
 */

// 1. 接口定义 - 展示多态性
interface Borrowable {
    boolean canBorrow();
    void borrowBy(String userId);
    void returnByUser();
}

interface Searchable {
    boolean matches(String keyword);
}

// 2. 抽象基类 - 展示继承和封装
abstract class Document implements Borrowable, Searchable {
    // 封装：私有属性
    private String id;
    private String title;
    private String author;
    private boolean borrowed;
    private String borrowerId;
    private LocalDate borrowDate;
    
    // 构造函数
    public Document(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowed = false;
    }
    
    // 封装：公有访问器方法
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return borrowed; }
    public String getBorrowerId() { return borrowerId; }
    
    // 实现接口方法
    @Override
    public boolean canBorrow() {
        return !borrowed;
    }
    
    @Override
    public void borrowBy(String userId) {
        if (canBorrow()) {
            this.borrowed = true;
            this.borrowerId = userId;
            this.borrowDate = LocalDate.now();
        }
    }
    
    @Override
    public void returnByUser() {
        this.borrowed = false;
        this.borrowerId = null;
        this.borrowDate = null;
    }
    
    @Override
    public boolean matches(String keyword) {
        return title.toLowerCase().contains(keyword.toLowerCase()) ||
               author.toLowerCase().contains(keyword.toLowerCase());
    }
    
    // 抽象方法 - 子类必须实现
    public abstract String getDocumentType();
    public abstract int getMaxBorrowDays();
    
    @Override
    public String toString() {
        return String.format("[%s] %s - %s (%s) %s", 
            getDocumentType(), title, author, id, 
            borrowed ? "[已借出]" : "[可借阅]");
    }
}

// 3. 具体文档类 - 展示继承和多态
class Book extends Document {
    private String isbn;
    private String publisher;
    
    public Book(String id, String title, String author, String isbn, String publisher) {
        super(id, title, author);
        this.isbn = isbn;
        this.publisher = publisher;
    }
    
    @Override
    public String getDocumentType() {
        return "图书";
    }
    
    @Override
    public int getMaxBorrowDays() {
        return 30; // 图书可借30天
    }
    
    public String getIsbn() { return isbn; }
    public String getPublisher() { return publisher; }
}

class Magazine extends Document {
    private String issue;
    private LocalDate publishDate;
    
    public Magazine(String id, String title, String author, String issue, LocalDate publishDate) {
        super(id, title, author);
        this.issue = issue;
        this.publishDate = publishDate;
    }
    
    @Override
    public String getDocumentType() {
        return "期刊";
    }
    
    @Override
    public int getMaxBorrowDays() {
        return 7; // 期刊只能借7天
    }
    
    public String getIssue() { return issue; }
    public LocalDate getPublishDate() { return publishDate; }
}

// 4. 用户抽象类
abstract class User {
    private String userId;
    private String name;
    private String email;
    private List<String> borrowedDocumentIds;
    
    public User(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.borrowedDocumentIds = new ArrayList<>();
    }
    
    // 封装的访问器
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getBorrowedDocumentIds() { return new ArrayList<>(borrowedDocumentIds); }
    
    // 借阅操作
    public void addBorrowedDocument(String documentId) {
        borrowedDocumentIds.add(documentId);
    }
    
    public void removeBorrowedDocument(String documentId) {
        borrowedDocumentIds.remove(documentId);
    }
    
    // 抽象方法 - 不同用户类型有不同的借阅限制
    public abstract int getMaxBorrowCount();
    public abstract String getUserType();
    
    @Override
    public String toString() {
        return String.format("%s: %s (%s) - 已借阅: %d/%d", 
            getUserType(), name, userId, borrowedDocumentIds.size(), getMaxBorrowCount());
    }
}

// 5. 具体用户类 - 展示继承
class Student extends User {
    private String studentId;
    private String major;
    
    public Student(String userId, String name, String email, String studentId, String major) {
        super(userId, name, email);
        this.studentId = studentId;
        this.major = major;
    }
    
    @Override
    public int getMaxBorrowCount() {
        return 5; // 学生最多借5本
    }
    
    @Override
    public String getUserType() {
        return "学生";
    }
    
    public String getStudentId() { return studentId; }
    public String getMajor() { return major; }
}

class Teacher extends User {
    private String department;
    private String title;
    
    public Teacher(String userId, String name, String email, String department, String title) {
        super(userId, name, email);
        this.department = department;
        this.title = title;
    }
    
    @Override
    public int getMaxBorrowCount() {
        return 10; // 教师最多借10本
    }
    
    @Override
    public String getUserType() {
        return "教师";
    }
    
    public String getDepartment() { return department; }
    public String getTitle() { return title; }
}

// 6. 主要的图书管理系统类
class LibrarySystem {
    private Map<String, Document> documents;
    private Map<String, User> users;
    private String currentUserId;
    
    public LibrarySystem() {
        documents = new HashMap<>();
        users = new HashMap<>();
        initializeTestData();
    }
    
    // 初始化测试数据
    private void initializeTestData() {
        // 添加测试文档
        addDocument(new Book("B001", "Java编程思想", "Bruce Eckel", "978-0131872486", "机械工业出版社"));
        addDocument(new Book("B002", "设计模式", "Gang of Four", "978-0201633610", "人民邮电出版社"));
        addDocument(new Magazine("M001", "计算机科学", "编辑部", "2024-01", LocalDate.of(2024, 1, 1)));
        addDocument(new Magazine("M002", "软件工程学报", "编辑部", "2024-02", LocalDate.of(2024, 2, 1)));
        
        // 添加测试用户
        addUser(new Student("S001", "张三", "zhangsan@example.com", "20210001", "计算机科学"));
        addUser(new Student("S002", "李四", "lisi@example.com", "20210002", "软件工程"));
        addUser(new Teacher("T001", "王老师", "wanglaoshi@example.com", "计算机学院", "教授"));
        addUser(new Teacher("T002", "赵老师", "zhaolaoshi@example.com", "软件学院", "副教授"));
    }
    
    // 添加文档
    public void addDocument(Document document) {
        documents.put(document.getId(), document);
    }
    
    // 添加用户
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    
    // 用户登录（简化版）
    public boolean login(String userId) {
        if (users.containsKey(userId)) {
            currentUserId = userId;
            System.out.println("用户 " + users.get(userId).getName() + " 登录成功！");
            return true;
        }
        System.out.println("用户不存在！");
        return false;
    }
    
    // 搜索文档 - 展示多态性
    public List<Document> searchDocuments(String keyword) {
        List<Document> results = new ArrayList<>();
        for (Document doc : documents.values()) {
            if (doc.matches(keyword)) { // 多态调用
                results.add(doc);
            }
        }
        return results;
    }
    
    // 借阅文档
    public boolean borrowDocument(String documentId) {
        if (currentUserId == null) {
            System.out.println("请先登录！");
            return false;
        }
        
        Document document = documents.get(documentId);
        User user = users.get(currentUserId);
        
        if (document == null) {
            System.out.println("文档不存在！");
            return false;
        }
        
        if (!document.canBorrow()) {
            System.out.println("文档已被借出！");
            return false;
        }
        
        if (user.getBorrowedDocumentIds().size() >= user.getMaxBorrowCount()) {
            System.out.println("借阅数量已达上限！");
            return false;
        }
        
        // 执行借阅
        document.borrowBy(currentUserId);
        user.addBorrowedDocument(documentId);
        
        System.out.println("借阅成功：" + document.getTitle());
        System.out.println("借阅期限：" + document.getMaxBorrowDays() + "天");
        return true;
    }
    
    // 归还文档
    public boolean returnDocument(String documentId) {
        if (currentUserId == null) {
            System.out.println("请先登录！");
            return false;
        }
        
        Document document = documents.get(documentId);
        User user = users.get(currentUserId);
        
        if (document == null) {
            System.out.println("文档不存在！");
            return false;
        }
        
        if (!document.isBorrowed() || !currentUserId.equals(document.getBorrowerId())) {
            System.out.println("您没有借阅此文档！");
            return false;
        }
        
        // 执行归还
        document.returnByUser();
        user.removeBorrowedDocument(documentId);
        
        System.out.println("归还成功：" + document.getTitle());
        return true;
    }
    
    // 显示所有文档
    public void showAllDocuments() {
        System.out.println("\n=== 所有文档 ===");
        for (Document doc : documents.values()) {
            System.out.println(doc); // 多态调用toString()
        }
    }
    
    // 显示所有用户
    public void showAllUsers() {
        System.out.println("\n=== 所有用户 ===");
        for (User user : users.values()) {
            System.out.println(user); // 多态调用toString()
        }
    }
    
    // 显示当前用户的借阅情况
    public void showMyBorrowings() {
        if (currentUserId == null) {
            System.out.println("请先登录！");
            return;
        }
        
        User user = users.get(currentUserId);
        System.out.println("\n=== 我的借阅 ===");
        System.out.println(user);
        
        for (String docId : user.getBorrowedDocumentIds()) {
            Document doc = documents.get(docId);
            if (doc != null) {
                System.out.println("  - " + doc.getTitle() + " (" + doc.getId() + ")");
            }
        }
    }
    
    // 主菜单
    public void showMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        System.out.println("\n========== 简化图书管理系统 ==========");
        System.out.println("展示面向对象编程核心概念：封装、继承、多态、抽象、接口");
        
        while (running) {
            try {
                if (currentUserId == null) {
                    System.out.println("\n请先登录：");
                    System.out.println("可用用户ID: S001, S002, T001, T002");
                    System.out.print("输入用户ID (或输入 'exit' 退出): ");
                    String input = scanner.nextLine().trim();
                    
                    if ("exit".equals(input)) {
                        running = false;
                        continue;
                    }
                    
                    login(input);
                } else {
                    System.out.println("\n===== 主菜单 =====");
                    System.out.println("1. 查看所有文档");
                    System.out.println("2. 搜索文档");
                    System.out.println("3. 借阅文档");
                    System.out.println("4. 归还文档");
                    System.out.println("5. 我的借阅");
                    System.out.println("6. 查看所有用户");
                    System.out.println("7. 登出");
                    System.out.println("0. 退出");
                    System.out.print("请选择: ");
                    
                    String choice = scanner.nextLine().trim();
                    
                    switch (choice) {
                        case "1":
                            showAllDocuments();
                            break;
                        case "2":
                            System.out.print("输入搜索关键词: ");
                            String keyword = scanner.nextLine().trim();
                            List<Document> results = searchDocuments(keyword);
                            System.out.println("\n搜索结果:");
                            if (results.isEmpty()) {
                                System.out.println("未找到相关文档");
                            } else {
                                for (Document doc : results) {
                                    System.out.println(doc);
                                }
                            }
                            break;
                        case "3":
                            System.out.print("输入要借阅的文档ID: ");
                            String borrowId = scanner.nextLine().trim();
                            borrowDocument(borrowId);
                            break;
                        case "4":
                            System.out.print("输入要归还的文档ID: ");
                            String returnId = scanner.nextLine().trim();
                            returnDocument(returnId);
                            break;
                        case "5":
                            showMyBorrowings();
                            break;
                        case "6":
                            showAllUsers();
                            break;
                        case "7":
                            System.out.println("用户 " + users.get(currentUserId).getName() + " 已登出");
                            currentUserId = null;
                            break;
                        case "0":
                            running = false;
                            break;
                        default:
                            System.out.println("无效选择，请重试");
                    }
                }
            } catch (Exception e) {
                System.out.println("发生错误: " + e.getMessage());
            }
        }
        
        System.out.println("感谢使用图书管理系统！");
        scanner.close();
    }
}

// 7. 主类 - 程序入口
public class SimpleLibrarySystem {
    public static void main(String[] args) {
        LibrarySystem library = new LibrarySystem();
        library.showMenu();
    }
}
