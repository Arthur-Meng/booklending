package com.hundsun.booklending.controller;

import java.util.List;
import java.util.Map;

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
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.MsgBean;
import com.hundsun.booklending.service.BookService;
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
	private UserService userService;

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
		// 查询有无类型书名但是没ISBN的信息，如果有就替换该信息
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
	public String confirmBorrow(@RequestBody Map borrowInfo) {
		try {
			if (bookService.updateBorrow((String) borrowInfo.get("borrow_id"), 2)) {
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
	public String confirmReturn(@RequestParam String borrow_id) {
		try {
			if (bookService.updateBorrow("borrow_id", 3)) {
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
	public String deletBook(@RequestParam String bookId) {
		if (bookService.deleteBook(bookId)) {
			return new MsgBean(0, "删除图书成功").toReturn();
		} else {
			return new MsgBean(1, "删除图书失败").toReturn();
		}
	}

	@RequestMapping(value = "/bookStatus", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletBookStatus(@RequestParam String bookId) {
		if (bookService.deleteBookStatus(bookId)) {
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
	public String pullOffBook(@RequestParam String book_id) {
		if (bookService.updateBook(book_id, 9)) {
			return new MsgBean(0, "下架图书成功").toReturn();
		} else {
			return new MsgBean(1, "下架图书失败").toReturn();
		}

	}

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
}
