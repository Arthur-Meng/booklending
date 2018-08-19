package com.hundsun.booklending.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.mapper.UserMapper;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.OtherUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;

	@Autowired
	private BookService booService;

	public User getUserByUserId(String userId) {
		User user = userMapper.getUserByUserId(userId);
		return user;
	}

	public boolean saveUser(User user) {
		if (userMapper.saveUser(user)) {
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
		if (userMapper.borrow(borrowId, userId, bookId, dateString, 0) && booService.updateBook(bookId, 2)) {
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

	public boolean deletBorrow(String borrowId) {
		if (userMapper.deletBorrow(borrowId)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveCommend(String userId, Book book, String reason) {
		if (userMapper.saveCommend(userId, book, reason)) {
			return true;
		} else {
			return false;
		}
	}
}
