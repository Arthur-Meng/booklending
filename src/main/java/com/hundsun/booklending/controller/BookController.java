package com.hundsun.booklending.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.Comment;
import com.hundsun.booklending.bean.PageBean;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.util.HttpUtil;
import com.hundsun.booklending.util.JsonUtil;
import com.hundsun.booklending.util.OtherUtil;

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
	private BookService bookService;

	/**
	 * 获取全部图书
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping("/allBooks")
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
	@RequestMapping("/newBooks")
	@ResponseBody
	public String getNewBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List newBooks = bookService.getNewBooks();
		PageInfo<Book> pageInfo = new PageInfo<Book>(newBooks);
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
	@RequestMapping("/AddedBooks")
	@ResponseBody
	public String getAddedBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List newBooks = bookService.getAddedBooks();
		PageInfo<Book> pageInfo = new PageInfo<Book>(newBooks);
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
	@RequestMapping("/searchBooks")
	@ResponseBody
	public String searchBooks(@RequestParam String title, @RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List searchBooks = bookService.searchBooks(title, false);
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
	@RequestMapping("/details")
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
	@RequestMapping("/comment")
	@ResponseBody
	public String bookComments(@RequestParam String ISBN, @RequestParam int start, @RequestParam int limit) {
		PageHelper.startPage(start, limit);
		List bookComments = bookService.searchBookComments(ISBN);
		PageInfo<Comment> pageInfo = new PageInfo<Comment>(bookComments);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}
}
