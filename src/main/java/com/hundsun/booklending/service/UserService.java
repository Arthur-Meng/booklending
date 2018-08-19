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
	 * 保存用户
	 * 
	 * @param user
	 * @return
	 */
	public boolean saveUser(User user) throws DuplicateKeyException;

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
	public boolean deletBorrow(String borrowId);

	/**
	 * 推荐
	 * 
	 * @param borrowId
	 * @return
	 */
	public boolean saveCommend(String userId, Book book, String reason);

}
