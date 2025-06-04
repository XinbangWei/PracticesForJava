import java.io.File;

/**
 * 下载路径管理，固定为：~/Downloads/GitHub-Repos/{repoName}/{branch}
 */
public class PathManager {

	/**
	 * 获取下载路径，附加分支文件夹
	 * @param repository 格式 "owner/repo"
	 * @param branch     分支名
	 * @return 绝对路径字符串
	 */
	public static String getDownloadPath(String repository, String branch) {
		String repoName = repository.split("/")[1];
		String userHome = System.getProperty("user.home");
		String path = userHome + File.separator
				+ "Downloads" + File.separator
				+ "GitHub-Repos" + File.separator
				+ repoName + File.separator
				+ branch;
		System.out.println("✓ 下载路径: " + path);
		return path;
	}
}