import java.util.*;

/**
 * 改进的JSON解析器，修复了原版本的多个问题
 * 修复日期: 2025-06-04
 */
public class SimpleJsonParser {

	/**
	 * 解析JSON对象
	 */
	public static Map<String, Object> parseObject(String json) {
		Map<String, Object> result = new HashMap<>();

		if (json == null || json.trim().isEmpty()) {
			return result;
		}

		// 移除外层大括号和空白
		json = json.trim();
		if (json.startsWith("{") && json.endsWith("}")) {
			json = json.substring(1, json.length() - 1).trim();
		}

		if (json.isEmpty()) {
			return result;
		}

		// 改进的键值对解析
		List<String> pairs = splitKeyValuePairs(json);

		for (String pair : pairs) {
			String[] kv = splitKeyValue(pair);
			if (kv.length == 2) {
				String key = kv[0].trim();
				String value = kv[1].trim();

				// 移除键的引号
				if (key.startsWith("\"") && key.endsWith("\"")) {
					key = key.substring(1, key.length() - 1);
				}

				// 解析值
				Object parsedValue = parseValue(value);
				result.put(key, parsedValue);
			}
		}

		return result;
	}

	/**
	 * 解析JSON数组 - 主要修复点
	 */
	public static List<Map<String, Object>> parseArray(String json) {
		List<Map<String, Object>> result = new ArrayList<>();

		if (json == null || json.trim().isEmpty()) {
			return result;
		}

		// 移除外层方括号
		json = json.trim();
		if (!json.startsWith("[") || !json.endsWith("]")) {
			System.err.println("JSON数组格式错误: " + json.substring(0, Math.min(100, json.length())));
			return result;
		}

		json = json.substring(1, json.length() - 1).trim();
		if (json.isEmpty()) {
			return result;
		}

		// 🔧 核心修复：正确分割JSON对象数组
		List<String> objects = splitJsonObjects(json);

		for (String obj : objects) {
			if (!obj.trim().isEmpty()) {
				try {
					Map<String, Object> parsed = parseObject(obj);
					if (!parsed.isEmpty()) {
						result.add(parsed);
					}
				} catch (Exception e) {
					System.err.println("解析JSON对象失败: " + obj.substring(0, Math.min(50, obj.length())) + "...");
					System.err.println("错误: " + e.getMessage());
				}
			}
		}

		return result;
	}

	/**
	 * 🚀 新增：正确分割JSON对象数组的方法
	 */
	private static List<String> splitJsonObjects(String json) {
		List<String> objects = new ArrayList<>();
		int braceCount = 0;
		int start = 0;
		boolean inString = false;
		boolean escapeNext = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (escapeNext) {
				escapeNext = false;
				continue;
			}

			if (c == '\\') {
				escapeNext = true;
				continue;
			}

			if (c == '"') {
				inString = !inString;
			} else if (!inString) {
				if (c == '{') {
					braceCount++;
				} else if (c == '}') {
					braceCount--;

					// 当大括号平衡且遇到逗号时，分割对象
					if (braceCount == 0) {
						String obj = json.substring(start, i + 1).trim();
						if (!obj.isEmpty()) {
							objects.add(obj);
						}

						// 寻找下一个对象的开始
						while (i + 1 < json.length() &&
								(json.charAt(i + 1) == ',' || Character.isWhitespace(json.charAt(i + 1)))) {
							i++;
						}
						start = i + 1;
					}
				}
			}
		}

		// 处理最后一个对象（如果没有以逗号结尾）
		if (start < json.length()) {
			String obj = json.substring(start).trim();
			if (!obj.isEmpty()) {
				objects.add(obj);
			}
		}

		return objects;
	}

	/**
	 * 🚀 新增：分割键值对
	 */
	private static List<String> splitKeyValuePairs(String json) {
		List<String> pairs = new ArrayList<>();
		int braceCount = 0;
		int bracketCount = 0;
		int start = 0;
		boolean inString = false;
		boolean escapeNext = false;

		for (int i = 0; i < json.length(); i++) {
			char c = json.charAt(i);

			if (escapeNext) {
				escapeNext = false;
				continue;
			}

			if (c == '\\') {
				escapeNext = true;
				continue;
			}

			if (c == '"') {
				inString = !inString;
			} else if (!inString) {
				if (c == '{') {
					braceCount++;
				} else if (c == '}') {
					braceCount--;
				} else if (c == '[') {
					bracketCount++;
				} else if (c == ']') {
					bracketCount--;
				} else if (c == ',' && braceCount == 0 && bracketCount == 0) {
					String pair = json.substring(start, i).trim();
					if (!pair.isEmpty()) {
						pairs.add(pair);
					}
					start = i + 1;
				}
			}
		}

		// 添加最后一个键值对
		if (start < json.length()) {
			String pair = json.substring(start).trim();
			if (!pair.isEmpty()) {
				pairs.add(pair);
			}
		}

		return pairs;
	}

	/**
	 * 🚀 新增：分割单个键值对
	 */
	private static String[] splitKeyValue(String pair) {
		int colonIndex = -1;
		boolean inString = false;
		boolean escapeNext = false;

		// 找到第一个不在字符串内的冒号
		for (int i = 0; i < pair.length(); i++) {
			char c = pair.charAt(i);

			if (escapeNext) {
				escapeNext = false;
				continue;
			}

			if (c == '\\') {
				escapeNext = true;
				continue;
			}

			if (c == '"') {
				inString = !inString;
			} else if (!inString && c == ':') {
				colonIndex = i;
				break;
			}
		}

		if (colonIndex == -1) {
			return new String[]{pair}; // 没有找到冒号
		}

		String key = pair.substring(0, colonIndex).trim();
		String value = pair.substring(colonIndex + 1).trim();
		return new String[]{key, value};
	}

	/**
	 * 🚀 新增：解析值（支持字符串、数字、布尔、null）
	 */
	private static Object parseValue(String value) {
		if (value == null || value.trim().isEmpty()) {
			return "";
		}

		value = value.trim();

		// null值
		if ("null".equals(value)) {
			return null;
		}

		// 布尔值
		if ("true".equals(value)) {
			return true;
		}
		if ("false".equals(value)) {
			return false;
		}

		// 字符串值（移除引号）
		if (value.startsWith("\"") && value.endsWith("\"") && value.length() >= 2) {
			return value.substring(1, value.length() - 1);
		}

		// 数字值
		try {
			if (value.contains(".")) {
				return Double.parseDouble(value);
			} else {
				return Long.parseLong(value);
			}
		} catch (NumberFormatException e) {
			// 如果不是数字，返回原字符串
			return value;
		}
	}

	/**
	 * 🚀 新增：调试方法，帮助排查JSON解析问题
	 */
	public static void debugParseArray(String json) {
		System.out.println("=== 调试JSON解析 ===");
		System.out.println("输入长度: " + json.length());
		System.out.println("前100字符: " + json.substring(0, Math.min(100, json.length())));

		List<Map<String, Object>> result = parseArray(json);
		System.out.println("解析结果数量: " + result.size());

		for (int i = 0; i < Math.min(3, result.size()); i++) {
			System.out.println("项目 " + i + ": " + result.get(i));
		}
		System.out.println("================");
	}
}