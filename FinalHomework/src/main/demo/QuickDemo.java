package main.demo;

import main.service.LibraryManagementSystem;
import main.model.user.*;
import main.model.document.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 图书管理系统快速功能演示
 * 简化版演示，快速展示核心功能
 */
public class QuickDemo {
    
    private LibraryManagementSystem system;
    
    public QuickDemo() {
        this.system = new LibraryManagementSystem();
    }
    
    /**
     * 运行快速演示
     */
    public void runQuickDemo() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                  图书管理系统 - 快速功能演示                  ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝\n");
        
        // 1. 展示面向对象特性
        demonstrateOOPFeatures();
        
        // 2. 展示用户权限系统
        demonstrateUserPermissions();
        
        // 3. 展示文档管理
        demonstrateDocumentTypes();
        
        // 4. 展示借阅流程
        demonstrateBorrowingProcess();
        
        // 5. 展示多线程
        demonstrateMultithreading();
        
        // 6. 展示设计模式
        demonstrateDesignPatterns();
        
        System.out.println("\n╔════════════════════════════════════════════════════════════╗");
        System.out.println("║                     快速演示完成                          ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * 演示面向对象特性
     */
    private void demonstrateOOPFeatures() {
        System.out.println("【1. 面向对象编程特性演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // 创建不同类型用户 - 展示继承
        User[] users = {
            new RegularUser("user01", "student", "123456", "张同学", "student@edu.cn", "13800000001", "计算机系"),
            new AuthorizedUser("user02", "researcher", "123456", "李研究员", "researcher@institute.cn", "13800000002", "AI实验室", "AI-2024", "机器学习"),
            new ArchiveManager("user03", "archivist", "123456", "王档案员", "archive@institute.cn", "13800000003", "档案部", "技术档案", "高级认证"),
            new Administrator("user04", "sysadmin", "123456", "赵管理员", "admin@institute.cn", "13800000004", "信息部", "高级", "系统管理")
        };
          System.out.println("✓ 继承特性 - 不同用户类型继承自User基类:");
        for (User user : users) {
            System.out.println("  " + user.getUserTypeDescription() + " - 最大借阅数: " + user.getMaxBorrowCount());
        }
        
        // 创建不同类型文档 - 展示多态
        Document book = new PhysicalBook("PB001", "Java编程艺术", "James Gosling", "技术出版社", 
                                       "编程", "Java编程指南", LocalDate.now(), "ISBN-001", 500, "中文", "A区", "A001");
        Document ebook = new EBook("EB001", "Python数据科学", "Wes McKinney", "数据出版社", 
                                 "数据科学", "Python数据分析", LocalDate.now(), "ISBN-002", 400, "中文", "PDF", 10485760L, "http://example.com/book.pdf");
        
        System.out.println("\n✓ 多态特性 - 同一接口不同实现:");
        System.out.println("  实体书类型: " + book.getDocumentType() + " - 可借阅: " + book.isAvailable());
        System.out.println("  电子书类型: " + ebook.getDocumentType() + " - 可借阅: " + ebook.isAvailable());
        
        System.out.println("\n✓ 封装特性 - 数据隐藏和方法访问:");
        System.out.println("  用户信息通过getter方法访问: " + users[0].getRealName() + " (" + users[0].getDepartment() + ")");
        System.out.println("  文档状态通过方法管理: " + book.getTitle() + " - 状态: " + (!book.isAvailable() ? "已借出" : "可借阅"));
        
        pauseForNext();
    }
    
    /**
     * 演示用户权限系统
     */
    private void demonstrateUserPermissions() {
        System.out.println("\n【2. 用户权限和访问控制演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // 创建不同安全级别的文档
        Document publicBook = new PhysicalBook("PUB001", "公开技术资料", "公开作者", "科技出版社", 
                                             "技术", "公开可访问", LocalDate.now(), "PUB-ISBN", 300, "中文", "公开区", "PUB001");
        
        Document internalDoc = new InternalDocument("INT001", "内部研发报告", "研发团队", "内部", 
                                                   "研发", "内部技术报告", LocalDate.now(), "机密", "人工智能", "AI-2024", "TECH-001", "技术总监");
        
        Document archiveDoc = new ArchiveDocument("ARCH001", "历史档案资料", "档案员", "档案室", 
                                                 "历史", "重要历史档案", LocalDate.now(), "绝密", "历史档案区", "HIST-001", "高级档案员", true);
        
        // 测试不同用户的访问权限
        RegularUser student = new RegularUser("stu001", "student", "123456", "学生甲", "stu@edu.cn", "13800000001", "计算机系");
        AuthorizedUser researcher = new AuthorizedUser("res001", "researcher", "123456", "研究员乙", "res@institute.cn", "13800000002", "AI实验室", "AI-2024", "机器学习");
        ArchiveManager archivist = new ArchiveManager("arch001", "archivist", "123456", "档案员丙", "arch@institute.cn", "13800000003", "档案部", "全部档案", "高级认证");
        
        System.out.println("✓ 权限控制 - 不同用户对不同文档的访问权限:");
        System.out.println("  文档类型          | 普通用户 | 授权用户 | 档案管理员");
        System.out.println("  ------------------|----------|----------|----------");
        System.out.println("  公开技术资料      |    " + (student.canAccess(publicBook) ? "✓" : "✗") + "    |    " + (researcher.canAccess(publicBook) ? "✓" : "✗") + "    |     " + (archivist.canAccess(publicBook) ? "✓" : "✗"));
        System.out.println("  内部研发报告      |    " + (student.canAccess(internalDoc) ? "✓" : "✗") + "    |    " + (researcher.canAccess(internalDoc) ? "✓" : "✗") + "    |     " + (archivist.canAccess(internalDoc) ? "✓" : "✗"));
        System.out.println("  历史档案资料      |    " + (student.canAccess(archiveDoc) ? "✓" : "✗") + "    |    " + (researcher.canAccess(archiveDoc) ? "✓" : "✗") + "    |     " + (archivist.canAccess(archiveDoc) ? "✓" : "✗"));
          System.out.println("\n✓ 借阅限制 - 不同用户类型的借阅限制:");
        System.out.println("  普通用户最大借阅数: " + student.getMaxBorrowCount() + " 本");
        System.out.println("  授权用户最大借阅数: " + researcher.getMaxBorrowCount() + " 本");
        System.out.println("  档案管理员最大借阅数: " + archivist.getMaxBorrowCount() + " 本");
        
        pauseForNext();
    }
    
    /**
     * 演示文档类型
     */
    private void demonstrateDocumentTypes() {
        System.out.println("\n【3. 文档管理和类型演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // 登录档案管理员添加各种文档
        system.login("archive", "archive123", "192.168.1.100");
        
        System.out.println("✓ 工厂模式 - 使用DocumentFactory创建不同类型文档:");
        
        // 实体书
        PhysicalBook physicalBook = DocumentFactory.createPhysicalBook("DEMO_PB", "算法导论", "Thomas Cormen", "机械工业出版社", 
                                                                      "算法", "经典算法教材", LocalDate.of(2023, 1, 1), "978-7-111-40701-0", 1312, "中文", "C区3层", "C3-088");
        system.addDocument(physicalBook);
        
        // 电子书
        EBook eBook = DocumentFactory.createEBook("DEMO_EB", "深度学习", "Ian Goodfellow", "人民邮电出版社", 
                                                 "机器学习", "深度学习经典教材", LocalDate.of(2023, 6, 1), "978-7-115-48042-0", 787, "中文", "PDF", 52428800L, "https://library.institute.edu/dl.pdf");
        system.addDocument(eBook);
        
        // 内部文档
        InternalDocument internalDoc = DocumentFactory.createInternalDocument("DEMO_ID", "AI项目研发手册", "AI团队", "内部", 
                                                                             "技术手册", "人工智能项目内部手册", LocalDate.of(2024, 1, 1), "内部", "人工智能", "AI-2024-001", "DEV-2024-001", "项目总监");
        system.addDocument(internalDoc);
        
        // 档案文档
        ArchiveDocument archiveDoc = DocumentFactory.createArchiveDocument("DEMO_AD", "科研历史档案", "档案室", "档案管理部", 
                                                                           "历史档案", "2020-2023科研项目档案", LocalDate.of(2024, 2, 1), "机密", "科研档案区", "SCI-2024-001", "高级档案员", true);
        system.addDocument(archiveDoc);
        
        System.out.println("  ✓ 实体书: " + physicalBook.getTitle() + " (位置: " + physicalBook.getLocation() + ")");
        System.out.println("  ✓ 电子书: " + eBook.getTitle() + " (格式: " + eBook.getFileFormat() + ", 大小: " + eBook.getFileSize()/(1024*1024) + "MB)");
        System.out.println("  ✓ 内部资料: " + internalDoc.getTitle() + " (密级: " + internalDoc.getSecurityLevel() + ")");
        System.out.println("  ✓ 档案文档: " + archiveDoc.getTitle() + " (归档号: " + archiveDoc.getArchiveNumber() + ")");
        
        System.out.println("\n✓ 接口实现 - 所有文档都实现了Borrowable, Searchable, Auditable接口:");
        System.out.println("  Borrowable: 支持借阅、归还、续借操作");
        System.out.println("  Searchable: 支持关键词搜索和高级搜索");
        System.out.println("  Auditable: 支持操作审计和日志记录");
        
        system.logout();
        pauseForNext();
    }
    
    /**
     * 演示借阅流程
     */
    private void demonstrateBorrowingProcess() {
        System.out.println("\n【4. 借阅管理流程演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        // 添加演示用户
        RegularUser demoUser = new RegularUser("demo_user", "demo_student", "123456", "演示学生", "demo@edu.cn", "13900000000", "演示系");
        system.login("admin", "admin123", "192.168.1.101");
        system.addUser(demoUser);
        system.logout();
        
        // 用户登录并进行借阅操作
        system.login("demo_student", "123456", "192.168.1.102");
        
        System.out.println("✓ 用户登录: 演示学生");
        
        // 搜索文档
        System.out.println("\n✓ 搜索功能演示:");
        List<Object> searchResults = system.searchDocuments("算法");
        System.out.println("  搜索'算法'找到 " + searchResults.size() + " 个结果");
          // 借阅文档
        System.out.println("\n✓ 借阅操作演示:");
        String borrowResult1 = system.borrowDocument("DEMO_PB");
        System.out.println("  借阅实体书: " + borrowResult1);
        System.out.println("  借阅者: 演示学生 (用户ID: demo_user)");

        String borrowResult2 = system.borrowDocument("DEMO_EB");
        System.out.println("  借阅电子书: " + borrowResult2);
        System.out.println("  借阅者: 演示学生 (用户ID: demo_user)");

        // 查看借阅记录
        System.out.println("\n✓ 借阅记录查询:");
        List<Document> myBooks = system.getMyBorrowedDocuments();
        System.out.println("  当前借阅数量: " + myBooks.size());
        for (Document doc : myBooks) {
            System.out.println("  - " + doc.getTitle() + " (" + doc.getClass().getSimpleName() + ")");
            if (doc.getCurrentBorrower() != null) {
                System.out.println("    借阅者: " + doc.getCurrentBorrower().getRealName() + 
                                   " (到期日: " + doc.getDueDate() + ")");
            }
        }
        
        // 续借操作
        System.out.println("\n✓ 续借操作演示:");
        String extendResult = system.extendDocument("DEMO_PB", 14);
        System.out.println("  续借实体书14天: " + extendResult);
        
        // 归还操作
        System.out.println("\n✓ 归还操作演示:");
        String returnResult = system.returnDocument("DEMO_EB");
        System.out.println("  归还电子书: " + returnResult);
        
        // 权限测试
        System.out.println("\n✓ 权限控制演示:");
        String accessResult = system.borrowDocument("DEMO_ID");
        System.out.println("  尝试借阅内部资料: " + accessResult);
        
        system.logout();
        pauseForNext();
    }
    
    /**
     * 演示多线程功能
     */
    private void demonstrateMultithreading() {
        System.out.println("\n【5. 多线程并发控制演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("✓ 并发借阅控制:");
        System.out.println("  模拟多用户同时借阅同一文档的情况");
        System.out.println("  使用线程池和锁机制确保数据一致性");
        
        // 调用系统的多线程演示
        system.demonstrateMultithreading();
        
        pauseForNext();
    }
    
    /**
     * 演示设计模式
     */
    private void demonstrateDesignPatterns() {
        System.out.println("\n【6. 设计模式应用演示】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        
        System.out.println("✓ 工厂模式 (Factory Pattern):");
        System.out.println("  DocumentFactory统一创建不同类型文档对象");
        System.out.println("  支持PhysicalBook、EBook、InternalDocument、ArchiveDocument");
        
        System.out.println("\n✓ 单例模式 (Singleton Pattern):");
        main.service.AuditService audit1 = main.service.AuditService.getInstance();
        main.service.AuditService audit2 = main.service.AuditService.getInstance();
        System.out.println("  AuditService单例验证: " + (audit1 == audit2 ? "✓ 同一实例" : "✗ 不同实例"));
        
        System.out.println("\n✓ 策略模式的体现:");
        System.out.println("  不同用户类型有不同的权限策略");
        System.out.println("  不同文档类型有不同的访问策略");
        
        System.out.println("\n✓ 观察者模式的体现:");
        System.out.println("  AuditService监听并记录系统中的各种操作");
        System.out.println("  所有重要操作都会触发审计日志记录");
        
        pauseForNext();
    }
    
    /**
     * 暂停等待用户操作
     */
    private void pauseForNext() {
        System.out.println("\n" + "─".repeat(60));
        System.out.println("按 Enter 键继续下一个演示...");
        try {
            System.in.read();
            // 清空输入缓冲区
            while (System.in.available() > 0) {
                System.in.read();
            }
        } catch (Exception e) {
            // 忽略异常
        }
    }
    
    /**
     * 主方法
     */
    public static void main(String[] args) {
        QuickDemo demo = new QuickDemo();
        demo.runQuickDemo();
        
        // 显示统计信息
        demo.system.login("admin", "admin123", "192.168.1.199");
        System.out.println("\n【系统统计信息】");
        System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
        System.out.println(demo.system.getSystemStatistics());
        demo.system.logout();
        
        // 关闭系统
        demo.system.shutdown();
    }
}
