package main;

import main.service.LibraryManagementSystem;
import main.demo.SystemDemonstration;
import main.demo.QuickDemo;
import java.util.Scanner;

/**
 * 图书管理系统主程序
 * 提供多种运行模式：正常使用、演示模式等
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("请选择运行模式：");
        System.out.println("1. 正常使用模式 - 完整的图书管理系统");
        System.out.println("2. 快速演示模式 - 核心功能演示");
        System.out.println("3. 完整演示模式 - 所有功能详细演示");
        System.out.println("0. 退出");
        System.out.println();
        System.out.print("请输入选择 (0-3): ");
        
        try {
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    System.out.println("\n启动正常使用模式...");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    LibraryManagementSystem system = new LibraryManagementSystem();
                    system.startConsole();
                    break;
                    
                case "2":
                    System.out.println("\n启动快速演示模式...");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    QuickDemo quickDemo = new QuickDemo();
                    quickDemo.runQuickDemo();
                    break;
                    
                case "3":
                    System.out.println("\n启动完整演示模式...");
                    System.out.println("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
                    SystemDemonstration fullDemo = new SystemDemonstration();
                    fullDemo.runAllDemonstrations();
                    break;
                case "0":
                    System.out.println("\n感谢使用图书管理系统！");
                    break;
                    
                default:
                    System.out.println("\n无效选择，请重新运行程序并输入 0-4 之间的数字。");
                    break;
            }
            
        } catch (Exception e) {
            System.err.println("程序运行出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        
        System.out.println("\n程序已退出。");
    }
}
