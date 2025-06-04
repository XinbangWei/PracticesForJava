import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主类：GitHub 仓库下载器 - 修复版
 * 修复了中文文件名和特殊文件的下载问题
 */
public class GitHubDownloaderSimple {
	public static final String API_BASE = "https://api.github.com";
	public static final int THREAD_COUNT = 6;
	public static AtomicInteger totalFiles      = new AtomicInteger(0);
	public static AtomicInteger downloadedFiles = new AtomicInteger(0);

	private final String accessToken;
	private final String branch;

	public GitHubDownloaderSimple(String token, String branch) {
		this.accessToken = (token != null ? token : "");
		this.branch      = (branch != null && !branch.isEmpty() ? branch : "main");
	}

	public static void main(String[] args) {
		System.out.println("GitHub 仓库下载器");
		System.out.println("=====================================");

		String token   = TokenManager.getGitHubToken();
		String repo    = RepositoryInputHandler.getRepositoryFromUser();
		String branch  = BranchInputHandler.getBranchFromUser(repo, token);
		String localPath = PathManager.getDownloadPath(repo, branch);

		System.out.println("\n下载配置:");
		System.out.println("• 仓库:   " + repo);
		System.out.println("• 分支:   " + branch);
		System.out.println("• 路径:   " + localPath);
		System.out.println("• API 限制:" + (token.isEmpty() ? "60次/小时" : "5000次/小时"));
		System.out.println("=====================================");

		GitHubDownloaderSimple dl = new GitHubDownloaderSimple(token, branch);
		long start = System.currentTimeMillis();
		dl.downloadRepository(repo, localPath);
		long end   = System.currentTimeMillis();
		System.out.println("总耗时: " + ((end - start) / 1000) + " 秒");
	}

	public void downloadRepository(String repository, String localBasePath) {
		ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
		try {
			Path basePath = Paths.get(localBasePath);
			Files.createDirectories(basePath);

			String contentsUrl = API_BASE
					+ "/repos/" + repository
					+ "/contents?ref=" + branch;
			System.out.println("获取文件列表...");

			downloadDirectory(executor, contentsUrl, basePath, repository, 0);
		} catch (IOException e) {
			System.err.println("下载失败: " + e.getMessage());
		} finally {
			executor.shutdown();
			try {
				executor.awaitTermination(300, TimeUnit.SECONDS);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
			}
			printSummary();
		}
	}

	/**
	 * 🚀 修复版本：增加了repository参数和深度限制
	 */
	/**
	 * 🚀 修复版本：修复URL重复参数问题
	 */
	private void downloadDirectory(ExecutorService executor, String url, Path localPath, String repository, int depth) {
		// 防止无限递归
		if (depth > 10) {
			System.err.println("⚠️ 目录层级过深，跳过: " + localPath);
			return;
		}

		try {
			String json = fetchJsonString(url);
			if (json == null || json.trim().isEmpty()) {
				System.err.println("❌ 获取目录内容失败: " + url);
				return;
			}

			List<Map<String, Object>> items = SimpleJsonParser.parseArray(json);
			System.out.println("📁 处理目录: " + localPath + " (包含 " + items.size() + " 项)");

			for (Map<String, Object> item : items) {
				String type = (String) item.get("type");
				String name = (String) item.get("name");

				if (type == null || name == null) {
					System.err.println("⚠️ 解析项目信息失败，跳过");
					continue;
				}

				Path itemPath = localPath.resolve(name);

				if ("file".equals(type)) {
					totalFiles.incrementAndGet();

					// 🔧 获取下载URL
					String downloadUrl = getValidDownloadUrl(item, repository, name);

					if (downloadUrl != null) {
						System.out.println("📄 准备下载: " + name);
						executor.submit(new FileDownloader(downloadUrl, itemPath, name, accessToken));
					} else {
						System.err.println("❌ 无法获取有效下载链接: " + name);
					}

				} else if ("dir".equals(type)) {
					try {
						Files.createDirectories(itemPath);
						String subUrl = (String) item.get("url");
						if (subUrl != null) {
							// 🚀 关键修复：正确处理URL参数
							subUrl = buildUrlWithRef(subUrl, branch);
							System.out.println("🔗 子目录URL: " + subUrl);
							downloadDirectory(executor, subUrl, itemPath, repository, depth + 1);
						} else {
							System.err.println("❌ 目录缺少URL: " + name);
						}
					} catch (IOException e) {
						System.err.println("❌ 创建目录失败: " + itemPath + " - " + e.getMessage());
					}
				}
			}
		} catch (Exception e) {
			System.err.println("❌ 处理目录失败 " + url + ": " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 🚀 新增：正确构造带有ref参数的URL
	 */
	private String buildUrlWithRef(String baseUrl, String ref) {
		if (baseUrl == null || baseUrl.trim().isEmpty()) {
			return baseUrl;
		}

		// 检查URL是否已经包含ref参数
		if (baseUrl.contains("?ref=") || baseUrl.contains("&ref=")) {
			// 如果已经包含ref参数，直接返回
			System.out.println("   URL已包含ref参数: " + baseUrl);
			return baseUrl;
		}

		// 检查URL是否已经包含其他查询参数
		if (baseUrl.contains("?")) {
			// 使用&添加ref参数
			return baseUrl + "&ref=" + ref;
		} else {
			// 使用?添加ref参数
			return baseUrl + "?ref=" + ref;
		}
	}
	/**
	 * 🚀 修复版：获取有效的下载URL，处理文件路径
	 */
	private String getValidDownloadUrl(Map<String, Object> item, String repository, String fileName) {
		// 获取文件的完整路径
		String filePath = (String) item.get("path");
		if (filePath == null) {
			filePath = fileName; // 降级为文件名
		}

		// 方法1：尝试使用API提供的download_url
		String downloadUrl = (String) item.get("download_url");
		if (downloadUrl != null && !downloadUrl.trim().isEmpty()) {
			System.out.println("   使用API下载URL: " + downloadUrl);
			return downloadUrl;
		}

		// 方法2：构造raw.githubusercontent.com URL
		try {
			// URL编码完整路径，处理中文字符和特殊字符
			String encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString())
					.replace("+", "%20") // 空格用%20而不是+
					.replace("%2F", "/"); // 保持路径分隔符

			String rawUrl = "https://raw.githubusercontent.com/" + repository + "/" + branch + "/" + encodedPath;
			System.out.println("   构造Raw URL: " + rawUrl);
			return rawUrl;
		} catch (Exception e) {
			System.err.println("   URL编码失败: " + e.getMessage());
		}

		// 方法3：使用html_url构造
		String htmlUrl = (String) item.get("html_url");
		if (htmlUrl != null) {
			try {
				String rawUrl = htmlUrl.replace("github.com", "raw.githubusercontent.com")
						.replace("/blob/", "/");
				System.out.println("   从HTML URL构造: " + rawUrl);
				return rawUrl;
			} catch (Exception e) {
				System.err.println("   从HTML URL构造失败: " + e.getMessage());
			}
		}

		// 方法4：使用GitHub API的原始内容端点
		try {
			String encodedPath = URLEncoder.encode(filePath, StandardCharsets.UTF_8.toString())
					.replace("+", "%20")
					.replace("%2F", "/");
			String apiRawUrl = API_BASE + "/repos/" + repository + "/contents/" + encodedPath + "?ref=" + branch;
			System.out.println("   尝试API原始内容: " + apiRawUrl);
			return apiRawUrl;
		} catch (Exception e) {
			System.err.println("   API原始内容URL构造失败: " + e.getMessage());
		}

		return null;
	}

	/**
	 * 🚀 改进：增加更详细的错误处理
	 */
	private String fetchJsonString(String urlString) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
		conn.setRequestProperty("User-Agent", "GitHub-Downloader/1.0");
		conn.setRequestProperty("Accept", "application/vnd.github.v3+json");

		if (!accessToken.isEmpty()) {
			conn.setRequestProperty("Authorization", "token " + accessToken);
		}

		int responseCode = conn.getResponseCode();
		if (responseCode != 200) {
			String errorMsg = "API 请求失败，状态码: " + responseCode;

			// 尝试读取错误详情
			try (Scanner scanner = new Scanner(conn.getErrorStream())) {
				StringBuilder sb = new StringBuilder();
				while (scanner.hasNextLine()) {
					sb.append(scanner.nextLine());
				}
				if (sb.length() > 0) {
					errorMsg += "\n详情: " + sb.toString();
				}
			} catch (Exception ignored) {}

			throw new IOException(errorMsg);
		}

		StringBuilder sb = new StringBuilder();
		try (Scanner scanner = new Scanner(conn.getInputStream())) {
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine());
			}
		}
		return sb.toString();
	}

	private void printSummary() {
		System.out.println("\n=====================================");
		System.out.println("下载完成统计:");
		System.out.println("• 总文件数: " + totalFiles.get());
		System.out.println("• 已下载:   " + downloadedFiles.get());
		System.out.println("• 成功率:   " + String.format("%.1f%%",
				totalFiles.get() > 0 ? (downloadedFiles.get() * 100.0 / totalFiles.get()) : 0));
		System.out.println("=====================================");
	}
}