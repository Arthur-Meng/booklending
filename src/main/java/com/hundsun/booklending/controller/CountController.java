package com.hundsun.booklending.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hundsun.booklending.service.CountService;
import com.hundsun.booklending.util.OtherUtil;

import lombok.extern.log4j.Log4j;

/**
 * 统计控制类
 * 
 * @author mengjw
 *
 */
@RequestMapping("/api/count")
@Controller
@Log4j
public class CountController {

	@Autowired
	private CountService countService;

	/**
	 * 获取全部图书数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/book", method = RequestMethod.GET)
	@ResponseBody
	public int countBook() {
		return countService.countBook(null);
	}

	/**
	 * 获取全部图书统计
	 * 
	 * @return
	 */
	@RequestMapping(value = "/haveBook", method = RequestMethod.GET)
	@ResponseBody
	public int countJaveBook() {
		return countService.countBook(true);
	}

	/**
	 * 获取借阅数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/borrow", method = RequestMethod.GET)
	@ResponseBody
	public int countBorrow() {
		return countService.countBorrow(null);
	}

	/**
	 * 获取待借阅数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/waitBorrow", method = RequestMethod.GET)
	@ResponseBody
	public int countWaitBorrow() {
		return countService.countBorrow("1");
	}

	/**
	 * 获取已借阅数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/haveBorrow", method = RequestMethod.GET)
	@ResponseBody
	public int countHaveBorrow() {
		return countService.countBorrow("2");
	}

	/**
	 * 获取已还数量
	 * 
	 * @return
	 */
	@RequestMapping(value = "/hadBorrow", method = RequestMethod.GET)
	@ResponseBody
	public int countHadBorrow() {
		return countService.countBorrow("3");
	}

	/**
	 * 获取每天登陆
	 * 
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	@ResponseBody
	public String countLogin(@RequestParam String beginDate, @RequestParam String endDate) {
		List<Map> loginList = countService.countLogin(beginDate, endDate);
		Map result = new HashMap();
		List dateList = new ArrayList();
		List countList = new ArrayList();
		String should = beginDate;
		int times = 0;
		for (int i = 0; i < loginList.size();) {
			Map m = loginList.get(i);
			String today = m.get("date").toString().replaceAll("-", "");
			// 判断是不是该天是不是上一天,如果不是，则插入没有的那天，并且数量为0
			int days = OtherUtil.differentDays(OtherUtil.getDate(should), OtherUtil.getDate(today));
			if (days >= 1) {
				dateList.add(should);
				countList.add(0);
				should = OtherUtil.getDate(OtherUtil.getDate(should), 1);
				i = times;
				continue;
			} else {
				dateList.add(today);
				countList.add(m.get("count"));
				times++;
				i++;
				should = OtherUtil.getDate(OtherUtil.getDate(should), 1);
			}
		}
		if (dateList.size()>0) {
			// 如果最后还是没到endDate，需要补上
			String end = (String) dateList.get(dateList.size() - 1);
			int add = OtherUtil.differentDays(OtherUtil.getDate(end), OtherUtil.getDate(endDate));
			for (int i = 0; i < add; i++) {
				dateList.add(should);
				countList.add(0);
				should = OtherUtil.getDate(OtherUtil.getDate(should), 1);
			}
		}
		result.put("date", dateList);
		result.put("count", countList);
		return JSON.toJSONString(result);
	}
}