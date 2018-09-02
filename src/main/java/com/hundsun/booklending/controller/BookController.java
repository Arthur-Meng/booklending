package com.hundsun.booklending.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.JsonUtil;

import lombok.extern.log4j.Log4j;

/**
 * 图书控制类
 * 
 * @author mengjw
 *
 */
@RequestMapping("/api/book")
@Controller
@Log4j
public class BookController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	/**
	 * 获取全部图书
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/allBooks", method = RequestMethod.GET)
	@ResponseBody
	public String getAllBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List allBooks = bookService.getAllBooks();
		PageInfo<Book> pageInfo = new PageInfo<Book>(allBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 获取已经添加的新书（15天内）
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/newBooks", method = RequestMethod.GET)
	@ResponseBody
	public String getNewBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List<Book> newBooks = bookService.getNewBooks();
		// 这里需要对书籍的列表进行过滤，筛选出ISBN一样的最新的书籍。
		Map<String, Book> bookMap = new LinkedHashMap<String, Book>();
		for (Book book : newBooks) {
			if (bookMap.containsKey(book.getISBN())) {
				Book b = bookMap.get(book.getISBN());
				if (Integer.valueOf(book.getAddTime()) > Integer.valueOf(b.getAddTime())) {
					book.setRemain(b.getRemain() + 1);
					bookMap.put(book.getISBN(), book);
				} else {
					b.setRemain(b.getRemain() + 1);
				}
			} else {
				Book b = book;
				b.setRemain(1);
				bookMap.put(b.getISBN(), b);
			}
		}
		// 拿出ISBN一样的Book，去Bookid
		List books = new ArrayList<Book>();
		for (Entry<String, Book> entry : bookMap.entrySet()) {
			Book book = entry.getValue();
			book.setBookId("");
			books.add(book);
		}
		PageInfo<Book> pageInfo = new PageInfo<Book>(books);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 获取已经添加的图书
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/AddedBooks", method = RequestMethod.GET)
	@ResponseBody
	public String getAddedBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List<Book> allBooks = bookService.getAddedBooks();
		// 这里需要对书籍的列表进行过滤，筛选出ISBN一样的书籍。
		Map<String, Book> bookMap = new HashMap<String, Book>();
		for (Book book : allBooks) {
			if (bookMap.containsKey(book.getISBN())) {
				Book b = bookMap.get(book.getISBN());
				b.setRemain(b.getRemain() + 1);
			} else {
				Book b = book;
				b.setRemain(1);
				bookMap.put(b.getISBN(), b);
			}
		}
		// 拿出ISBN一样的Book，去Bookid
		List books = new ArrayList<Book>();
		for (Entry<String, Book> entry : bookMap.entrySet()) {
			Book book = entry.getValue();
			book.setBookId("");
			books.add(book);
		}
		PageInfo<Book> pageInfo = new PageInfo<Book>(books);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 根据书名查找图书，返回列表
	 * 
	 * @param title
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/searchBooks", method = RequestMethod.GET)
	@ResponseBody
	public String searchBooks(@RequestParam String title, @RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List searchBooks = bookService.searchBooks(title, null);
		PageInfo<Book> pageInfo = new PageInfo<Book>(searchBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 根据ISBN查看图书信息
	 * 
	 * @param ISBN
	 * @return
	 */
	@RequestMapping(value = "/details", method = RequestMethod.GET)
	@ResponseBody
	public String searchBookDetails(@RequestParam String ISBN) {
		// start是当前页数，limit为每页页数
		Book searchBooks = bookService.searchBookDetails(ISBN);
		return JsonUtil.convertBean2Json(searchBooks);
	}

	/**
	 * 根据图书的ISBN获取该书的评论
	 * 
	 * @param ISBN
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/comment", method = RequestMethod.GET)
	@ResponseBody
	public String bookComments(@RequestParam String ISBN, @RequestParam int start, @RequestParam int limit) {
		PageHelper.startPage(start, limit);
		List<Map> bookComments = bookService.searchBookComments(ISBN);
		// 处理信息，增加用户名和头像地址
		for (Map m : bookComments) {
			User u = userService.getUserByUserId((String) m.get("userid"));
			m.put("username", u.getName());
			m.put("userhead", "/api/images/" + (String) m.get("userid") + ".jpg");
		}
		PageInfo<Map> pageInfo = new PageInfo<Map>(bookComments);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 查看用户自己推荐的列表
	 * 
	 * @param user_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/recommendList", method = RequestMethod.GET)
	@ResponseBody
	public String searchUserCommendBooks(@RequestParam String user_id, @RequestParam int start,
			@RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List searchBooks = bookService.searchUserCommendBooks(user_id);
		PageInfo<Map> pageInfo = new PageInfo<Map>(searchBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 查看别人推荐的列表
	 * 
	 * @param user_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/otherRecommendList", method = RequestMethod.GET)
	@ResponseBody
	public String searchCommnedBooks(@RequestParam String user_id, @RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List searchBooks = bookService.searchCommendBooks(user_id);
		PageInfo<Map> pageInfo = new PageInfo<Map>(searchBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}
}
