package com.hundsun.booklending.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hundsun.booklending.bean.Book;

/**
 * 图书mapper类
 * 
 * @author mengjw
 *
 */
public interface BookMapper {

	/**
	 * 添加图书
	 * 
	 * @param book
	 * @return
	 */
	public Boolean saveBook(@Param("book") Book book);

	/**
	 * 添加图书状态
	 * 
	 * @param book
	 * @return
	 */
	public Boolean saveBookStatus(@Param("book") Book book);

	/**
	 * 获取全部图书
	 * 
	 * @return
	 */
	public List getAllBooks();

	/**
	 * 获取对应状态的图书
	 * 
	 * @return
	 */
	public List getAllBooks(@Param("ifNew") Boolean ifNew, @Param("status") int status);

	/**
	 * 获取全部图书
	 * 
	 * @return
	 */
	public List searchBooks(@Param("title") String title, @Param("ifNew") Boolean ifNew);

	/**
	 * 获取图书细节
	 * 
	 * @return
	 */
	public Book searchBookDetails(@Param("ISBN") String ISBN);

	/**
	 * 查看图书评论
	 * 
	 * @return
	 */
	public List searchBookComments(@Param("ISBN") String ISBN);
	
	/**
	 * 查看推荐图书
	 * 
	 * @return
	 */
	public List searchCommendBooks(@Param("userId") String userId);

	/**
	 * 点赞图书/想要图书，状态1为点赞，2为想看
	 * 
	 * @return
	 */
	public Boolean likeBook(@Param("ISBN") String ISBN, @Param("userId") String userId, @Param("status") int status);

	/**
	 * 更新图书状态
	 * 
	 * @return
	 */
	public Boolean updateBook(@Param("bookId") String bookId, @Param("status") int status);

	/**
	 * 更新借阅状态
	 * 
	 * @return
	 */
	public Boolean updateBorrow(@Param("borrowId") String borrowId, @Param("status") int status);

	/**
	 * 续借
	 * 
	 * @return
	 */
	public Boolean renew(@Param("borrowId") String borrowId);
}
