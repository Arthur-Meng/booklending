package com.hundsun.booklending.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
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
	public boolean borrow(String userId, String bookId, String borrowId) throws DuplicateKeyException;

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
	public List searchBorrow(String userId, String ISBN, String name, int status);

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
	 * @param date
	 * @return
	 */
	public Boolean likeBook(String ISBN, String userId, int status, String date) throws DuplicateKeyException;

	/**
	 * 删除点赞图书/想看图书
	 * 
	 * @param ISBN
	 * @param userId
	 * @param status
	 * @return
	 */
	public Boolean deleteBookLike(String ISBN, String userId, int status);

	/**
	 * 保存喜欢推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @param date
	 * @return
	 */
	public Boolean saveLikeCommend(String bookId, String userId, String date);

	/**
	 * 查询喜欢推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @return
	 */
	public Map searchLikeCommend(String bookId, String userId);

	/**
	 * 更新推荐
	 * 
	 * @param bookId
	 * @param userId
	 * @return
	 */
	public Boolean updateCommend(String bookId, String userId);
}
