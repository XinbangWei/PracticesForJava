package main.interfaces;

import java.util.List;

/**
 * 可搜索接口
 * 定义了搜索功能的标准方法
 */
public interface Searchable {
    
    /**
     * 根据关键词搜索
     * @param keyword 搜索关键词
     * @return 搜索结果列表
     */
    List<Object> searchByKeyword(String keyword);
    
    /**
     * 根据标题搜索
     * @param title 标题
     * @return 匹配的结果列表
     */
    List<Object> searchByTitle(String title);
    
    /**
     * 根据作者搜索
     * @param author 作者
     * @return 匹配的结果列表
     */
    List<Object> searchByAuthor(String author);
    
    /**
     * 根据分类搜索
     * @param category 分类
     * @return 匹配的结果列表
     */
    List<Object> searchByCategory(String category);
    
    /**
     * 高级搜索
     * @param title 标题（可为null）
     * @param author 作者（可为null）
     * @param category 分类（可为null）
     * @param keyword 关键词（可为null）
     * @return 匹配的结果列表
     */
    List<Object> advancedSearch(String title, String author, String category, String keyword);
}
