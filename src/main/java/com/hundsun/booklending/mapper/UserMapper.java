package com.hundsun.booklending.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;

/**
 * 用户mapper类
 * 
 * @author mengjw
 *
 */
public interface UserMapper {

	/**
	 * 根据用户id获取用户
	 * 
	 * @param userId
	 * 
	 * @return
	 */
	public User getUserByUserId(@Param("userId") String userId);

	/**
	 * 获取全部用户
	 * 
	 * @return
	 */
	public List getAllUser();

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	public Boolean saveUser(@Param("user") User user);

	/**
	 * 更新用户
	 * 
	 */
	public Boolean updateUser(@Param("user") User user);

	/**
	 * 用户借书
	 * 
	 * @param user
	 * @return
	 */
	public Boolean borrow(@Param("borrowId") String borrowId, @Param("userId") String userId,
			@Param("bookId") String bookId, @Param("borrowTime") String borrowTime, @Param("status") int status);

	/**
	 * 查看用户借书
	 * 
	 * @param user
	 * @return
	 */
	public Map searchBorrowDetails(@Param("borrowId") String borrowId);

	/**
	 * 查询用户借书
	 * 
	 * @param userId
	 * @return
	 */
	public List searchBorrow(@Param("userId") String userId, @Param("ISBN") String ISBN, @Param("name") String name,
			@Param("status") int status);

	/**
	 * 删除借书记录
	 * 
	 * @param borrowId
	 * @return
	 */
	public Boolean deleteBorrow(@Param("borrowId") String borrowId);

	/**
	 * 推荐
	 * 
	 * @param userId
	 * @param book
	 * @param reason
	 * @param date
	 * @return
	 */
	public Boolean saveCommend(@Param("userId") String userId, @Param("book") Book book, @Param("reason") String reason,
			@Param("date") String date);

	/**
	 * 获取全部在借信息
	 * 
	 * @param
	 * @return
	 */
	public List searchAllBorrow(@Param("status") int status);

	/**
	 * 更新借阅信息
	 * 
	 * @param borrowId
	 * @param status
	 */
	public Boolean updateBorrow(@Param("borrowId") String borrowId, @Param("status") int status,
			@Param("returnTime") String returnTime);

	/**
	 * 保存喜欢推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @param date
	 * @return
	 */
	public Boolean saveLikeCommend(@Param("bookId") String bookId, @Param("userId") String userId,
			@Param("date") String date);

	/**
	 * 查询喜欢推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @return
	 */
	public Map searchLikeCommend(@Param("bookId") String bookId, @Param("userId") String userId);

	/**
	 * 更新推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @return
	 */
	public Boolean updateCommend(@Param("bookId") String bookId, @Param("userId") String userId);
}
