import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单的JSON解析器，避免依赖第三方库
 */
public class SimpleJsonParser {

	public static Map<String, Object> parseObject(String json) {
		Map<String, Object> result = new HashMap<>();

		// 移除外层大括号
		json = json.trim();
		if (json.startsWith("{") && json.endsWith("}")) {
			json = json.substring(1, json.length() - 1);
		}

		// 简单的字段解析
		Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*([^,}]+)");
		Matcher matcher = pattern.matcher(json);

		while (matcher.find()) {
			String key = matcher.group(1);
			String value = matcher.group(2).trim();

			// 移除引号
			if (value.startsWith("\"") && value.endsWith("\"")) {
				value = value.substring(1, value.length() - 1);
			}

			result.put(key, value);
		}

		return result;
	}

	public static List<Map<String, Object>> parseArray(String json) {
		List<Map<String, Object>> result = new ArrayList<>();

		// 移除外层方括号
		json = json.trim();
		if (json.startsWith("[") && json.endsWith("]")) {
			json = json.substring(1, json.length() - 1);
		}

		// 简单分割对象（这里简化处理）
		String[] objects = json.split("\\},\\s*\\{");

		for (String obj : objects) {
			if (!obj.trim().isEmpty()) {
				// 修复分割后的大括号
				if (!obj.startsWith("{")) obj = "{" + obj;
				if (!obj.endsWith("}")) obj = obj + "}";

				result.add(parseObject(obj));
			}
		}

		return result;
	}
}