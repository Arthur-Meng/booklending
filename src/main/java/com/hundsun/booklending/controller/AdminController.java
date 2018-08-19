package com.hundsun.booklending.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hundsun.booklending.service.BookService;

import lombok.extern.log4j.Log4j;

/**
 * 操作员控制类
 * 
 * @author mengjw
 *
 */
@RequestMapping("/api/admin")
@Controller
@Log4j
public class AdminController {
	@Autowired
	private BookService bookService;
	/**
	 * 取认借书
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/confirmBorrow", method = RequestMethod.POST)
	@ResponseBody
	public String confirmBorrow(@RequestBody Map borrowInfo) {
		try {
			if (bookService.updateBorrow((String) borrowInfo.get("borrow_id"),1)) {
				return "确认借阅成功";
			} else {
				return "确认借阅失败";
			}
		} catch (DuplicateKeyException e) {
			return "该图书已被确认借阅";
		} catch (Exception e1) {
			log.error(e1);
			return "异常错误，请检查";
		}
	}

}
