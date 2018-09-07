package com.hundsun.booklending.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.expr.NewArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.MsgBean;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.CountService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.HttpUtil;
import com.hundsun.booklending.util.OtherUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j;

/**
 * 操作员控制类
 * 
 * @author mengjw
 *
 */
@RequestMapping("/api/admin")
@Controller
@Log4j
public class AdminController {
	@Autowired
	private BookService bookService;
	@Autowired
	private CountService countService;
	@Autowired
	private UserService userService;

	/**
	 * 获取全部书籍数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/countBook", method = RequestMethod.GET)
	@ResponseBody
	public int countBook() {
		return countService.countBook(null);
	}

	/**
	 * 根据ISBN保存图书
	 * 
	 * @param userid
	 * @param ISBN
	 * @return
	 */
	@RequestMapping(value = "/saveBook", method = RequestMethod.POST)
	@ResponseBody
	public String saveBook(@RequestBody Map info) {
		Map bookMap = HttpUtil.getBookInfo((String) info.get("ISBN"));
		Book book = bookService.transMapToBook(bookMap);
		// 查询有无类似书名但是没ISBN的信息，如果有就替换该信息
		List<Book> books = bookService.searchBooks(book.getTitle(), null);
		if (null != books) {
			for (Book b : books) {
				if (b.getTitle().equals(book.getTitle())) {
					bookService.deleteBook(b.getBookId());
					bookService.deleteBookStatus(b.getBookId());
					book.setBookId(b.getBookId());
				}
			}
		}
		if (bookService.saveBook(book)) {
			if (bookService.saveBookStatus(book)) {
				if (userService.saveCommend((String) info.get("user_id"), book, (String) info.get("reason"),
						OtherUtil.getDate())) {
					return new MsgBean(0, "保存图书成功").toReturn();
				} else {
					bookService.deleteBook(book.getBookId());
					bookService.deleteBookStatus(book.getBookId());
					return new MsgBean(1, "保存图书时添加推荐失败").toReturn();
				}
			} else {
				bookService.deleteBook(book.getBookId());
				return new MsgBean(1, "保存图书状态失败").toReturn();
			}
		} else {
			return new MsgBean(1, "保存图书失败").toReturn();
		}

	}

	/**
	 * 确认借书
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "确认借书", notes = "确认借书")
	@RequestMapping(value = "/confirmBorrow", method = RequestMethod.POST)
	@ResponseBody
	public String confirmBorrow(@RequestBody Map borrow_info) {
		try {
			if (bookService.updateBorrow((String) borrow_info.get("borrow_id"), 2)) {
				return new MsgBean(0, "确认借阅成功").toReturn();
			} else {
				return new MsgBean(1, "确认借阅失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "该图书已被确认借阅").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
		}
	}

	/**
	 * 确认还书
	 * 
	 * @param user
	 * @return
	 */
	@ApiOperation(value = "确认还书", notes = "确认还书")
	@RequestMapping(value = "/confirmReturn", method = RequestMethod.POST)
	@ResponseBody
	public String confirmReturn(@RequestBody Map borrow_info) {
		try {
			if (bookService.updateBorrow((String) borrow_info.get("borrow_id"), 3)) {
				return new MsgBean(0, "确认还书成功").toReturn();
			} else {
				return new MsgBean(1, "确认还书失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "该图书已被确认还书").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
		}
	}

	@RequestMapping(value = "/book", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletBook(@RequestParam String book_id) {
		if (bookService.deleteBook(book_id)) {
			return new MsgBean(0, "删除图书成功").toReturn();
		} else {
			return new MsgBean(1, "删除图书失败").toReturn();
		}
	}

	@RequestMapping(value = "/bookStatus", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletBookStatus(@RequestParam String book_id) {
		if (bookService.deleteBookStatus(book_id)) {
			return new MsgBean(0, "删除图书状态成功").toReturn();
		} else {
			return new MsgBean(1, "删除图书状态失败").toReturn();
		}
	}

	/**
	 * 删除借阅记录
	 * 
	 * @param borrowInfo
	 * @return
	 */
	@RequestMapping(value = "/borrow", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletBorrow(@RequestParam String borrow_id) {
		if (userService.deleteBorrow("borrow_id")) {
			return new MsgBean(0, "删除借阅成功").toReturn();
		} else {
			return new MsgBean(1, "删除借阅失败").toReturn();
		}
	}

	/**
	 * 下架图书
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/pullOffBook", method = RequestMethod.POST)
	@ResponseBody
	public String pullOffBook(@RequestBody Map info) {
		if (bookService.updateBook((String) info.get("book_id"), 9)) {
			return new MsgBean(0, "下架图书成功").toReturn();
		} else {
			return new MsgBean(1, "下架图书失败").toReturn();
		}

	}

	/**
	 * 获取全部待买图书
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/allVoidBooks", method = RequestMethod.GET)
	@ResponseBody
	public String getAllVoidBooks(@RequestParam int start, @RequestParam int limit) {
		// start是当前页数，limit为每页页数
		PageHelper.startPage(start, limit);
		List allBooks = bookService.getAllVoidBooks();
		PageInfo<Book> pageInfo = new PageInfo<Book>(allBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 根据ISBN获取全部图书
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getBooksByISBN", method = RequestMethod.GET)
	@ResponseBody
	public String getBooksByISBN(@RequestParam String ISBN, @RequestParam String name, @RequestParam String status,
			@RequestParam int start, @RequestParam int end) {
		int borrow_status = -1;
		if (!StringUtil.isEmpty(status)) {
			borrow_status = Integer.valueOf(status);
		}
		String book_isbn = null;
		if (!StringUtil.isEmpty(ISBN)) {
			book_isbn = ISBN;
		}
		String book_name = null;
		if (!StringUtil.isEmpty(name)) {
			book_name = name;
		}
		List<Book> booksList = bookService.getAddedBooks(book_isbn, book_name, borrow_status);
		// 根据ISBN分类
		Map<String, List<Book>> bookMap = new HashMap<String, List<Book>>();
		for (Book book : booksList) {
			// 对书进行查询，获取其借阅信息
			List borrowsList = countService.bookHistory(book.getBookId());
			if (borrowsList != null && borrowsList.size() > 0) {
				Map borrow = (Map) borrowsList.get(0);
				if (borrow.get("borrowstatus").equals(1)) {
					// 没取
					book.setTimeout(-100);
				} else if (borrow.get("borrowstatus").equals(2)) {
					// 没还
					book.setTimeout(-99);
				} else if (borrow.get("borrowstatus").equals(0)) {
					// 过期
					book.setTimeout(OtherUtil
							.differentDays(OtherUtil.getSQLDate(String.valueOf(borrow.get("returntime"))), new Date()));
				} else {
					// 架上
					book.setTimeout(-99);
				}

				if (bookMap.containsKey(book.getISBN())) {
					bookMap.get(book.getISBN()).add(book);
				} else {
					List<Book> list = new ArrayList<Book>();
					list.add(book);
					bookMap.put(book.getISBN(), list);
				}
			}
		}
		List result = new ArrayList();
		for (Map.Entry<String, List<Book>> entry : bookMap.entrySet()) {
			result.add(entry);
		}
		Map finalMap = new HashMap();
		for (Map.Entry<String, List<Book>> entry : (List<Map.Entry>) OtherUtil.getRightInfos(result, start, end)) {
			finalMap.put(entry.getKey(), entry.getValue());
		}
		Map resultMap = new HashMap<>();
		resultMap.put("data", finalMap);
		resultMap.put("all", result.size());
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 获取书籍历史记录
	 * 
	 * @param book_id
	 * @return
	 */
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	@ResponseBody
	public String bookHistory(@RequestParam String book_id, @RequestParam int start, @RequestParam int limit) {
		PageHelper.startPage(start, limit);
		List<Map> resultList = countService.bookHistory(book_id);
		PageInfo<Map> pageInfo = new PageInfo<Map>(resultList);
		return JSON.toJSONString(pageInfo);
	}

	/**
	 * 查询借阅
	 * 
	 * @param ISBN
	 * @param name
	 * @param status
	 * @param start
	 * @param end
	 * @return
	 */
	@RequestMapping(value = "/searchBorrow", method = RequestMethod.GET)
	@ResponseBody
	public String searchBorrow(@RequestParam String ISBN, @RequestParam String name, @RequestParam int status,
			@RequestParam int start, @RequestParam int end) {
		List<Map> borrowList = userService.searchBorrow(null, ISBN, name, status);
		Map finalMap = new HashMap();
		finalMap.put("all", borrowList.size());
		finalMap.put("data", OtherUtil.getRightInfos(borrowList, start, end));
		return JSON.toJSONString(OtherUtil.getRightInfos(borrowList, start, end));
	}
}
