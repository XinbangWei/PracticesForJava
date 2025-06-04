import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主类：简化版 GitHub 仓库下载器
 * 支持分支选择，并在路径中添加分支文件夹
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

			downloadDirectory(executor, contentsUrl, basePath);
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

	private void downloadDirectory(ExecutorService executor, String url, Path localPath) {
		try {
			String json = fetchJsonString(url);
			List<Map<String, Object>> items = SimpleJsonParser.parseArray(json);
			for (Map<String, Object> item : items) {
				String type = (String) item.get("type");
				String name = (String) item.get("name");
				Path itemPath = localPath.resolve(name);

				if ("file".equals(type)) {
					totalFiles.incrementAndGet();
					String downloadUrl = (String) item.get("download_url");
					executor.submit(new FileDownloader(downloadUrl, itemPath, name, accessToken));
				} else if ("dir".equals(type)) {
					Files.createDirectories(itemPath);
					String subUrl = (String) item.get("url") + "?ref=" + branch;
					downloadDirectory(executor, subUrl, itemPath);
				}
			}
		} catch (Exception e) {
			System.err.println("处理目录失败: " + e.getMessage());
		}
	}

	private String fetchJsonString(String urlString) throws IOException {
		HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
		conn.setRequestProperty("User-Agent", "GitHub-Downloader/1.0");
		if (!accessToken.isEmpty()) {
			conn.setRequestProperty("Authorization", "token " + accessToken);
		}
		if (conn.getResponseCode() != 200) {
			throw new IOException("API 请求失败，状态码: " + conn.getResponseCode());
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
		System.out.println("=====================================");
	}
}