package com.hundsun.booklending.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.mapper.BookMapper;
import com.hundsun.booklending.mapper.UserMapper;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.OtherUtil;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BookMapper bookMapper;

	@Autowired
	private BookService booService;

	public User getUserByUserId(String userId) {
		User user = userMapper.getUserByUserId(userId);
		return user;
	}

	public List getAllUser() {
		return userMapper.getAllUser();
	}

	public boolean saveUser(User user) {
		if (userMapper.saveUser(user)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean updateUser(User user) {
		if (userMapper.updateUser(user)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean borrow(String userId, String bookId) {
		// 保存借阅信息且更新图书状态
		String borrowId = OtherUtil.getUUID();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateString = df.format(new Date());
		if (userMapper.borrow(borrowId, userId, bookId, dateString, 1)
				&& booService.updateBook(bookId, 2)) {
			return true;
		} else {
			return false;
		}
	}

	public Map searchBorrowDetails(String borrowId) {
		return userMapper.searchBorrowDetails(borrowId);
	}

	public List searchBorrow(String userId) {
		return userMapper.searchBorrow(userId);
	}

	public boolean deleteBorrow(String borrowId) {
		if (userMapper.deleteBorrow(borrowId)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveCommend(String userId, Book book, String reason, String date) {
		if (userMapper.saveCommend(userId, book, reason, date)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveBookComments(String ISBN, String userId, String content, String date, int score) {
		if (bookMapper.saveBookComments(ISBN, userId, content, date, score)) {
			List<Map> books = bookMapper.searchBookStatus(ISBN);
			// 计算评分
			int nums = bookMapper.searchBookComments(ISBN).size();
			double scoreall = (nums * Double.parseDouble((String) books.get(0).get("scoreall")) + score) / nums + 1;
			for (Map book : books) {
				book.put("commentall", Integer.parseInt((String) book.get("commentall")) + 1);
				book.put("scoreall", scoreall);
				bookMapper.updateBookStatus(book);
			}
			return true;
		} else {
			return false;
		}
	}

	public Boolean likeBook(String ISBN, String userId, int status, String date) {
		if (bookMapper.likeBook(ISBN, userId, status, date)) {
			List<Map> books = bookMapper.searchBookStatus(ISBN);
			for (Map book : books) {
				if (status == 1) {
					book.put("likeall", Integer.parseInt((String) book.get("likeall")) + 1);
				} else {
					book.put("wannaall", Integer.parseInt((String) book.get("wannaall")) + 1);
				}
				bookMapper.updateBookStatus(book);
			}
			return true;
		} else {
			return false;
		}
	}

}
