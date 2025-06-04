import java.util.Scanner;
import java.util.regex.Pattern;

public class RepositoryInputHandler {
	private static final Pattern REPO_PATTERN = Pattern.compile("^[a-zA-Z0-9_.-]+/[a-zA-Z0-9_.-]+$");

	public static String getRepositoryFromUser() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("支持的仓库格式:");
		System.out.println("1. owner/repo-name                    (如: facebook/react)");
		System.out.println("2. https://github.com/owner/repo-name");

		while (true) {
			System.out.print("\n请输入GitHub仓库: ");
			String input = scanner.nextLine().trim();

			if (input.isEmpty()) continue;

			String repo = parseRepository(input);
			if (repo != null) {
				System.out.println("✓ 目标仓库: " + repo);
				return repo;
			} else {
				System.out.println("× 格式错误，请重新输入");
			}
		}
	}

	private static String parseRepository(String input) {
		// 格式1: 直接的 owner/repo
		if (REPO_PATTERN.matcher(input).matches()) {
			return input;
		}

		// 格式2: GitHub完整URL
		if (input.startsWith("https://github.com/")) {
			String path = input.substring("https://github.com/".length());
			// 移除可能的.git后缀
			path = path.replaceAll("\\.git$", "");
			// 移除可能的末尾斜杠
			path = path.replaceAll("/$", "");

			if (REPO_PATTERN.matcher(path).matches()) {
				return path;
			}
		}

		return null;
	}
}