import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.Scanner;

public class TokenManager {
	private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".github-downloader";
	private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "config.properties";

	/**
	 * 获取GitHub Token，优先从配置文件读取，没有则提示用户输入
	 */
	public static String getGitHubToken() {
		// 先尝试从配置文件读取
		String token = loadTokenFromConfig();
		if (token != null && !token.isEmpty()) {
			System.out.println("✓ 使用已保存的GitHub Token (高频率限制: 5000次/小时)");
			return token;
		}

		// 没有配置文件，询问用户
		return promptForToken();
	}

	private static String loadTokenFromConfig() {
		try {
			Path configPath = Paths.get(CONFIG_FILE);
			if (!Files.exists(configPath)) {
				return null;
			}

			Properties props = new Properties();
			try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
				props.load(fis);
				return props.getProperty("github.token", "").trim();
			}

		} catch (IOException e) {
			System.err.println("读取配置文件失败: " + e.getMessage());
			return null;
		}
	}

	private static String promptForToken() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("\n=== GitHub Token 配置 ===");
		System.out.println("GitHub API 访问限制:");
		System.out.println("• 无Token: 60次/小时 (可能不够用)");
		System.out.println("• 有Token: 5000次/小时 (推荐)");
		System.out.println("\n如何获取Token:");
		System.out.println("1. 访问: https://github.com/settings/tokens");
		System.out.println("2. 点击 'Generate new token (classic)'");
		System.out.println("3. 勾选 'repo' 权限");
		System.out.println("4. 复制生成的token");

		System.out.println("\n选项:");
		System.out.println("1. 输入GitHub Token (推荐)");
		System.out.println("2. 使用访客模式 (60次/小时限制)");
		System.out.print("请选择 (1/2): ");

		String choice = scanner.nextLine().trim();

		if ("1".equals(choice)) {
			return inputAndSaveToken(scanner);
		} else {
			System.out.println("✓ 使用访客模式 (60次/小时限制)");
			return "";
		}
	}

	private static String inputAndSaveToken(Scanner scanner) {
		System.out.print("\n请输入GitHub Token (ghp_开头): ");
		String token = scanner.nextLine().trim();

		if (token.isEmpty()) {
			System.out.println("✓ 跳过Token配置，使用访客模式");
			return "";
		}

		// 简单验证Token格式
		if (!token.startsWith("ghp_") && !token.startsWith("github_pat_")) {
			System.out.println("⚠ Token格式可能不正确，但仍会尝试使用");
		}

		// 询问是否保存
		System.out.print("是否保存Token到本地? (y/n): ");
		String saveChoice = scanner.nextLine().trim().toLowerCase();

		if ("y".equals(saveChoice) || "yes".equals(saveChoice)) {
			saveTokenToConfig(token);
			System.out.println("✓ Token已保存到: " + CONFIG_FILE);
		}

		System.out.println("✓ 使用Token模式 (5000次/小时限制)");
		return token;
	}

	private static void saveTokenToConfig(String token) {
		try {
			// 创建配置目录
			Path configDir = Paths.get(CONFIG_DIR);
			Files.createDirectories(configDir);

			// 保存配置
			Properties props = new Properties();
			props.setProperty("github.token", token);
			props.setProperty("created.time", String.valueOf(System.currentTimeMillis()));

			try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
				props.store(fos, "GitHub Downloader Configuration");
			}

			// 设置文件权限 (仅所有者可读写)
			if (!System.getProperty("os.name").toLowerCase().contains("windows")) {
				Files.setPosixFilePermissions(Paths.get(CONFIG_FILE),
						java.nio.file.attribute.PosixFilePermissions.fromString("rw-------"));
			}

		} catch (IOException e) {
			System.err.println("保存配置失败: " + e.getMessage());
		}
	}

	/**
	 * 删除保存的Token配置
	 */
	public static void clearToken() {
		try {
			Path configPath = Paths.get(CONFIG_FILE);
			if (Files.exists(configPath)) {
				Files.delete(configPath);
				System.out.println("✓ Token配置已清除");
			}
		} catch (IOException e) {
			System.err.println("清除配置失败: " + e.getMessage());
		}
	}

	/**
	 * 显示当前配置状态
	 */
	public static void showStatus() {
		String token = loadTokenFromConfig();
		System.out.println("配置文件: " + CONFIG_FILE);
		if (token != null && !token.isEmpty()) {
			System.out.println("Token状态: 已配置 (****" + token.substring(Math.max(0, token.length() - 4)) + ")");
			System.out.println("API限制: 5000次/小时");
		} else {
			System.out.println("Token状态: 未配置");
			System.out.println("API限制: 60次/小时");
		}
	}
}