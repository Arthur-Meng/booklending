package com.hundsun.booklending.mapper;

import java.util.List;
import java.util.Map;

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
	 * 获取全部没买的图书
	 * 
	 * @return
	 */
	public List getAllVoidBooks();

	/**
	 * 获取对应状态的图书
	 * 
	 * @return
	 */
	public List getAllBooks(@Param("status") int status, @Param("nostatus") int nostatus, @Param("ISBN") String ISBN,
			@Param("name") String name, @Param("ifNew") Boolean ifNew, @Param("ifOrder") Boolean ifOrder,
			@Param("byTime") Boolean byTime);

	/**
	 * 获取对应bookId的图书
	 * 
	 * @return
	 */
	public Book getBookById(@Param("bookId") String bookId);

	/**
	 * 获取全部图书
	 * 
	 * @return
	 */
	public List searchBooks(@Param("title") String title, @Param("ifNew") Boolean ifNew);

	/**
	 * 获取全部图书信息
	 * 
	 * @param title
	 * @return
	 */
	public List searchAllBookInfo(@Param("title") String title);

	/**
	 * 通过ISBN获取图书状态
	 * 
	 * @return
	 */
	public List searchBookStatus(@Param("ISBN") String ISBN);

	/**
	 * 更新图书状态
	 * 
	 * @param likeall
	 * @param wannaall
	 * @param commentall
	 * @param scoreall
	 * @param ifNew
	 * @return
	 */
	public Boolean updateBookStatus(@Param("bookStatus") Map bookStatus);

	/**
	 * 获取图书细节
	 * 
	 * @return
	 */
	public List searchBookDetails(@Param("ISBN") String ISBN);

	/**
	 * 保存用户评论
	 * 
	 * @param ISBN
	 * @param userId
	 * @param content
	 * @param date
	 * @param score
	 * @return
	 */
	public Boolean saveBookComments(@Param("ISBN") String ISBN, @Param("userId") String userId,
			@Param("content") String content, @Param("date") String date, @Param("score") int score);

	/**
	 * 查看图书评论
	 * 
	 * @return
	 */
	public List searchBookComments(@Param("ISBN") String ISBN);

	/**
	 * 查看全部图书评论
	 * 
	 * @return
	 */
	public List searchBookComments();

	/**
	 * 查看别人推荐图书
	 * 
	 * @return
	 */
	public List searchCommendBooks(@Param("userId") String userId);

	/**
	 * 查看自己推荐图书
	 * 
	 * @return
	 */
	public List searchUserCommendBooks(@Param("userId") String userId);

	/**
	 * 点赞图书/想要图书，状态1为点赞，2为想看
	 * 
	 * @return
	 */
	public Boolean likeBook(@Param("ISBN") String ISBN, @Param("userId") String userId, @Param("status") int status,
			@Param("date") String date);

	/**
	 * 删除点赞图书/想要图书，状态1为点赞，2为想看
	 * 
	 * @return
	 */
	public Boolean deleteBookLike(@Param("ISBN") String ISBN, @Param("userId") String userId,
			@Param("status") int status);

	/**
	 * 查询用户点赞图书/想要图书，状态1为点赞，2为想看
	 * 
	 * @return
	 */
	public List searchLikeBook(@Param("status") int status, @Param("ISBN") String ISBN, @Param("userId") String userId);

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
	public Boolean updateBorrow(@Param("borrowId") String borrowId,@Param("confirmtime") String confirmtime, @Param("status") int status);

	/**
	 * 续借
	 * 
	 * @return
	 */
	public Boolean renew(@Param("returnTime") String returnTime, @Param("borrowId") String borrowId);

	/**
	 * 删除图书
	 * 
	 * @param bookId
	 * @return
	 */
	public Boolean deleteBook(@Param("bookId") String bookId);

	/**
	 * 删除图书
	 * 
	 * @param borrowId
	 * @return
	 */
	public Boolean deleteBookStatus(@Param("bookId") String bookId);
}
