package com.hundsun.booklending.mapper.impl;

import java.util.List;
import java.util.Map;

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

	public List getAllVoidBooks() {
		return null;
	}

	public List getAllBooks(int status, int nostatus, String ISBN, String name, Boolean ifNew, Boolean ifOrder,
			Boolean byTime) {
		return null;
	}

	public Book getBookById(String bookId) {
		return null;
	}

	public Boolean saveBookStatus(Book book) {
		return null;
	}

	public List searchBooks(String title, Boolean ifNew) {
		return null;
	}

	public List searchAllBookInfo(String title) {
		return null;
	}

	public List searchBookStatus(String ISBN) {
		return null;
	}

	public Boolean updateBookStatus(Map bookStatus) {
		return null;
	}

	public List searchBookDetails(String ISBN) {
		return null;
	}

	public Boolean saveBookComments(String ISBN, String userId, String content, String date, int score) {
		return null;
	}

	public List searchBookComments(String ISBN) {
		return null;
	}

	public List searchBookComments() {
		return null;
	}

	public List searchCommendBooks(String userId) {
		return null;
	}

	public List searchUserCommendBooks(String userId) {
		return null;
	}

	public Boolean likeBook(String ISBN, String userId, int status, String date) {
		return null;
	}

	public Boolean deleteBookLike(String ISBN, String userId, int status) {
		return null;
	}

	public List searchLikeBook(int status, String ISBN, String userId) {
		return null;
	}

	public Boolean updateBook(String bookId, int status) {
		return null;
	}

	public Boolean updateBorrow(String borrowId,String confirmtime,int status) {
		return null;
	}

	public Boolean renew(String returnTime, String borrowId) {
		return null;
	}

	public Boolean deleteBook(String bookId) {
		return null;
	}

	public Boolean deleteBookStatus(String bookId) {
		return null;
	}

}
