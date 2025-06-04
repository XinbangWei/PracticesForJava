import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class BranchInputHandler {
	private static final String API_BASE = "https://api.github.com";

	/**
	 * 拉取仓库的分支列表，让用户从中选择一个
	 * @param repository 格式 "owner/repo"
	 * @param token      GitHub 访问令牌（可为空）
	 * @return 用户选定的分支名
	 */
	public static String getBranchFromUser(String repository, String token) {
		List<String> branches = fetchBranchNames(repository, token);
		if (branches.isEmpty()) {
			System.out.println("⚠️ 未能获取到分支列表，使用默认分支 main");
			return "main";
		}

		Scanner scanner = new Scanner(System.in);
		System.out.println("\n可用分支列表:");
		for (int i = 0; i < branches.size(); i++) {
			System.out.printf("  %d. %s%n", i + 1, branches.get(i));
		}
		System.out.print("请输入分支编号 (1-" + branches.size() + ", 默认 1): ");
		String line = scanner.nextLine().trim();
		try {
			int idx = Integer.parseInt(line);
			if (idx >= 1 && idx <= branches.size()) {
				return branches.get(idx - 1);
			}
		} catch (Exception ignored) { }
		// 输入不合法或回车直接选第一个
		return branches.get(0);
	}

	private static List<String> fetchBranchNames(String repository, String token) {
		String url = API_BASE + "/repos/" + repository + "/branches";
		try {
			HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setRequestProperty("User-Agent", "GitHub-Downloader/1.0");
			if (token != null && !token.isEmpty()) {
				conn.setRequestProperty("Authorization", "token " + token);
			}
			if (conn.getResponseCode() != 200) {
				return Collections.emptyList();
			}
			Scanner scanner = new Scanner(conn.getInputStream());
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine());
			}
			scanner.close();
			// 解析 JSON 数组
			List<Map<String, Object>> items = SimpleJsonParser.parseArray(sb.toString());
			List<String> names = new ArrayList<>();
			for (Map<String, Object> item : items) {
				Object n = item.get("name");
				if (n instanceof String) {
					names.add((String) n);
				}
			}
			return names;
		} catch (IOException e) {
			return Collections.emptyList();
		}
	}
}