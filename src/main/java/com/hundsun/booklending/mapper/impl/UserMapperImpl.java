package com.hundsun.booklending.mapper.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.mapper.UserMapper;

public class UserMapperImpl implements UserMapper {

	public User getUserByUserId(String userId) {
		return null;
	}

	public List getAllUser() {
		return null;
	}

	public Boolean saveUser(User user) {
		return null;
	}

	public Boolean updateUser(User user) {
		return null;
	}

	public Boolean borrow(String borrowId, String userId, String bookId, String borrowTime, String returnTime,
			int status) {
		return null;
	}

	public Map searchBorrowDetails(String borrowId) {
		return null;
	}

	public List searchBorrow(String userId, String ISBN, String name, int status,Boolean cancelstatus) {
		return null;
	}

	public Boolean deleteBorrow(String borrowId) {
		return null;
	}

	public Boolean saveCommend(String userId, Book book, String reason, String date) {
		return null;
	}

	public List searchAllBorrow(int status) {
		return null;
	}

	public Boolean updateBorrow(String borrowId, int status, String returnTime) {
		return null;
	}

	public Boolean saveLikeCommend(String bookId, String userId, String date) {
		return null;
	}

	public Map searchLikeCommend(String bookId, String userId) {
		return null;
	}

	public Boolean updateCommend(String bookId, String userId) {
		return null;
	}

}