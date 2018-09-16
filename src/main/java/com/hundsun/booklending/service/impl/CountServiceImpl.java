package com.hundsun.booklending.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hundsun.booklending.mapper.CountMapper;
import com.hundsun.booklending.service.CountService;
import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class  CountServiceImpl implements CountService {
	@Autowired
	private CountMapper countMapper;

	public int countBook(Boolean ifHave) {
		return countMapper.countBook(ifHave);
	}

	public int countBorrow(String status) {
		return countMapper.countBorrow(status);
	}

	public Boolean saveLogin(String date, int count) {
		return countMapper.saveLogin(date, count);
	}
	
	public Boolean updateLogin(String date, int count) {
		return countMapper.updateLogin(date, count);
	}

	public List countLogin(String beginDate, String endDate) {
		return countMapper.countLogin(beginDate, endDate);
	}


	public List bookHistory(String bookId,String beginDate, String endDate) {
		return countMapper.bookHistory(bookId,beginDate,endDate);
	}

}
