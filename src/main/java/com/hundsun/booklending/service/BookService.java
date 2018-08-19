package com.hundsun.booklending.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;

import com.hundsun.booklending.bean.Book;

/**
 * 图书服务类
 * 
 * @author mengjwa
 *
 */
public interface BookService {
	/**
	 * 根据图书id获取用图书
	 * 
	 * @param userId
	 * @return
	 */
	public Book getBookByBookId(String bookId);

	/**
	 * 获取全部图书
	 * 
	 * @return
	 */
	public List getAllBooks();

	/**
	 * 获取全部新书
	 * 
	 * @return
	 */
	public List getNewBooks();

	/**
	 * 获取新增书籍
	 * 
	 * @return
	 */
	public List getAddedBooks();

	/**
	 * 保存图书
	 * 
	 * @param book
	 * @return
	 */
	public boolean saveBook(Book book);

	/**
	 * 保存图书状态
	 * 
	 * @param book
	 * @return
	 */
	public boolean saveBookStatus(Book book);

	/**
	 * 查找图书
	 * 
	 * @param title,ifNew
	 * @return
	 */
	public List searchBooks(String title, Boolean ifNew);

	/**
	 * 查看细节图书
	 * 
	 * @param ISBN
	 * @return
	 */
	public Book searchBookDetails(String ISBN);

	/**
	 * 查看图书评论
	 * 
	 * @param ISBN
	 * @return
	 */
	public List searchBookComments(String ISBN);

	/**
	 * 查看推荐的列表
	 * 
	 * @param userId
	 * @return
	 */
	public List searchCommendBooks(String userId);

	/**
	 * 点赞图书/想看图书
	 * 
	 * @param ISBN
	 * @param userId
	 * @param status
	 * @return
	 */
	public Boolean likeBook(String ISBN, String userId, int status);

	/**
	 * 更新图书状态
	 * 
	 * @param bookId
	 * @param status
	 * @return
	 */
	public Boolean updateBook(String bookId, int status);

	/**
	 * 更新借阅状态
	 * 
	 * @param borrowId
	 * @param status
	 * @return
	 * @throws DuplicateKeyException
	 */
	public Boolean updateBorrow(String borrowId, int status) throws DuplicateKeyException;

	/**
	 * 续借
	 * 
	 * @param borrowId
	 * @return
	 */
	public Boolean renew(String borrowId);

	/**
	 * 转变为图书类
	 * 
	 * @param book
	 * @return
	 */
	public Book transMapToBook(Map map);
}
