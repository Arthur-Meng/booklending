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

import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.mapper.BookMapper;
import com.hundsun.booklending.mapper.UserMapper;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.util.OtherUtil;
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

	public List searchBookStatus(String ISBN) {
		return bookMapper.searchBookStatus(ISBN);
	}

	public Book getBookByBookId(String bookId) {
		return bookMapper.getBookById(bookId);
	}

	public List getAllBooks() {
		return bookMapper.getAllBooks();
	}

	public List getAllVoidBooks() {
		return bookMapper.getAllVoidBooks();
	}

	public List getNewBooks() {
		return bookMapper.getAllBooks(-1, 0, null, null, true, null, true);
	}

	public List getAddedBooks() {
		return bookMapper.getAllBooks(-1, 0, null, null, null, true, null);
	}

	public List getAddedBooks(String ISBN, String name, int status) {
		return bookMapper.getAllBooks(status, 0, ISBN, name, null, true, null);
	}

	public List searchLikeBook() {
		return bookMapper.searchLikeBook(1, null, null);
	}

	public List searchLikeBook(String ISBN, String userId) {
		return bookMapper.searchLikeBook(1, ISBN, userId);
	}

	public List searchWannaBook() {
		return bookMapper.searchLikeBook(2, null, null);
	}

	public List searchBooks(String title, Boolean ifNew) {
		return bookMapper.searchBooks(title, ifNew);
	}

	public List searchAllBookInfo(String title) {
		return bookMapper.searchAllBookInfo(title);
	}

	public Book searchBookDetails(String ISBN) {
		List<Book> books = (List<Book>) bookMapper.searchBookDetails(ISBN);
		Book returnBook = new Book();
		int remain = 0;
		for (Book book : books) {
			if (null != book.getStatus() && book.getStatus().equals("1")) {
				returnBook = book;
				remain++;
			}
		}
		if (null == returnBook.getBookId()) {
			return books.get(0);
		} else {
			returnBook.setRemain(remain);
			return returnBook;
		}

	}

	public List searchBookComments(String ISBN) {
		return bookMapper.searchBookComments(ISBN);
	}

	public List searchBookComments() {
		return bookMapper.searchBookComments();
	}

	public List searchCommendBooks(String userId) {
		return bookMapper.searchCommendBooks(userId);
	}

	public List searchUserCommendBooks(String userId) {
		return bookMapper.searchUserCommendBooks(userId);
	}

	public Boolean updateBook(String bookId, int status) {
		if (bookMapper.updateBook(bookId, status)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean updateBorrow(String borrowId, String confirmtime, int status) {
		if (bookMapper.updateBorrow(borrowId, confirmtime, status)) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean renew(String returnTime, String borrowId) {
		if (bookMapper.renew(returnTime, borrowId)) {
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
			String authors = map.get("author").toString().replace("[", "").replace("]", "").replaceAll("\"", "");
			book.setAuthor(authors);
			book.setPubdate((String) map.get("pubdate"));
			List<Map<String, Object>> tagsList = (List<Map<String, Object>>) map.get("tags");
			List<String> tagsName = new ArrayList<String>();
			for (Map tag : tagsList) {
				tagsName.add((String) tag.get("name"));
			}
			book.setTags(String.join(",", tagsName));
			book.setImage((String) map.get("image"));
			book.setBinding((String) map.get("binding"));
			String trans = map.get("translator").toString().replace("[", "").replace("]", "").replaceAll("\"", "");
			book.setTranslator(trans);
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

	@Override
	public Boolean deleteBook(String bookId) {
		return bookMapper.deleteBook(bookId);
	}

	@Override
	public Boolean deleteBookStatus(String borrowId) {
		return bookMapper.deleteBookStatus(borrowId);
	}
}
