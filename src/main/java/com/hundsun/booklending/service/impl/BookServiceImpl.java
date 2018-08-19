package com.hundsun.booklending.service.impl;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.controller.BookController;
import com.hundsun.booklending.mapper.BookMapper;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.util.OtherUtil;
import com.mysql.jdbc.log.Log;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class BookServiceImpl implements BookService {
	@Autowired
	private BookMapper bookMapper;

	public boolean saveBook(Book book) {
		if (bookMapper.saveBook(book)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean saveBookStatus(Book book) {
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateString = df.format(new Date());
		book.setAddTime(dateString);
		if (bookMapper.saveBookStatus(book)) {
			return true;
		} else {
			return false;
		}
	}

	public Book getBookByBookId(String bookId) {
		return null;
	}

	public List getAllBooks() {
		return bookMapper.getAllBooks();
	}

	public List getNewBooks() {
		return bookMapper.getAllBooks(true, 0);
	}

	public List getAddedBooks() {
		return bookMapper.getAllBooks(null, 0);
	}

	public List searchBooks(String title, Boolean ifNew) {
		return bookMapper.searchBooks(title, ifNew);
	}

	public Book searchBookDetails(String ISBN) {
		return bookMapper.searchBookDetails(ISBN);
	}

	public List searchBookComments(String ISBN) {
		return bookMapper.searchBookComments(ISBN);
	}
	
	public List searchCommendBooks(String userId){
		return bookMapper.searchCommendBooks(userId);
	}

	public Boolean likeBook(String ISBN, String userId, int status) {
		if (bookMapper.likeBook(ISBN, userId, status)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean updateBook(String bookId, int status) {
		if (bookMapper.updateBook(bookId, status)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean updateBorrow(String borrowId, int status) {
		if (bookMapper.updateBorrow(borrowId, status)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean renew(String borrowId) {
		if (bookMapper.renew(borrowId)) {
			return true;
		} else {
			return false;
		}
	}

	public Book transMapToBook(Map map) {
		Book book = new Book();
		book.setBookId(OtherUtil.getUUID());
		if (StringUtils.isNotBlank((String) map.get("isbn13"))) {
			book.setISBN((String) map.get("isbn13"));
		}
		book.setTitle((String) map.get("title"));
		book.setStatus("0");
		try {
			// String authors = map.get("author").toString()
			// .replace("[", "").replace("]", "").replaceAll("\"", "");
			book.setAuthor(map.get("author").toString());
			book.setPubdate((String) map.get("pubdate"));
			List<Map<String, Object>> tagsList = (List<Map<String, Object>>) map.get("tags");
			List<String> tagsName = new ArrayList<String>();
			for (Map tag : tagsList) {
				tagsName.add((String) tag.get("name"));
			}
			book.setTags(String.join(",", tagsName));
			book.setImage((String) map.get("image"));
			book.setBinding((String) map.get("binding"));
			// String trans = map.get("translator").toString()
			// .replace("[", "").replace("]", "").replaceAll("\"", "");
			book.setTranslator(map.get("translator").toString());
			book.setPublisher((String) map.get("publisher"));
			book.setAuthor_intro((String) map.get("author_intro"));
			book.setSummary((String) map.get("summary"));
			book.setPrice((String) map.get("price"));
			book.setEbook_price((String) map.get("ebook_price"));
		} catch (Exception e) {
			log.error(e);
		}
		return book;

	}

}
