package main.demo;

import main.service.LibraryManagementSystem;
import main.model.user.*;
import main.model.document.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 图书管理系统全面功能演示类
 * 展示所有主要功能和面向对象编程特性
 */
public class SystemDemonstration {
    
    private LibraryManagementSystem system;
    
    public SystemDemonstration() {
        this.system = new LibraryManagementSystem();
    }
    
    /**
     * 运行所有演示
     */
    public void runAllDemonstrations() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║              科研院所图书管理系统功能演示                    ║");
        System.out.println("║          Research Institution Library Management System      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        
        // 1. 面向对象编程特性演示
        demonstrateOOPPrinciples();
        
        // 2. 用户管理和权限系统演示
        demonstrateUserManagement();
        
        // 3. 文档管理演示
        demonstrateDocumentManagement();
        
        // 4. 搜索功能演示
        demonstrateSearchFunctionality();
        
        // 5. 借阅系统演示
        demonstrateBorrowingSystem();
        
        // 6. 多线程并发演示
        demonstrateMultithreading();
        
        // 7. 异常处理演示
        demonstrateExceptionHandling();
        
        // 8. 接口和抽象类演示
        demonstrateInterfacesAndAbstractClasses();
        
        // 9. 设计模式演示
        demonstrateDesignPatterns();
        
        // 10. 数据持久化演示
        demonstrateDataPersistence();
        
        // 11. 审计日志演示
        demonstrateAuditLogging();
        
        // 12. 系统配置和统计演示
        demonstrateSystemConfiguration();
        
        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                      演示完成                               ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }
    
    /**
     * 1. 面向对象编程特性演示
     * 演示封装、继承、多态的核心概念
     */
    private void demonstrateOOPPrinciples() {
        printSectionHeader("面向对象编程特性演示");
        
        // 封装演示 - 用户类的私有属性和公共方法
        System.out.println("1. 封装 (Encapsulation) 演示:");
        RegularUser user = new RegularUser("demo001", "demouser", "password123", 
                                         "演示用户", "demo@library.com", "13800000000", "计算机系");
        
        System.out.println("   - 用户信息通过getter方法访问（封装了内部状态）:");
        System.out.println("     用户ID: " + user.getUserId());
        System.out.println("     用户名: " + user.getUsername());
        System.out.println("     部门: " + user.getDepartment());
        System.out.println("     当前借阅数量: " + user.getCurrentBorrowCount());
        
        // 继承演示 - 不同用户类型继承自User基类
        System.out.println("\n2. 继承 (Inheritance) 演示:");
        AuthorizedUser authUser = new AuthorizedUser("auth001", "authuser", "auth123", 
                                                    "授权用户", "auth@library.com", "13800000001", 
                                                    "研发部", "PROJECT001", "人工智能");
        ArchiveManager archiveManager = new ArchiveManager("arch001", "archmanager", "arch123", 
                                                          "档案管理员", "arch@library.com", "13800000002", 
                                                          "档案部", "全部档案", "高级认证");
        Administrator admin = new Administrator("admin002", "demoadmin", "admin123", 
                                              "演示管理员", "admin@library.com", "13800000003", 
                                              "信息部", "高级", "系统管理");
        
        System.out.println("   - 不同用户类型都继承自User基类，但有各自的特性:");
        System.out.println("     普通用户类型: " + user.getUserTypeDescription());        System.out.println("     授权用户类型: " + authUser.getUserTypeDescription() + 
                         " (安全等级: " + authUser.getSecurityLevel() + ")");        System.out.println("     档案管理员类型: " + archiveManager.getUserTypeDescription() + 
                         " (管理区域: " + archiveManager.getManagementArea() + ")");        System.out.println("     系统管理员类型: " + admin.getUserTypeDescription() + 
                         " (权限级别: " + admin.getAdminLevel() + ")");
        
        // 多态演示 - 同一个方法在不同类中的不同实现
        System.out.println("\n3. 多态 (Polymorphism) 演示:");
        User[] users = {user, authUser, archiveManager, admin};
        
        System.out.println("   - 同一个canAccess方法在不同用户类型中的不同行为:");
        
        // 创建不同访问级别的文档进行测试
        PhysicalBook publicBook = new PhysicalBook("PB001", "公开图书", "作者A", "出版社A", 
                                                  "科技", "公开可借阅", LocalDate.now(), 
                                                  "ISBN001", 200, "中文", "A区", "A001");
        
        InternalDocument internalDoc = new InternalDocument("ID001", "内部资料", "作者B", "内部", 
                                                           "机密", "内部使用文档", LocalDate.now(),
                                                           "机密", "人工智能", "PROJECT001", "批准001", "定密人A");
        
        ArchiveDocument archiveDoc = new ArchiveDocument("AD001", "档案文档", "档案员", "档案室", 
                                                        "历史", "重要档案", LocalDate.now(),
                                                        "绝密", "档案室A", "ARCH001", "归档员A", true);
        
        for (User u : users) {
            System.out.println("     " + u.getUserTypeDescription() + ":");
            System.out.println("       - 公开图书访问: " + u.canAccess(publicBook));
            System.out.println("       - 内部资料访问: " + u.canAccess(internalDoc));
            System.out.println("       - 档案文档访问: " + u.canAccess(archiveDoc));
        }
        
        printSectionFooter();
    }
    
    /**
     * 2. 用户管理和权限系统演示
     */
    private void demonstrateUserManagement() {
        printSectionHeader("用户管理和权限系统演示");
        
        // 登录系统管理员账户
        System.out.println("1. 管理员登录:");
        boolean loginSuccess = system.login("admin", "admin123", "192.168.1.100");
        System.out.println("   管理员登录" + (loginSuccess ? "成功" : "失败"));
        
        if (loginSuccess) {
            // 创建不同类型的用户
            System.out.println("\n2. 创建不同类型用户:");
            
            RegularUser regularUser = new RegularUser("reg001", "student01", "student123", 
                                                    "张同学", "student@university.edu", "13900000001", "计算机学院");
            
            AuthorizedUser researcher = new AuthorizedUser("auth002", "researcher01", "research123", 
                                                         "李研究员", "researcher@institute.com", "13900000002", 
                                                         "AI实验室", "AI-2024-001", "机器学习");
            
            ArchiveManager archivist = new ArchiveManager("arch002", "archivist01", "archive123", 
                                                        "王档案员", "archivist@institute.com", "13900000003", 
                                                        "档案管理部", "技术档案区", "档案管理师认证");
            
            system.addUser(regularUser);
            system.addUser(researcher);
            system.addUser(archivist);
            
            System.out.println("   已创建: 普通用户、授权用户、档案管理员");
              // 演示不同用户的权限
            System.out.println("\n3. 用户权限演示:");
            System.out.println("   普通用户最大借阅数: " + regularUser.getMaxBorrowCount());
            System.out.println("   授权用户最大借阅数: " + researcher.getMaxBorrowCount());
            System.out.println("   档案管理员最大借阅数: " + archivist.getMaxBorrowCount());
            
            System.out.println("\n4. 用户状态管理:");
            System.out.println("   用户激活状态: " + regularUser.isActive());
            regularUser.setActive(false);
            System.out.println("   禁用后状态: " + regularUser.isActive());
            regularUser.setActive(true);
            System.out.println("   重新激活后状态: " + regularUser.isActive());
        }
        
        system.logout();
        printSectionFooter();
    }
    
    /**
     * 3. 文档管理演示
     */
    private void demonstrateDocumentManagement() {
        printSectionHeader("文档管理演示");
        
        // 登录档案管理员
        system.login("archive", "archive123", "192.168.1.101");
        
        System.out.println("1. 创建不同类型文档:");
        
        // 使用工厂模式创建不同类型的文档
        PhysicalBook physicalBook = DocumentFactory.createPhysicalBook(
            "PB002", "深度学习", "Ian Goodfellow", "人民邮电出版社", 
            "人工智能", "深度学习理论与实践", LocalDate.of(2023, 6, 1),
            "978-7-115-48042-0", 750, "中文", "B区2层", "B2-015"
        );
        
        EBook eBook = DocumentFactory.createEBook(
            "EB002", "机器学习实战", "Peter Harrington", "人民邮电出版社",
            "机器学习", "机器学习算法实现", LocalDate.of(2023, 3, 15),
            "978-7-115-29001-4", 400, "中文", "PDF", 15728640L, 
            "https://library.institute.edu/ebooks/ml-action.pdf"
        );
        
        InternalDocument internalDoc = DocumentFactory.createInternalDocument(
            "ID002", "AI项目技术报告", "项目组", "内部", "技术报告", 
            "人工智能项目技术总结", LocalDate.of(2024, 1, 10),
            "内部", "人工智能", "AI-2024-001", "TECH-2024-001", "技术总监"
        );
        
        ArchiveDocument archiveDoc = DocumentFactory.createArchiveDocument(
            "AD002", "科研项目历史档案", "档案室", "档案管理部", "历史档案",
            "2020-2023年重要科研项目档案", LocalDate.of(2024, 2, 1),
            "机密", "科研档案区", "SCI-ARCH-2024-001", "高级档案员", true
        );
        
        // 添加文档到系统
        system.addDocument(physicalBook);
        system.addDocument(eBook);
        system.addDocument(internalDoc);
        system.addDocument(archiveDoc);
        
        System.out.println("   已创建: 实体书、电子书、内部资料、档案文档");
          // 演示文档属性
        System.out.println("\n2. 文档属性演示:");        System.out.println("   实体书位置: " + physicalBook.getLocation() + ", 书架号: " + physicalBook.getShelfNumber());
        System.out.println("   电子书格式: " + eBook.getFileFormat() + ", 文件大小: " + eBook.getFileSize() / (1024*1024) + "MB");        System.out.println("   内部资料密级: " + internalDoc.getSecurityLevel() + ", 项目代码: " + internalDoc.getProjectCode());
        System.out.println("   档案文档归档号: " + archiveDoc.getArchiveNumber() + ", 需要特殊权限: " + archiveDoc.isRequiresSpecialPermission());
        
        // 演示借阅状态
        System.out.println("\n3. 文档状态管理:");        System.out.println("   实体书借阅状态: " + (!physicalBook.isAvailable() ? "已借出" : "可借阅"));
        System.out.println("   电子书借阅状态: " + (!eBook.isAvailable() ? "已借出" : "可借阅"));
        
        system.logout();
        printSectionFooter();
    }
    
    /**
     * 4. 搜索功能演示
     */
    private void demonstrateSearchFunctionality() {
        printSectionHeader("搜索功能演示");
        
        // 登录普通用户进行搜索
        system.login("student01", "student123", "192.168.1.102");
        
        System.out.println("1. 关键词搜索:");
        List<Object> searchResults = system.searchDocuments("机器学习");
        System.out.println("   搜索'机器学习'结果数量: " + searchResults.size());
        for (Object result : searchResults) {
            if (result instanceof Document) {
                Document doc = (Document) result;
                System.out.println("   - " + doc.getTitle() + " (" + doc.getClass().getSimpleName() + ")");
            }
        }
        
        System.out.println("\n2. 高级搜索:");
        List<Object> advancedResults = system.advancedSearchDocuments("深度学习", "Ian Goodfellow", "人工智能", null);
        System.out.println("   高级搜索结果数量: " + advancedResults.size());
        for (Object result : advancedResults) {
            if (result instanceof Document) {
                Document doc = (Document) result;
                System.out.println("   - " + doc.getTitle() + " (作者: " + doc.getAuthor() + ")");
            }
        }
        
        System.out.println("\n3. 权限过滤搜索:");
        // 普通用户搜索，应该过滤掉无权限访问的文档
        List<Object> filteredResults = system.searchDocuments("项目");
        System.out.println("   普通用户搜索'项目'结果数量: " + filteredResults.size());
        System.out.println("   (系统自动过滤了无权限访问的内部资料和档案文档)");
        
        system.logout();
        printSectionFooter();
    }
    
    /**
     * 5. 借阅系统演示
     */
    private void demonstrateBorrowingSystem() {
        printSectionHeader("借阅系统演示");
        
        // 登录普通用户进行借阅
        system.login("student01", "student123", "192.168.1.103");
        
        System.out.println("1. 文档借阅:");
        String borrowResult1 = system.borrowDocument("PB002");
        System.out.println("   借阅实体书结果: " + borrowResult1);
        
        String borrowResult2 = system.borrowDocument("EB002");
        System.out.println("   借阅电子书结果: " + borrowResult2);
        
        System.out.println("\n2. 查看借阅记录:");
        List<Document> borrowedDocs = system.getMyBorrowedDocuments();
        System.out.println("   当前借阅文档数量: " + borrowedDocs.size());
        for (Document doc : borrowedDocs) {
            System.out.println("   - " + doc.getTitle() + " (类型: " + doc.getClass().getSimpleName() + ")");
        }
        
        System.out.println("\n3. 续借操作:");
        String extendResult = system.extendDocument("PB002", 14);
        System.out.println("   续借结果: " + extendResult);
        
        System.out.println("\n4. 归还操作:");
        String returnResult = system.returnDocument("EB002");
        System.out.println("   归还结果: " + returnResult);
        
        // 验证归还后的状态
        List<Document> remainingDocs = system.getMyBorrowedDocuments();
        System.out.println("   归还后剩余借阅数量: " + remainingDocs.size());
        
        system.logout();
        printSectionFooter();
    }
    
    /**
     * 6. 多线程并发演示
     */
    private void demonstrateMultithreading() {
        printSectionHeader("多线程并发演示");
        
        // 使用系统内置的多线程演示
        system.demonstrateMultithreading();
        
        printSectionFooter();
    }
    
    /**
     * 7. 异常处理演示
     */
    private void demonstrateExceptionHandling() {
        printSectionHeader("异常处理演示");
        
        System.out.println("1. 认证异常演示:");
        try {
            // 尝试使用错误密码登录
            boolean loginResult = system.login("admin", "wrongpassword", "192.168.1.104");
            System.out.println("   错误密码登录结果: " + (loginResult ? "成功" : "失败"));
        } catch (Exception e) {
            System.out.println("   捕获异常: " + e.getMessage());
        }
        
        System.out.println("\n2. 借阅异常演示:");
        // 登录用户
        system.login("student01", "student123", "192.168.1.105");
        
        try {
            // 尝试借阅不存在的文档
            String result = system.borrowDocument("NONEXISTENT");
            System.out.println("   借阅不存在文档结果: " + result);
            
            // 尝试重复借阅同一文档
            system.borrowDocument("PB002");
            String duplicateResult = system.borrowDocument("PB002");
            System.out.println("   重复借阅结果: " + duplicateResult);
            
        } catch (Exception e) {
            System.out.println("   捕获借阅异常: " + e.getMessage());
        }
        
        System.out.println("\n3. 权限异常演示:");
        try {
            // 普通用户尝试借阅无权限文档
            String result = system.borrowDocument("ID002"); // 内部资料
            System.out.println("   无权限借阅结果: " + result);
        } catch (Exception e) {
            System.out.println("   捕获权限异常: " + e.getMessage());
        }
        
        system.logout();
        printSectionFooter();
    }
      /**
     * 8. 接口和抽象类演示
     */
    private void demonstrateInterfacesAndAbstractClasses() {
        printSectionHeader("接口和抽象类演示");
        
        System.out.println("1. Borrowable接口演示:");
        PhysicalBook book = new PhysicalBook("DEMO001", "演示图书", "演示作者", "演示出版社", 
                                           "演示", "接口演示用书", LocalDate.now(), 
                                           "DEMO-ISBN", 100, "中文", "演示区", "DEMO-001");
        
        // 创建演示用户
        RegularUser demoUser = new RegularUser("demo001", "demouser", "password123", 
                                             "演示用户", "demo@example.com", "13800000001", "演示部门");
        
        // 演示Borrowable接口的方法
        System.out.println("   图书可借阅状态: " + book.isAvailable());
        System.out.println("   借阅操作结果: " + book.borrow(demoUser, LocalDate.now().plusDays(30)));
        System.out.println("   借阅后状态: " + book.isAvailable());
        System.out.println("   归还操作结果: " + book.returnResource(demoUser, LocalDate.now()));
        System.out.println("   归还后状态: " + book.isAvailable());
        
        System.out.println("\n2. Searchable接口演示:");
        // 演示搜索接口的实现
        boolean titleMatch = book.matchesKeyword("演示");
        boolean authorMatch = book.matchesKeyword("演示作者");
        boolean noMatch = book.matchesKeyword("不存在的关键词");
        
        System.out.println("   标题匹配'演示': " + titleMatch);
        System.out.println("   作者匹配'演示作者': " + authorMatch);
        System.out.println("   关键词'不存在的关键词': " + noMatch);
        
        System.out.println("\n3. Auditable接口演示:");
        // 演示审计接口
        String auditInfo = book.getAuditInfo();
        System.out.println("   审计信息: " + auditInfo);
        
        System.out.println("\n4. 抽象类继承演示:");
        System.out.println("   Document抽象类的具体实现:");
        System.out.println("   - PhysicalBook: " + book.getClass().getSimpleName());
        System.out.println("   - 基本信息方法继承: getTitle() = " + book.getTitle());
        System.out.println("   - 抽象方法实现: getDocumentType() = " + book.getDocumentType());
        
        System.out.println("\n   User抽象类的具体实现:");
        RegularUser user = new RegularUser("demo002", "demouser2", "demo123", 
                                         "演示用户2", "demo2@test.com", "13900000000", "演示部门");
        System.out.println("   - RegularUser: " + user.getClass().getSimpleName());
        System.out.println("   - 基本方法继承: getUsername() = " + user.getUsername());
        System.out.println("   - 抽象方法实现: getUserTypeDescription() = " + user.getUserTypeDescription());
        
        printSectionFooter();
    }
    
    /**
     * 9. 设计模式演示
     */
    private void demonstrateDesignPatterns() {
        printSectionHeader("设计模式演示");
        
        System.out.println("1. 工厂模式 (Factory Pattern) 演示:");
        System.out.println("   使用DocumentFactory创建不同类型文档:");
        
        Document factoryBook = DocumentFactory.createPhysicalBook(
            "FACTORY001", "工厂模式演示书", "设计模式作者", "设计模式出版社",
            "设计模式", "演示工厂模式", LocalDate.now(),
            "FACTORY-ISBN", 200, "中文", "设计模式区", "DP-001"
        );
        
        Document factoryEBook = DocumentFactory.createEBook(
            "FACTORY002", "工厂模式电子书", "电子书作者", "电子出版社",
            "设计模式", "演示工厂模式电子书", LocalDate.now(),
            "EBOOK-ISBN", 150, "中文", "PDF", 5242880L, "http://example.com/ebook.pdf"
        );
        
        System.out.println("   工厂创建的实体书: " + factoryBook.getTitle() + " (类型: " + factoryBook.getClass().getSimpleName() + ")");
        System.out.println("   工厂创建的电子书: " + factoryEBook.getTitle() + " (类型: " + factoryEBook.getClass().getSimpleName() + ")");
        
        System.out.println("\n2. 单例模式 (Singleton Pattern) 演示:");
        System.out.println("   AuditService使用单例模式:");
        
        // 获取两个AuditService实例，验证是否为同一个对象
        main.service.AuditService audit1 = main.service.AuditService.getInstance();
        main.service.AuditService audit2 = main.service.AuditService.getInstance();
        
        System.out.println("   第一个实例hashCode: " + audit1.hashCode());
        System.out.println("   第二个实例hashCode: " + audit2.hashCode());
        System.out.println("   两个实例是否相同: " + (audit1 == audit2));
        
        System.out.println("\n3. 观察者模式的体现:");
        System.out.println("   审计服务监听系统操作:");
        // 登录操作会触发审计记录
        system.login("admin", "admin123", "192.168.1.106");
        System.out.println("   登录操作已记录到审计日志");
        
        // 借阅操作也会触发审计记录
        String borrowResult = system.borrowDocument("PB001");
        System.out.println("   借阅操作已记录到审计日志: " + borrowResult);
        
        system.logout();
        
        printSectionFooter();
    }
    
    /**
     * 10. 数据持久化演示
     */
    private void demonstrateDataPersistence() {
        printSectionHeader("数据持久化演示");
        
        System.out.println("1. 数据保存演示:");
        boolean saveResult = system.saveSystemData();
        System.out.println("   系统数据保存结果: " + (saveResult ? "成功" : "失败"));
        
        System.out.println("\n2. 序列化特性演示:");
        System.out.println("   所有用户和文档类都实现了Serializable接口");
        System.out.println("   支持对象的序列化和反序列化操作");
        System.out.println("   数据以二进制格式保存，确保数据完整性");
        
        System.out.println("\n3. 数据加载演示:");
        System.out.println("   系统启动时自动加载保存的用户和文档数据");
        System.out.println("   如果没有现有数据，系统会创建默认的管理员账户和示例文档");
        
        printSectionFooter();
    }
    
    /**
     * 11. 审计日志演示
     */
    private void demonstrateAuditLogging() {
        printSectionHeader("审计日志演示");
        
        System.out.println("1. 用户操作审计:");
        System.out.println("   系统记录所有重要操作的审计日志");
        System.out.println("   包括：登录/登出、借阅/归还、文档添加、用户管理等");
        
        System.out.println("\n2. 安全性审计:");
        System.out.println("   记录登录失败、权限违规等安全事件");
        System.out.println("   支持IP地址记录和时间戳");
        
        System.out.println("\n3. 操作追踪:");
        System.out.println("   每个操作都有唯一标识和详细描述");
        System.out.println("   支持按用户、按时间、按操作类型查询");
        
        printSectionFooter();
    }
    
    /**
     * 12. 系统配置和统计演示
     */
    private void demonstrateSystemConfiguration() {
        printSectionHeader("系统配置和统计演示");
        
        System.out.println("1. 系统配置信息:");
        System.out.println("   " + main.model.system.SystemConfig.getSystemInfo());
        
        System.out.println("\n2. 系统统计信息:");
        // 登录管理员查看统计
        system.login("admin", "admin123", "192.168.1.107");
        String statistics = system.getSystemStatistics();
        System.out.println(statistics);
        
        System.out.println("\n3. 系统常量配置:");
        System.out.println("   默认借阅天数: " + main.model.system.SystemConfig.DEFAULT_BORROW_DAYS + " 天");
        System.out.println("   最大续借天数: " + main.model.system.SystemConfig.MAX_EXTEND_DAYS + " 天");
        System.out.println("   普通用户借阅限制: " + main.model.system.SystemConfig.REGULAR_USER_BORROW_LIMIT + " 本");
        System.out.println("   授权用户借阅限制: " + main.model.system.SystemConfig.AUTHORIZED_USER_BORROW_LIMIT + " 本");
        
        system.logout();
        printSectionFooter();
    }
    
    /**
     * 打印章节标题
     */
    private void printSectionHeader(String title) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("  " + title);
        System.out.println("=".repeat(80));
    }
    
    /**
     * 打印章节结尾
     */
    private void printSectionFooter() {
        System.out.println("-".repeat(80));
        System.out.println("按Enter键继续...");
        try {
            System.in.read();
        } catch (Exception e) {
            // 忽略异常
        }
    }
    
    /**
     * 主方法 - 运行演示
     */
    public static void main(String[] args) {
        SystemDemonstration demo = new SystemDemonstration();
        demo.runAllDemonstrations();
        
        // 关闭系统
        demo.system.shutdown();
    }
}
