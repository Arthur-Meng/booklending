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

	public Boolean borrow(String borrowId, String userId, String bookId, String borrowTime, int status) {
		return null;
	}

	public Map searchBorrowDetails(String borrowId) {
		return null;
	}

	public List searchBorrow(String userId) {
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

	public void updateBorrow(String borrowId, int status,String returnTime) {
	}
}