package main.interfaces;

import main.model.user.User;
import java.time.LocalDate;

/**
 * 可借阅接口
 * 定义了所有可借阅资源必须实现的方法
 */
public interface Borrowable {
    
    /**
     * 借出资源
     * @param user 借阅用户
     * @param borrowDate 借阅日期
     * @return 是否借阅成功
     */
    boolean borrow(User user, LocalDate borrowDate);
    
    /**
     * 归还资源
     * @param user 归还用户
     * @param returnDate 归还日期
     * @return 是否归还成功
     */
    boolean returnResource(User user, LocalDate returnDate);
    
    /**
     * 续借资源
     * @param user 续借用户
     * @param extendDays 续借天数
     * @return 是否续借成功
     */
    boolean extend(User user, int extendDays);
    
    /**
     * 检查是否可借阅
     * @param user 用户
     * @return 是否可借阅
     */
    boolean isAvailableFor(User user);
    
    /**
     * 获取当前借阅用户
     * @return 当前借阅用户，如果未被借阅则返回null
     */
    User getCurrentBorrower();
    
    /**
     * 获取到期日期
     * @return 到期日期，如果未被借阅则返回null
     */
    LocalDate getDueDate();
    
    /**
     * 检查是否已逾期
     * @return 是否已逾期
     */
    boolean isOverdue();
}
