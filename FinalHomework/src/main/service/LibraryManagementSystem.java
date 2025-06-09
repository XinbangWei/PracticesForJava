package main.service;

import main.model.user.*;
import main.model.document.*;
import main.util.DataPersistenceUtil;
import main.model.system.SystemConfig;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Future;

/**
 * 图书馆管理系统主服务类
 * 集成所有服务组件，提供统一的系统接口
 */
public class LibraryManagementSystem {
    
    private Map<String, User> users;
    private Map<String, Document> documents;
    private SearchService searchService;
    private BorrowingService borrowingService;
    private AuditService auditService;
    private User currentUser;
    
    public LibraryManagementSystem() {
        this.users = new HashMap<>();
        this.documents = new HashMap<>();
        this.searchService = new SearchService();
        this.borrowingService = new BorrowingService();
        this.auditService = AuditService.getInstance();
        this.currentUser = null;
        
        // 系统启动时加载数据
        loadSystemData();
        initializeDefaultData();
    }
    
    /**
     * 用户登录
     */
    public boolean login(String username, String password, String ipAddress) {
        for (User user : users.values()) {
            if (user.getUsername().equals(username) && user.verifyPassword(password)) {
                if (!user.isActive()) {
                    auditService.logLogin(user.getUserId(), LocalDateTime.now(), ipAddress, false);
                    return false;
                }
                
                this.currentUser = user;
                user.updateLastLoginTime();
                auditService.logLogin(user.getUserId(), LocalDateTime.now(), ipAddress, true);
                
                System.out.println(String.format("用户 %s (%s) 登录成功", user.getRealName(), user.getUserTypeDescription()));
                return true;
            }
        }
        
        auditService.logLogin(username, LocalDateTime.now(), ipAddress, false);
        return false;
    }
    
    /**
     * 用户登出
     */
    public void logout() {
        if (currentUser != null) {
            auditService.logOperation(currentUser.getUserId(), SystemConfig.OperationTypes.LOGOUT, 
                                     null, LocalDateTime.now(), "用户登出");
            System.out.println(String.format("用户 %s 已登出", currentUser.getRealName()));
            this.currentUser = null;
        }
    }
    
    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        return currentUser;
    }
    
    /**
     * 检查是否已登录
     */
    public boolean isLoggedIn() {
        return currentUser != null;
    }
    
    /**
     * 搜索文档
     */
    public List<Object> searchDocuments(String keyword) {
        if (!isLoggedIn()) {
            System.out.println("请先登录");
            return new ArrayList<>();
        }
        
        return searchService.searchByKeyword(keyword, currentUser);
    }
    
    /**
     * 高级搜索
     */
    public List<Object> advancedSearchDocuments(String title, String author, String category, String keyword) {
        if (!isLoggedIn()) {
            System.out.println("请先登录");
            return new ArrayList<>();
        }
        
        return searchService.advancedSearch(title, author, category, keyword, currentUser);
    }
    
    /**
     * 借阅文档
     */
    public String borrowDocument(String documentId) {
        if (!isLoggedIn()) {
            return "请先登录";
        }
        
        BorrowingService.BorrowResult result = borrowingService.borrowSync(currentUser.getUserId(), documentId);
        return result.getMessage();
    }
    
    /**
     * 归还文档
     */
    public String returnDocument(String documentId) {
        if (!isLoggedIn()) {
            return "请先登录";
        }
        
        BorrowingService.BorrowResult result = borrowingService.returnSync(currentUser.getUserId(), documentId);
        return result.getMessage();
    }
    
    /**
     * 续借文档
     */
    public String extendDocument(String documentId, int days) {
        if (!isLoggedIn()) {
            return "请先登录";
        }
        
        BorrowingService.BorrowResult result = borrowingService.extendBorrow(currentUser.getUserId(), documentId, days);
        return result.getMessage();
    }
    
    /**
     * 获取用户借阅列表
     */
    public List<Document> getMyBorrowedDocuments() {
        if (!isLoggedIn()) {
            return new ArrayList<>();
        }
        
        return borrowingService.getUserBorrowedDocuments(currentUser.getUserId());
    }
    
    /**
     * 添加新文档（需要管理员权限）
     */
    public boolean addDocument(Document document) {
        if (!isLoggedIn()) {
            System.out.println("请先登录");
            return false;
        }
        
        if (!(currentUser instanceof Administrator) && !(currentUser instanceof ArchiveManager)) {
            System.out.println("权限不足，只有管理员可以添加文档");
            return false;
        }
        
        documents.put(document.getDocumentId(), document);
        searchService.addDocument(document);
        borrowingService.addDocument(document);
        
        auditService.logOperation(currentUser.getUserId(), "ADD_DOCUMENT", 
                                 document.getDocumentId(), LocalDateTime.now(), 
                                 "添加文档: " + document.getTitle());
        
        System.out.println("文档添加成功：" + document.getTitle());
        return true;
    }
    
    /**
     * 添加新用户（需要管理员权限）
     */
    public boolean addUser(User user) {
        if (!isLoggedIn()) {
            System.out.println("请先登录");
            return false;
        }
        
        if (!(currentUser instanceof Administrator)) {
            System.out.println("权限不足，只有系统管理员可以添加用户");
            return false;
        }
        
        users.put(user.getUserId(), user);
        borrowingService.addUser(user);
        
        auditService.logOperation(currentUser.getUserId(), "ADD_USER", 
                                 user.getUserId(), LocalDateTime.now(), 
                                 "添加用户: " + user.getRealName() + " (" + user.getUserTypeDescription() + ")");
        
        System.out.println("用户添加成功：" + user.getRealName());
        return true;
    }
    
    /**
     * 获取系统统计信息
     */
    public String getSystemStatistics() {
        StringBuilder stats = new StringBuilder();
        stats.append(SystemConfig.getSystemInfo()).append("\n\n");
        stats.append("=== 系统统计信息 ===\n");
        stats.append(String.format("用户总数: %d\n", users.size()));
        stats.append(String.format("文档总数: %d\n", documents.size()));
        
        // 按用户类型统计
        long regularUsers = users.values().stream().filter(u -> u instanceof RegularUser).count();
        long authorizedUsers = users.values().stream().filter(u -> u instanceof AuthorizedUser).count();
        long archiveManagers = users.values().stream().filter(u -> u instanceof ArchiveManager).count();
        long administrators = users.values().stream().filter(u -> u instanceof Administrator).count();
        
        stats.append(String.format("- 普通用户: %d\n", regularUsers));
        stats.append(String.format("- 授权用户: %d\n", authorizedUsers));
        stats.append(String.format("- 档案管理员: %d\n", archiveManagers));
        stats.append(String.format("- 系统管理员: %d\n", administrators));
        
        // 按文档类型统计
        long physicalBooks = documents.values().stream().filter(d -> d instanceof PhysicalBook).count();
        long eBooks = documents.values().stream().filter(d -> d instanceof EBook).count();
        long internalDocs = documents.values().stream().filter(d -> d instanceof InternalDocument).count();
        long archiveDocs = documents.values().stream().filter(d -> d instanceof ArchiveDocument).count();
        
        stats.append(String.format("- 实体书: %d\n", physicalBooks));
        stats.append(String.format("- 电子书: %d\n", eBooks));
        stats.append(String.format("- 内部资料: %d\n", internalDocs));
        stats.append(String.format("- 档案资料: %d\n", archiveDocs));
        
        // 借阅统计
        List<Document> overdueList = borrowingService.getOverdueDocuments();
        stats.append(String.format("逾期文档: %d\n", overdueList.size()));
        
        return stats.toString();
    }
    
    /**
     * 保存系统数据
     */
    public boolean saveSystemData() {
        List<User> userList = new ArrayList<>(users.values());
        List<Document> documentList = new ArrayList<>(documents.values());
        
        boolean usersaved = DataPersistenceUtil.saveUsers(userList);
        boolean docSaved = DataPersistenceUtil.saveDocuments(documentList);
        
        if (usersaved && docSaved) {
            System.out.println("系统数据保存成功");
            return true;
        } else {
            System.out.println("系统数据保存失败");
            return false;
        }
    }
    
    /**
     * 加载系统数据
     */
    private void loadSystemData() {
        List<User> userList = DataPersistenceUtil.loadUsers();
        for (User user : userList) {
            users.put(user.getUserId(), user);
            borrowingService.addUser(user);
        }
        
        List<Document> documentList = DataPersistenceUtil.loadDocuments();
        for (Document document : documentList) {
            documents.put(document.getDocumentId(), document);
            searchService.addDocument(document);
            borrowingService.addDocument(document);
        }
    }
    
    /**
     * 初始化默认数据
     */
    private void initializeDefaultData() {
        if (users.isEmpty()) {
            // 创建默认管理员
            Administrator admin = new Administrator("admin001", "admin", "admin123", 
                                                  "系统管理员", "admin@library.com", "13800000000", 
                                                  "信息技术部", "超级管理员", "全部权限");
            users.put(admin.getUserId(), admin);
            borrowingService.addUser(admin);
            
            // 创建默认档案管理员
            ArchiveManager archiveManager = new ArchiveManager("archive001", "archive", "archive123", 
                                                              "张档案", "archive@library.com", "13800000001", 
                                                              "档案管理部", "全部档案", "高级认证");
            users.put(archiveManager.getUserId(), archiveManager);
            borrowingService.addUser(archiveManager);
            
            System.out.println("默认用户创建完成");
        }
        
        if (documents.isEmpty()) {
            // 创建一些示例文档
            createSampleDocuments();
        }
    }
    
    /**
     * 创建示例文档
     */
    private void createSampleDocuments() {
        // 实体书
        PhysicalBook book1 = DocumentFactory.createPhysicalBook("PB001", "Java编程思想", "Bruce Eckel", 
                                                               "机械工业出版社", "编程", "Java编程经典教材", 
                                                               LocalDate.of(2020, 1, 1), "978-7-111-12345-6", 
                                                               800, "中文", "A区", "A001");
        documents.put(book1.getDocumentId(), book1);
        searchService.addDocument(book1);
        borrowingService.addDocument(book1);
        
        // 电子书
        EBook ebook1 = DocumentFactory.createEBook("EB001", "深入理解计算机系统", "Randal E. Bryant", 
                                                  "机械工业出版社", "计算机系统", "计算机系统经典教材", 
                                                  LocalDate.of(2021, 1, 1), "978-7-111-54321-9", 
                                                  900, "中文", "PDF", 50*1024*1024L, "http://library.com/download/csapp.pdf");
        documents.put(ebook1.getDocumentId(), ebook1);
        searchService.addDocument(ebook1);
        borrowingService.addDocument(ebook1);
        
        System.out.println("示例文档创建完成");
    }
    
    /**
     * 系统关闭
     */
    public void shutdown() {
        saveSystemData();
        borrowingService.shutdown();
        System.out.println("系统已关闭");
    }
    
    /**
     * 控制台主循环，演示系统功能
     */
    public void startConsole() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        boolean running = true;
        System.out.println("\n==== 图书管理系统 ====");
        while (running) {
            try {
                if (!isLoggedIn()) {
                    System.out.println("\n1. 登录  0. 退出");
                    System.out.print("请选择: ");
                    String op = scanner.nextLine();
                    if ("1".equals(op)) {
                        System.out.print("用户名: ");
                        String username = scanner.nextLine();
                        System.out.print("密码: ");
                        String password = scanner.nextLine();
                        boolean ok = login(username, password, "127.0.0.1");
                        if (!ok) {
                            System.out.println("登录失败，请重试。");
                        }
                    } else if ("0".equals(op)) {
                        running = false;
                        shutdown();
                    }
                } else {
                    System.out.println("\n欢迎, " + getCurrentUser().getRealName() + " (" + getCurrentUser().getUserTypeDescription() + ")");
                    System.out.println("1. 搜索文档  2. 借阅文档  3. 归还文档  4. 续借  5. 我的借阅  6. 统计信息  7. 登出  0. 退出");
                    if (getCurrentUser() instanceof Administrator || getCurrentUser() instanceof ArchiveManager) {
                        System.out.println("8. 添加文档  9. 添加用户");
                    }
                    System.out.print("请选择: ");
                    String op = scanner.nextLine();
                    switch (op) {
                        case "1":
                            System.out.print("请输入关键词: ");
                            String kw = scanner.nextLine();
                            var results = searchDocuments(kw);
                            System.out.println("搜索结果: ");
                            for (Object doc : results) {
                                System.out.println(doc);
                            }
                            break;
                        case "2":
                            System.out.print("输入要借阅的文档ID: ");
                            String docId = scanner.nextLine();
                            System.out.println(borrowDocument(docId));
                            break;
                        case "3":
                            System.out.print("输入要归还的文档ID: ");
                            String retId = scanner.nextLine();
                            System.out.println(returnDocument(retId));
                            break;
                        case "4":
                            System.out.print("输入要续借的文档ID: ");
                            String extId = scanner.nextLine();
                            System.out.print("续借天数: ");
                            int days = Integer.parseInt(scanner.nextLine());
                            System.out.println(extendDocument(extId, days));
                            break;
                        case "5":
                            var myDocs = getMyBorrowedDocuments();
                            System.out.println("我的借阅:");
                            for (var d : myDocs) {
                                System.out.println(d);
                            }
                            break;
                        case "6":
                            System.out.println(getSystemStatistics());
                            break;
                        case "7":
                            logout();
                            break;
                        case "8":
                            if (getCurrentUser() instanceof Administrator || getCurrentUser() instanceof ArchiveManager) {
                                System.out.print("文档类型(1实体书/2电子书/3内部资料/4档案): ");
                                String t = scanner.nextLine();
                                // 这里只做简单演示，实际应完善参数输入
                                System.out.print("文档ID: ");
                                String id = scanner.nextLine();
                                System.out.print("标题: ");
                                String title = scanner.nextLine();
                                Document doc = null;
                                if ("1".equals(t)) {
                                    doc = DocumentFactory.createPhysicalBook(id, title, "作者", "出版社", "分类", "描述", java.time.LocalDate.now(), "ISBN", 100, "中文", "A区", "A001");
                                } else if ("2".equals(t)) {
                                    doc = DocumentFactory.createEBook(id, title, "作者", "出版社", "分类", "描述", java.time.LocalDate.now(), "ISBN", 100, "中文", "PDF", 1024L, "url");
                                } else if ("3".equals(t)) {
                                    doc = DocumentFactory.createInternalDocument(
                                        id, title, "作者", "出版社", "分类", "描述", java.time.LocalDate.now(),
                                        "机密", "研究领域", "项目代码", "批准文号", "定密人"
                                    );
                                } else if ("4".equals(t)) {
                                    doc = DocumentFactory.createArchiveDocument(
                                        id, title, "作者", "出版社", "分类", "描述", java.time.LocalDate.now(),
                                        "绝密", "档案室", "DA001", "归档人", true
                                    );
                                }
                                if (doc != null) addDocument(doc);
                            }
                            break;
                        case "9":
                            if (getCurrentUser() instanceof Administrator) {
                                System.out.print("用户类型(1普通/2授权/3档案/4管理员): ");
                                String ut = scanner.nextLine();
                                System.out.print("用户ID: ");
                                String uid = scanner.nextLine();
                                System.out.print("用户名: ");
                                String uname = scanner.nextLine();
                                System.out.print("密码: ");
                                String upass = scanner.nextLine();
                                System.out.print("真实姓名: ");
                                String rname = scanner.nextLine();
                                User user = null;
                                if ("1".equals(ut)) {
                                    user = new RegularUser(uid, uname, upass, rname, "email", "phone", "部门");
                                } else if ("2".equals(ut)) {
                                    user = new AuthorizedUser(uid, uname, upass, rname, "email", "phone", "部门", "项目代码", "研究领域");
                                } else if ("3".equals(ut)) {
                                    user = new ArchiveManager(uid, uname, upass, rname, "email", "phone", "部门", "区域", "认证");
                                } else if ("4".equals(ut)) {
                                    user = new Administrator(uid, uname, upass, rname, "email", "phone", "部门", "级别", "权限");
                                }
                                if (user != null) addUser(user);
                            }
                            break;
                        case "0":
                            running = false;
                            shutdown();
                            break;
                        default:
                            System.out.println("无效操作");
                    }
                }
            } catch (Exception e) {
                System.out.println("发生异常: " + e.getMessage());
                e.printStackTrace();
            }
        }
        scanner.close();
    }
      /**
     * 多线程并发借阅演示
     * 演示多个用户同时借阅同一本书的情况
     */
    public void demonstrateMultithreading() {
        System.out.println("\n=== 多线程并发借阅演示 ===");
        
        // 临时记录当前用户，以便恢复
        User previousUser = currentUser;
        
        // 临时使用管理员身份来添加测试数据
        Administrator tempAdmin = new Administrator("temp_admin", "临时管理员", "123456", "系统", 
                                                   "system@library.com", "00000000000", 
                                                   "系统部", "超级管理员", "完全权限");
        currentUser = tempAdmin;
        
        // 创建测试用户
        RegularUser user1 = new RegularUser("test1", "用户1", "123456", "张三", 
                                           "user1@test.com", "13800138001", "计算机系");
        RegularUser user2 = new RegularUser("test2", "用户2", "123456", "李四", 
                                           "user2@test.com", "13800138002", "数学系");
        RegularUser user3 = new RegularUser("test3", "用户3", "123456", "王五", 
                                           "user3@test.com", "13800138003", "物理系");
        
        // 直接添加到仓库，绕过权限检查
        users.put(user1.getUserId(), user1);
        users.put(user2.getUserId(), user2);
        users.put(user3.getUserId(), user3);
        borrowingService.addUser(user1);
        borrowingService.addUser(user2);
        borrowingService.addUser(user3);        // 创建一本热门图书
        PhysicalBook popularBook = new PhysicalBook("BOOK999", "Java并发编程实战", 
                                                   "Brian Goetz", "机械工业出版社", 
                                                   "编程", "Java并发编程经典教材", 
                                                   LocalDate.of(2020, 1, 1), 
                                                   "978-7-111-27007-4", 336, "中文",
                                                   "A区3层", "A3-001");
        // 直接添加到仓库，绕过权限检查
        documents.put(popularBook.getDocumentId(), popularBook);
        searchService.addDocument(popularBook);
        borrowingService.addDocument(popularBook);
        
        // 恢复原用户状态
        currentUser = previousUser;
        
        System.out.println("测试环境准备完成：");
        System.out.println("- 已添加3个测试用户：张三、李四、王五");
        System.out.println("- 已添加热门图书：《Java并发编程实战》");
        System.out.println("- 文档ID: BOOK999");
        
        // 创建多个异步借阅请求
        System.out.println("\n开始并发借阅测试...");
        System.out.println("3个用户同时尝试借阅同一本书：");
        
        Future<BorrowingService.BorrowResult> future1 = borrowingService.borrowAsync("test1", "BOOK999");
        Future<BorrowingService.BorrowResult> future2 = borrowingService.borrowAsync("test2", "BOOK999");
        Future<BorrowingService.BorrowResult> future3 = borrowingService.borrowAsync("test3", "BOOK999");
        
        try {
            // 等待所有借阅操作完成并显示结果
            BorrowingService.BorrowResult result1 = future1.get();
            BorrowingService.BorrowResult result2 = future2.get();
            BorrowingService.BorrowResult result3 = future3.get();
            
            System.out.println("\n借阅结果：");
            System.out.println("用户张三: " + (result1.isSuccess() ? "✓ " : "✗ ") + result1.getMessage());
            System.out.println("用户李四: " + (result2.isSuccess() ? "✓ " : "✗ ") + result2.getMessage());
            System.out.println("用户王五: " + (result3.isSuccess() ? "✓ " : "✗ ") + result3.getMessage());
            
            // 统计成功和失败的数量
            int successCount = 0;
            String borrower = "";
            if (result1.isSuccess()) { successCount++; borrower = "张三"; }
            if (result2.isSuccess()) { successCount++; borrower = "李四"; }
            if (result3.isSuccess()) { successCount++; borrower = "王五"; }
            
            System.out.println(String.format("\n并发借阅统计：成功 %d 个，失败 %d 个", 
                             successCount, 3 - successCount));
            
            if (successCount == 1) {
                System.out.println("✓ 多线程并发控制成功！");
                System.out.println("  - 只有用户" + borrower + "成功借阅了图书");
                System.out.println("  - 其他用户收到了\"文档已被借出\"的提示");
                System.out.println("  - 线程安全机制确保了数据一致性");
            } else if (successCount == 0) {
                System.out.println("⚠ 所有用户都借阅失败，可能存在权限或其他问题");
            } else {
                System.out.println("⚠ 多个用户同时借阅成功，可能存在并发控制问题");
            }
            
        } catch (Exception e) {
            System.out.println("多线程演示过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== 多线程演示结束 ===\n");
    }
}
