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
	public List searchBorrow(@Param("userId") String userId);

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
	 * @param userId,book,reason
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
	public void updateBorrow(@Param("borrowId") String borrowId, @Param("status") int status,
			@Param("returnTime") String returnTime);
}
