package com.hundsun.booklending.controller;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.EmailUtils;
import com.hundsun.booklending.util.HttpUtil;
import com.hundsun.booklending.util.JsonUtil;
import com.hundsun.booklending.util.OtherUtil;

import lombok.extern.log4j.Log4j;

/**
 * 用户控制类
 * 
 * @author mengjw
 *
 */
@RequestMapping("/api/user")
@Controller
@Log4j
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	/**
	 * 用户登陆
	 * 
	 * @param userid
	 * @param pw
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(@RequestParam String userid, @RequestParam String pw) {
		User user = userService.getUserByUserId(userid);
		if (StringUtils.isNotBlank(pw)) {
			if (null == user) {
				return "没有该用户";
			}
			if (pw.equals(user.getPassword())) {
				return "登陆成功";
			} else {
				return "密码错误，请重试";
			}
		} else {
			return "请输入密码";
		}
	}

	/**
	 * 用户注册
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestBody User user) {
		try {
			if (userService.saveUser(user)) {
				return "保存成功";
			} else {
				return "保存失败";
			}
		} catch (DuplicateKeyException e) {
			return "已经注册，请登陆";
		} catch (Exception e1) {
			log.error(e1);
			return "异常错误，请检查";
		}

	}

	/**
	 * 用户借书
	 * 
	 * @param borrowInfo
	 * @return
	 */
	@RequestMapping(value = "/borrow", method = RequestMethod.POST)
	@ResponseBody
	public String borrow(@RequestBody Map borrowInfo) {
		try {
			if (userService.borrow((String) borrowInfo.get("user_id"), (String) borrowInfo.get("book_id"))) {
				return "借阅成功";
			} else {
				return "借阅失败";
			}
		} catch (DuplicateKeyException e) {
			return "该图书已被借阅";
		} catch (Exception e1) {
			log.error(e1);
			return "异常错误，请检查";
		}
	}

	/**
	 * 根据borrow_id查看借阅信息
	 * 
	 * @param borrow_id
	 * @return
	 */
	@RequestMapping(value = "/borrowDetails", method = RequestMethod.GET)
	@ResponseBody
	public String borrowDetails(@RequestParam String borrow_id) {
		Map borrowDetails = userService.searchBorrowDetails(borrow_id);
		borrowDetails.put("start_time", borrowDetails.get("borrowtime"));
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		try {
			Date start = df.parse((String) borrowDetails.get("borrowtime"));
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			int borrowDays = 30;
			if (null != borrowDetails.get("renew")) {
				borrowDays += 15;
			}
			cal.add(Calendar.DAY_OF_MONTH, borrowDays);
			Date end = cal.getTime();
			borrowDetails.put("end_time", df.format(end));
		} catch (ParseException e) {
			log.error(e);
		}

		return JsonUtil.convertBean2Json(borrowDetails);
	}

	/**
	 * 取消借书
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/confirmBorrow", method = RequestMethod.POST)
	@ResponseBody
	public String cancelBorrow(@RequestBody Map borrowInfo) {
		try {
			if (bookService.updateBorrow((String) borrowInfo.get("borrow_id"), 9)) {
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

	/**
	 * 删除借阅记录
	 * 
	 * @param borrowInfo
	 * @return
	 */
	@RequestMapping(value = "/borrow", method = RequestMethod.DELETE)
	@ResponseBody
	public String deletBorrow(@RequestBody Map borrowInfo) {
		if (userService.deletBorrow((String) borrowInfo.get("borrow_id"))) {
			return "删除借阅成功";
		} else {
			return "删除借阅失败";
		}
	}

	/**
	 * 续借
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/renew", method = RequestMethod.POST)
	@ResponseBody
	public String renew(@RequestBody Map borrowInfo) {
		try {
			if (bookService.renew((String) borrowInfo.get("borrow_id"))) {
				return "续借成功";
			} else {
				return "续借失败";
			}
		} catch (DuplicateKeyException e) {
			return "该图书已被确认续借";
		} catch (Exception e1) {
			log.error(e1);
			return "异常错误，请检查";
		}
	}

	/**
	 * 查询用户借书
	 * 
	 * @param borrowInfo
	 * @return
	 */
	@RequestMapping(value = "/searchBorrow", method = RequestMethod.GET)
	@ResponseBody
	public String searchBorrow(@RequestParam String user_id, @RequestParam int start, @RequestParam int limit) {
		PageHelper.startPage(start, limit);
		List allBooks = userService.searchBorrow(user_id);
		PageInfo<Book> pageInfo = new PageInfo<Book>(allBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 根据ISBN保存图书
	 * 
	 * @param userid
	 * @param ISBN
	 * @return
	 */
	@RequestMapping(value = "/wannaIntroduction", method = RequestMethod.POST)
	@ResponseBody
	public String saveBook(@RequestBody Map commendInfo) {
		Map bookMap = HttpUtil.getBookInfo((String) commendInfo.get("ISBN"));
		Book book = bookService.transMapToBook(bookMap);
		if (bookService.saveBook(book) && bookService.saveBookStatus(book)) {
			if (userService.saveCommend((String) commendInfo.get("referee_id"), book,
					(String) commendInfo.get("reason"))) {
				return "保存成功";
			} else {
				return "保存失败";
			}
		} else {
			return "保存失败";
		}
	}

	/**
	 * 根据ISBN给图书点赞
	 * 
	 * @param likeInfo
	 * @return
	 */
	@RequestMapping(value = "/like", method = RequestMethod.POST)
	@ResponseBody
	public String likeBook(@RequestBody Map likeInfo) {
		if (bookService.likeBook((String) likeInfo.get("ISBN"), (String) likeInfo.get("user_id"), 1)) {
			return "点赞成功";
		} else {
			return "点赞失败";
		}

	}

	/**
	 * 根据ISBN标记想看的图书
	 * 
	 * @param wannaInfo
	 * @return
	 */
	@RequestMapping(value = "/wannaBook", method = RequestMethod.POST)
	@ResponseBody
	public String wannaBook(@RequestBody Map wannaInfo) {
		if (bookService.likeBook((String) wannaInfo.get("ISBN"), (String) wannaInfo.get("user_id"), 2)) {
			return "成功添加想看";
		} else {
			return "添加失败";
		}
	}

	/**
	 * 获取验证码
	 * 
	 * @param user_id
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/getverification", method = RequestMethod.GET)
	@ResponseBody
	public String getVerification(@RequestParam String user_id) {
		String verification = OtherUtil.getRandomCode(8);
		User user = userService.getUserByUserId(user_id);
		// 发生邮件
		if (null != user.getEmail()) {
			try {
				EmailUtils.sendHtmlMail(user.getEmail(), "验证码", user_id + "，您的验证码是:" + verification + "。");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MessagingException e) {
				log.error(e);
			}
			return verification;
		} else {
			return "请保存邮箱信息后再试";
		}
	}

}
