package com.hundsun.booklending.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * 统计mapper类
 * 
 * @author mengjw
 *
 */
public interface CountMapper {
	/**
	 * 获取全部图书数量
	 * 
	 * @return
	 */
	public int countBook(@Param("ifHave") Boolean ifHave);

	/**
	 * 获取借阅数量
	 * 
	 * @return
	 */
	public int countBorrow(@Param("status") String status);

	/**
	 * 保存每天登陆
	 * 
	 * @return
	 */
	public Boolean saveLogin(@Param("date") String date, @Param("count") int count);

	/**
	 * 更新每天登陆
	 * 
	 * @return
	 */
	public Boolean updateLogin(@Param("date") String date, @Param("count") int count);

	/**
	 * 保存每天登陆
	 * 
	 * @return
	 */
	public List countLogin(@Param("beginDate") String beginDate, @Param("endDate") String endDate);

	/**
	 * 查询书籍历史记录
	 * 
	 * @return
	 */
	public List bookHistory(@Param("bookId") String bookId, @Param("beginDate") String beginDate,
			@Param("endDate") String endDate);

}
