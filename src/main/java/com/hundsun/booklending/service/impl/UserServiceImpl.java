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

	public boolean borrow(String userId, String bookId, String borrowId) {
		// 保存借阅信息且更新图书状态
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateString = df.format(new Date());
		if (userMapper.borrow(borrowId, userId, bookId, dateString, 1) && booService.updateBook(bookId, 2)) {
			return true;
		} else {
			return false;
		}
	}

	public Map searchBorrowDetails(String borrowId) {
		return userMapper.searchBorrowDetails(borrowId);
	}

	public List searchBorrow(String userId, String ISBN, String name, int status) {
		return userMapper.searchBorrow(userId, ISBN, name, status);
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
			double scoreall = 0;
			if (null != books) {
				int nums = bookMapper.searchBookComments(ISBN).size();
				if (null != books.get(0).get("scoreall")) {
					scoreall = (nums * Double.parseDouble((String) books.get(0).get("scoreall")) + score) / nums + 1;
				} else {
					scoreall = new Double(score).doubleValue();
				}
			} else {
				scoreall = new Double(score).doubleValue();
			}
			for (Map book : books) {
				if (null != book.get("commentall")) {
					book.put("commentall", Integer.parseInt((String) book.get("commentall")) + 1);
				} else {
					book.put("commentall", 1);
				}
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
					if (book.get("likeall") != null) {
						book.put("likeall", (int) book.get("likeall") + 1);
					} else {
						book.put("likeall", 1);
					}

				} else {
					if (book.get("wannaall") != null) {
						book.put("wannaall", (int) book.get("wannaall") + 1);
					} else {
						book.put("wannaall", 1);
					}
				}
				bookMapper.updateBookStatus(book);
			}
			return true;
		} else {
			return false;
		}
	}

	public Boolean deleteBookLike(String ISBN, String userId, int status) {
		return bookMapper.deleteBookLike(ISBN, userId, status);
	}

	public Boolean saveLikeCommend(String bookId, String userId, String date) {
		return userMapper.saveLikeCommend(bookId, userId, date);
	}

	public Map searchLikeCommend(String bookId, String userId) {
		return userMapper.searchLikeCommend(bookId, userId);
	}

	public Boolean updateCommend(String bookId, String userId) {
		return userMapper.updateCommend(bookId, userId);
	}
}
