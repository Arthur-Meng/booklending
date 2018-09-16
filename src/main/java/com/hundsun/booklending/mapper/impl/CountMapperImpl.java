package com.hundsun.booklending.mapper.impl;

import java.util.List;

import com.hundsun.booklending.mapper.CountMapper;

public class CountMapperImpl implements CountMapper {


	public int countBook(Boolean ifHave) {
		return 0;
	}

	public int countBorrow(String status) {
		return 0;
	}

	public Boolean saveLogin(String date, int count) {
		return null;
	}
	
	public Boolean updateLogin(String date, int count) {
		return null;
	}

	public List countLogin(String beginDate, String endDate) {
		return null;
	}

	public List bookHistory(String bookId,String beginDate, String endDate) {
		return null;
	}
	
}
