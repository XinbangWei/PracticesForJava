import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 单个文件下载任务
 * 简化版，不打印线程名
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
			if (Files.exists(localPath) && Files.size(localPath) > 0) {
				// 文件已存在，跳过
				return;
			}
			if (downloadUrl == null) {
				System.err.println("无下载链接：" + fileName);
				return;
			}

			System.out.println("开始下载：" + fileName);
			HttpURLConnection conn = (HttpURLConnection) new URL(downloadUrl).openConnection();
			conn.setRequestProperty("User-Agent", "GitHub-Downloader/1.0");
			if (!token.isEmpty()) {
				conn.setRequestProperty("Authorization", "token " + token);
			}

			try (InputStream in = conn.getInputStream();
			     FileOutputStream out = new FileOutputStream(localPath.toFile())) {
				byte[] buf = new byte[8192];
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			}

			System.out.println("完成下载：" + fileName);
			GitHubDownloaderSimple.downloadedFiles.incrementAndGet();
			// 简易限速
			Thread.sleep(150);
		} catch (Exception e) {
			System.err.println("下载失败 " + fileName + ": " + e.getMessage());
		}
	}
}