package com.hundsun.booklending.service.impl;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.MsgBean;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.mapper.BookMapper;
import com.hundsun.booklending.mapper.UserMapper;
import com.hundsun.booklending.service.AutoService;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.EmailUtils;
import com.hundsun.booklending.util.OtherUtil;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class AutoServiceImpl implements AutoService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private BookMapper bookMapper;
	@Autowired
	private UserService userService;
	@Autowired
	private BookService bookService;

	public List getExpireInfo() {
		List expireInfo = new ArrayList();
		List<Map> allInfo = userMapper.searchAllBorrow(1);
		for (Map borrow : allInfo) {
			Date borrowDate = OtherUtil.getSQLDate(String.valueOf(borrow.get("confirmtime")));
			Date returnDate = OtherUtil.getSQLDate(String.valueOf(borrow.get("retuentime")));
			int diff = OtherUtil.differentDays(returnDate, new Date());
			if (diff >= 0) {
				expireInfo.add(borrow);
			}
		}
		return expireInfo;
	}

	public Boolean handleExpireInfo() {
		List<Map> expireInfo = getExpireInfo();
		for (Map borrow : expireInfo) {
			// 更新借阅状态
			bookService.updateBorrow((String) borrow.get("borrowid"), null, 0);
			// 更新书籍状态
			bookService.updateBook((String) borrow.get("bookid"), 3);
			User user = userService.getUserByUserId((String) borrow.get("userid"));
			if (null != user.getEmail()) {
				try {
					Book book = bookService.getBookByBookId((String) borrow.get("bookid"));
					EmailUtils.sendHtmlMail(user.getEmail(), "借阅到期提醒",
							(String) borrow.get("userid") + "，您所借阅的《" + book.getTitle() + "》今日已经到期，请尽快归还！");
				} catch (UnsupportedEncodingException e) {
					log.error("发送邮件编码错误", e);
				} catch (MessagingException e) {
					log.error("发送邮件异常", e);
				}
			} else {
				log.info("用户" + (String) borrow.get("userid") + "没有邮箱，无法发送邮件");
			}
		}
		return true;
	}

	public List getUnConfirmedInfo() {
		List unConfirmedInfo = new ArrayList();
		List<Map> allInfo = userMapper.searchAllBorrow(0);
		for (Map borrow : allInfo) {
			Date date = OtherUtil.getSQLDate(String.valueOf(borrow.get("borrowtime")));
			int diff = OtherUtil.differentDays(date, new Date());
			if (diff >= 7) {
				unConfirmedInfo.add(borrow);
			}
		}
		return unConfirmedInfo;
	}

	public Boolean handleUnConfirmedInfo() {
		List<Map> unConfirmedInfo = getExpireInfo();
		for (Map borrow : unConfirmedInfo) {
			// 更新状态
			bookService.updateBorrow((String) borrow.get("borrowid"), null, 8);
			Map borrowMap = userService.searchBorrowDetails((String) borrow.get("borrowid"));
			bookService.updateBook((String) borrowMap.get("bookid"), 1);
		}
		return true;
	}

	public Boolean handleUserLogin(String type) {
		List<User> allUser = userService.getAllUser();
		if (type.equals("week")) {
			for (User u : allUser) {
				u.setWeekLoginNum(0);
			}
		} else if (type.equals("month")) {
			for (User u : allUser) {
				u.setMonthLoginNum(0);
			}
		} else if (type.equals("year")) {
			for (User u : allUser) {
				u.setYearLoginNum(0);
			}
		}
		for (User user : allUser) {
			userService.saveUser(user);
		}
		return true;
	}

	@Override
	public Boolean handleIfNew() {
		List<Book> allBook = bookService.getAllBooks();
		for (Book b : allBook) {
			if (null != b.getAddTime()) {
				int days = OtherUtil.differentDays(OtherUtil.getSQLDate(b.getAddTime()), new Date());
				if (days >= 15) {
					Map map = new HashMap();
					map.put("bookId", b.getBookId());
					map.put("ifNew", 0);
					bookMapper.updateBookStatus(map);
				}
			}
		}
		return true;
	}

}
