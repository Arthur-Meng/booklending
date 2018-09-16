package com.hundsun.booklending.service;

import java.util.List;

/**
 * 自动服务类
 * 
 * @author mengjw
 *
 */
public interface AutoService {

	/**
	 * 从全部在借信息中获取借阅到期的信息
	 * 
	 * @return
	 */
	public List getExpireInfo();

	/**
	 * 处理借阅到期的信息
	 * 
	 * @return
	 */
	public Boolean handleExpireInfo();

	/**
	 * 从全部预定信息中获取到期的信息
	 * 
	 * @return
	 */
	public List getUnConfirmedInfo();

	/**
	 * 处理预定未借阅到期的信息
	 * 
	 * @return
	 */
	public Boolean handleUnConfirmedInfo();
	
	/**
	 * 处理用户登陆
	 * 
	 * @return
	 */
	public Boolean handleUserLogin(String type);
	
	/**
	 * 处理是否新书
	 * 
	 * @return
	 */
	public Boolean handleIfNew();

}
