package com.hundsun.booklending.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 统计服务类
 * 
 * @author mengjw
 *
 */
public interface CountService {
	/**
	 * 获取全部图书数量
	 * 
	 * @return
	 */
	public int countBook(Boolean ifHave);
	
	/**
	 * 获取借阅数量
	 * 
	 * @return
	 */
	public int countBorrow(String status);
	
	/**
	 * 保存每天登陆
	 * 
	 * @return
	 */
	public Boolean saveLogin(String date,int count);
	
	/**
	 * 更新每天登陆
	 * 
	 * @return
	 */
	public Boolean updateLogin(String date,int count);
	
	/**
	 * 查询每天登陆
	 * 
	 * @return
	 */
	public List countLogin(String beginDate,String endDate);
	
	/**
	 * 查询书籍历史记录
	 * 
	 * @return
	 */
	public List bookHistory(String bookId,String beginDate,String endDate);
}
