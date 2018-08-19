package com.hundsun.booklending.mapper.impl;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.mapper.BookMapper;

public class BookMapperImpl implements BookMapper {

	public Boolean saveBook(Book book) {
		return null;
	}

	public List getAllBooks() {
		return null;
	}

	public List getAllBooks(Boolean ifNew, int status) {
		return null;
	}

	public Boolean saveBookStatus(Book book) {
		return null;
	}

	public List searchBooks(String title, Boolean ifNew) {
		return null;
	}

	public Book searchBookDetails(String ISBN) {
		return null;
	}

	public List searchBookComments(String ISBN) {
		return null;
	}

	public List searchCommendBooks(String userId) {
		return null;
	}

	public Boolean likeBook(String ISBN, String userId, int status) {
		return null;
	}

	public Boolean updateBook(String bookId, int status) {
		return null;
	}

	public Boolean updateBorrow(String borrowId, int status) {
		return null;
	}

	public Boolean renew(String borrowId) {
		return null;
	}
}
