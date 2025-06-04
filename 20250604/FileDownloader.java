import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * 单个文件下载任务 - 完整修复版
 * 修复了DOCX等二进制文件下载400错误的问题
 * 修复日期: 2025-06-04
 */
public class FileDownloader implements Runnable {
	private final String downloadUrl;
	private final Path localPath;
	private final String fileName;
	private final String token;

	public FileDownloader(String downloadUrl, Path localPath, String fileName, String token) {
		this.downloadUrl = downloadUrl;
		this.localPath   = localPath;
		this.fileName    = fileName;
		this.token       = (token != null ? token : "");
	}

	@Override
	public void run() {
		try {
			// 检查文件是否已存在
			if (Files.exists(localPath) && Files.size(localPath) > 0) {
				System.out.println("跳过已存在文件: " + fileName);
				return;
			}

			if (downloadUrl == null || downloadUrl.trim().isEmpty()) {
				System.err.println("❌ 无下载链接：" + fileName);
				return;
			}

			System.out.println("📥 开始下载：" + fileName);

			// 🚀 修复1: 改进的HTTP连接设置
			HttpURLConnection conn = setupConnection(downloadUrl);

			// 🚀 修复2: 检查响应状态码
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				handleErrorResponse(conn, responseCode);
				return;
			}

			// 🚀 修复3: 获取文件信息
			String contentType = conn.getContentType();
			long contentLength = conn.getContentLengthLong();
			System.out.println("📄 " + fileName + " - 类型: " +
					(contentType != null ? contentType : "未知") +
					", 大小: " + formatFileSize(contentLength));

			// 🚀 修复4: 改进的文件下载逻辑
			long downloaded = downloadFile(conn);

			System.out.println("✅ 完成下载：" + fileName + " (" + formatFileSize(downloaded) + ")");
			GitHubDownloaderSimple.downloadedFiles.incrementAndGet();

			// 简易限速
			Thread.sleep(150);

		} catch (Exception e) {
			System.err.println("❌ 下载失败 " + fileName + ": " + e.getMessage());
			// 删除可能的损坏文件
			try {
				if (Files.exists(localPath)) {
					Files.delete(localPath);
				}
			} catch (Exception ignored) {}
		}
	}

	/**
	 * 🚀 新增: 设置HTTP连接 - 修复DOCX下载问题的核心
	 */
	private HttpURLConnection setupConnection(String url) throws Exception {
		HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();

		// 基础请求头
		conn.setRequestProperty("User-Agent", "GitHub-Downloader/1.0 (Java)");

		// 🔧 关键修复: 针对不同文件类型设置Accept头
		if (isBinaryFile(fileName)) {
			conn.setRequestProperty("Accept", "application/octet-stream, */*");
		} else {
			conn.setRequestProperty("Accept", "text/plain, application/octet-stream, */*");
		}

		// 避免压缩问题
		conn.setRequestProperty("Accept-Encoding", "identity");

		// 缓存控制
		conn.setRequestProperty("Cache-Control", "no-cache");

		// GitHub Token认证
		if (!token.isEmpty()) {
			conn.setRequestProperty("Authorization", "token " + token);
		}

		// 连接设置
		conn.setConnectTimeout(30000); // 30秒连接超时
		conn.setReadTimeout(300000);   // 5分钟读取超时
		conn.setInstanceFollowRedirects(true); // 允许重定向

		return conn;
	}

	/**
	 * 🚀 新增: 处理错误响应
	 */
	private void handleErrorResponse(HttpURLConnection conn, int responseCode) {
		System.err.println("❌ 下载失败 " + fileName + ": HTTP " + responseCode);

		// 尝试读取详细错误信息
		try (InputStream errorStream = conn.getErrorStream()) {
			if (errorStream != null) {
				Scanner scanner = new Scanner(errorStream);
				StringBuilder errorMsg = new StringBuilder();
				while (scanner.hasNextLine()) {
					errorMsg.append(scanner.nextLine()).append("\n");
				}
				scanner.close();

				String error = errorMsg.toString().trim();
				if (!error.isEmpty()) {
					System.err.println("   错误详情: " + error);
				}
			}
		} catch (Exception e) {
			System.err.println("   无法读取错误详情: " + e.getMessage());
		}

		// 针对常见错误码提供建议
		switch (responseCode) {
			case 400:
				System.err.println("   💡 建议: 可能是文件URL格式问题或文件已损坏");
				break;
			case 401:
				System.err.println("   💡 建议: Token认证失败，请检查Token权限");
				break;
			case 403:
				System.err.println("   💡 建议: 权限不足或API限制，请检查Token权限");
				break;
			case 404:
				System.err.println("   💡 建议: 文件不存在或已被删除");
				break;
			case 429:
				System.err.println("   💡 建议: API请求过于频繁，请稍后重试");
				break;
		}
	}

	/**
	 * 🚀 新增: 改进的文件下载逻辑
	 */
	private long downloadFile(HttpURLConnection conn) throws Exception {
		// 确保父目录存在
		Files.createDirectories(localPath.getParent());

		try (InputStream in = conn.getInputStream();
		     BufferedInputStream bis = new BufferedInputStream(in, 16384);
		     FileOutputStream out = new FileOutputStream(localPath.toFile());
		     BufferedOutputStream bos = new BufferedOutputStream(out, 16384)) {

			byte[] buffer = new byte[16384]; // 增大缓冲区
			int bytesRead;
			long totalDownloaded = 0;

			while ((bytesRead = bis.read(buffer)) != -1) {
				bos.write(buffer, 0, bytesRead);
				totalDownloaded += bytesRead;
			}

			bos.flush(); // 确保数据写入磁盘
			return totalDownloaded;
		}
	}

	/**
	 * 🚀 新增: 判断是否为二进制文件
	 */
	private boolean isBinaryFile(String fileName) {
		if (fileName == null) return false;

		String[] binaryExtensions = {
				// Office文档
				".docx", ".xlsx", ".pptx", ".doc", ".xls", ".ppt",
				// PDF和其他文档
				".pdf", ".rtf", ".odt", ".ods", ".odp",
				// 压缩文件
				".zip", ".rar", ".7z", ".tar", ".gz", ".bz2",
				// 可执行文件
				".exe", ".dll", ".so", ".dylib", ".jar",
				// 图像文件
				".jpg", ".jpeg", ".png", ".gif", ".bmp", ".tiff", ".webp",
				// 音视频文件
				".mp3", ".mp4", ".avi", ".mkv", ".wav", ".flac",
				// 其他二进制
				".bin", ".dat", ".db", ".sqlite"
		};

		String lowerName = fileName.toLowerCase();
		for (String ext : binaryExtensions) {
			if (lowerName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 🚀 新增: 格式化文件大小显示
	 */
	private String formatFileSize(long bytes) {
		if (bytes < 0) return "未知";
		if (bytes < 1024) return bytes + " B";
		if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
		if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024));
		return String.format("%.1f GB", bytes / (1024.0 * 1024 * 1024));
	}
}