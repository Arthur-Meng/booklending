package com.hundsun.booklending.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;

/**
 * 用户服务类
 * 
 * @author mengjw
 *
 */
public interface UserService {
	/**
	 * 根据用户id获取用户
	 * 
	 * @param userId
	 * @return
	 */
	public User getUserByUserId(String userId);

	/**
	 * 获取全部用户
	 * 
	 * @return
	 */
	public List getAllUser();

	/**
	 * 保存用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean saveUser(User user) throws DuplicateKeyException;

	/**
	 * 更新用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUser(User user);

	/**
	 * 借阅
	 * 
	 * @param user_id,bookId
	 * @return
	 */
	public boolean borrow(String userId, String bookId) throws DuplicateKeyException;

	/**
	 * 查看借阅
	 * 
	 * @param borrowId
	 * @return
	 */
	public Map searchBorrowDetails(String borrowId);

	/**
	 * 查看借阅
	 * 
	 * @param user_id
	 * @return
	 */
	public List searchBorrow(String userId);

	/**
	 * 删除借阅
	 * 
	 * @param borrowId
	 * @return
	 */
	public boolean deleteBorrow(String borrowId);

	/**
	 * 推荐
	 * 
	 * @param userId
	 * @param book
	 * @param reason
	 * @param date
	 * @return
	 */
	public boolean saveCommend(String userId, Book book, String reason, String date) throws DuplicateKeyException;

	/**
	 * 评论
	 * 
	 * @param ISBN
	 * @param userId
	 * @param content
	 * @param date
	 * @param score
	 * @return
	 */
	public boolean saveBookComments(String ISBN, String userId, String content, String date, int score);

	/**
	 * 点赞图书/想看图书
	 * 
	 * @param ISBN
	 * @param userId
	 * @param status
	 * @return
	 */
	public Boolean likeBook(String ISBN, String userId, int status, String date);
}
