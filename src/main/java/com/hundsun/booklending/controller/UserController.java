
package com.hundsun.booklending.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hundsun.booklending.bean.Book;
import com.hundsun.booklending.bean.MsgBean;
import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.cache.ListCache;
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

	@Value("${booklending.imagesPath}")
	private String webUploadPath;

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
	public String login(@RequestBody Map info) {
		User user = userService.getUserByUserId((String) info.get("user_id"));
		String pw = (String) info.get("pw");
		if (StringUtils.isNotBlank(pw)) {
			if (null == user) {
				return new MsgBean(1, "没有该用户").toReturn();
			}
			if (pw.equals(user.getPassword())) {
				// user.setAllLoginNum(user.getAllLoginNum() + 1);
				// user.setWeekLoginNum(user.getWeekLoginNum() + 1);
				// user.setMonthLoginNum(user.getMonthLoginNum() + 1);
				// user.setYearLoginNum(user.getYearLoginNum() + 1);
				// userService.saveUser(user);
				ListCache.addLoginCount();
				return new MsgBean(0, "登陆成功").toReturn();
			} else {
				return new MsgBean(1, "密码错误，请重试").toReturn();
			}
		} else {
			return new MsgBean(1, "请输入密码").toReturn();
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
				return new MsgBean(0, "保存成功").toReturn();
			} else {
				return new MsgBean(1, "保存失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "已经注册，请登陆").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
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
	public String borrow(@RequestBody Map borrow_info) {
		String borrowId = OtherUtil.getUUID();
		try {
			if (userService.borrow((String) borrow_info.get("user_id"), (String) borrow_info.get("book_id"),
					borrowId)) {
				return new MsgBean(0, borrowId).toReturn();
			} else {
				return new MsgBean(1, "借阅失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "该图书已被借阅").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
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
			Date start = df.parse(String.valueOf(borrowDetails.get("borrowtime")));
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
	@RequestMapping(value = "/cancelBorrow", method = RequestMethod.POST)
	@ResponseBody
	public String cancelBorrow(@RequestBody Map borrow_info) {
		try {
			if (bookService.updateBorrow((String) borrow_info.get("borrow_id"), 9)) {
				return new MsgBean(0, "取消借阅成功").toReturn();
			} else {
				return new MsgBean(1, "取消借阅失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "该图书已被取消借阅").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
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
	public String renew(@RequestBody Map borrow_info) {
		try {
			Map borrowMap = userService.searchBorrowDetails((String) borrow_info.get("borrow_id"));
			int days = OtherUtil.differentDays(OtherUtil.getSQLDate(String.valueOf(borrowMap.get("confirmtime"))),
					OtherUtil.getSQLDate(String.valueOf(borrowMap.get("returntime"))));
			if (days > 30) {
				return new MsgBean(1, "同学，你的借阅超过2次啦～<br/>借阅次数≧2，将不能再续借啦！").toReturn();
			}
			String returnTime = OtherUtil.getDate(OtherUtil.getSQLDate((String) borrowMap.get("confirmtime")), 45);
			if (bookService.renew(returnTime, (String) borrow_info.get("borrow_id"))) {
				return new MsgBean(0, "续借成功～<br/>还书日期推迟为：" + returnTime + "<br/>请及时归还书籍，逾期不还，将会扣除你的经验值哦～").toReturn();
			} else {
				return new MsgBean(1, "续借失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "该图书已被确认续借").toReturn();
		} catch (Exception e1) {
			log.error(e1);
			return new MsgBean(1, "异常错误，请检查").toReturn();
		}
	}

	/**
	 * 查询用户借书
	 * 
	 * @param user_id
	 * @return
	 */
	@RequestMapping(value = "/searchBorrow", method = RequestMethod.GET)
	@ResponseBody
	public String searchBorrow(@RequestParam String user_id, @RequestParam int start, @RequestParam int limit) {
		PageHelper.startPage(start, limit);
		List allBooks = userService.searchBorrow(user_id, null, null, -1);
		PageInfo<Map> pageInfo = new PageInfo<Map>(allBooks);
		String bookinfos = JSON.toJSONString(pageInfo);
		return bookinfos;
	}

	/**
	 * 查询是否可以根据书名推荐图书
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping(value = "/ifCanIntroduction", method = RequestMethod.GET)
	@ResponseBody
	public String ifCanIntroduction(@RequestParam String name) {
		List<Map> books = bookService.searchAllBookInfo(name);
		if (null != books && books.size() > 0) {
			List<Map> sameBooks = new ArrayList<Map>();
			List<Map> sameCommendBooks = new ArrayList<Map>();
			List<Map> otherBooks = new ArrayList<Map>();
			List<Map> otherCommendBooks = new ArrayList<Map>();
			Map result = new HashMap();
			for (Map book : books) {
				if (book.get("title").equals(name)) {
					result.put("search", 1);
					if (!book.get("status").equals(0)) {
						// 非推荐，已上架
						sameBooks.add(book);
					} else {
						if (book.get("userid") != null) {
							User user = userService.getUserByUserId((String) book.get("userid"));
							book.put("user_name", user.getName());
						}
						sameCommendBooks.add(book);
					}
				} else {
					if (!book.get("status").equals(0)) {
						// 非推荐，已上架
						otherBooks.add(book);
					} else {
						if (book.get("userid") != null) {
							User user = userService.getUserByUserId((String) book.get("userid"));
							book.put("user_name", user.getName());
						}
						otherCommendBooks.add(book);
					}
				}
			}
			if (result.get("search") != null) {
				result.put("list", sameBooks);
				result.put("commend_list", sameCommendBooks);
			} else {
				result.put("search", 0);
				result.put("list", otherBooks);
				result.put("commend_list", otherCommendBooks);
			}
			String bookinfos = JSON.toJSONString(result);
			return bookinfos;
		} else {
			return new MsgBean(1, "暂无相似").toReturn();
		}
	}

	/**
	 * 推荐图书
	 * 
	 * @param info
	 * @return
	 */
	@RequestMapping(value = "/wannaIntroduction", method = RequestMethod.POST)
	@ResponseBody
	public String recommendBook(@RequestBody Map info) {
		Book newBook = new Book();
		newBook.setBookId(OtherUtil.getUUID());
		newBook.setTitle((String) info.get("name"));
		newBook.setStatus("0");
		if (bookService.saveBook(newBook)) {
			if (bookService.saveBookStatus(newBook)) {
				if (userService.saveCommend((String) info.get("user_id"), newBook, (String) info.get("reason"),
						OtherUtil.getDate())) {
					return new MsgBean(0, "推荐成功").toReturn();
				} else {
					bookService.deleteBookStatus(newBook.getBookId());
					bookService.deleteBook(newBook.getBookId());
					return new MsgBean(1, "推荐失败").toReturn();
				}
			} else {
				bookService.deleteBook(newBook.getBookId());
				return new MsgBean(1, "保存图书状态失败").toReturn();
			}
		} else {
			return new MsgBean(1, "保存图书失败").toReturn();
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
		try {
			if (userService.likeBook((String) likeInfo.get("ISBN"), (String) likeInfo.get("user_id"), 1,
					OtherUtil.getDate())) {
				return new MsgBean(0, "点赞成功").toReturn();
			} else {
				return new MsgBean(1, "点赞失败").toReturn();
			}
		} catch (DuplicateKeyException e) {
			return new MsgBean(1, "已经点赞了").toReturn();
		} catch (Exception e) {
			return new MsgBean(1, "异常错误").toReturn();
		}

	}

	/**
	 * 根据ISBN给图书取消点赞
	 * 
	 * @param user_id
	 * @param ISBN
	 * @return
	 */
	@RequestMapping(value = "/like", method = RequestMethod.DELETE)
	@ResponseBody
	public String likeBook(@RequestParam String user_id, @RequestParam String ISBN) {
		if (userService.deleteBookLike(ISBN, user_id, 1)) {
			return new MsgBean(0, "取消点赞成功").toReturn();
		} else {
			return new MsgBean(1, "取消点赞失败").toReturn();
		}
	}

	/**
	 * 根据ISBN标记想看的图书
	 * 
	 * @param wannaInfo
	 * @return
	 */
	@RequestMapping(value = "/wannabook", method = RequestMethod.POST)
	@ResponseBody
	public String wannaBook(@RequestBody Map wanna_info) {
		if (userService.likeBook((String) wanna_info.get("ISBN"), (String) wanna_info.get("user_id"), 2,
				OtherUtil.getDate())) {
			return new MsgBean(0, "成功添加想看").toReturn();
		} else {
			return new MsgBean(1, "添加失败").toReturn();
		}
	}

	/**
	 * 添加评论
	 * 
	 * @param comment
	 * @return
	 */
	@RequestMapping(value = "/saveComment", method = RequestMethod.POST)
	@ResponseBody
	public String saveComment(@RequestBody Map comment) {
		// 先升级借阅信息，改为已评价（4）
		if (bookService.updateBorrow((String) comment.get("borrow_id"), 4)) {
			if (userService.saveBookComments((String) comment.get("ISBN"), (String) comment.get("user_id"),
					(String) comment.get("content"), OtherUtil.getDate(), (int) comment.get("score"))) {
				return new MsgBean(0, "成功添加评论").toReturn();
			} else {
				return new MsgBean(1, "添加失败").toReturn();
			}
		} else {
			return new MsgBean(1, "更新借阅状态为已评价失败").toReturn();
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
	@RequestMapping(value = "/getVerification", method = RequestMethod.GET)
	@ResponseBody
	public String getVerification(@RequestParam String user_id) {
		String verification = OtherUtil.getRandomCode(8);
		User user = userService.getUserByUserId(user_id);
		// 发送邮件
		if (null != user.getEmail()) {
			try {
				EmailUtils.sendHtmlMail(user.getEmail(), "验证码", user_id + "，您的验证码是:" + verification + "。");
			} catch (UnsupportedEncodingException e) {
				log.error("发送邮件编码错误", e);
			} catch (MessagingException e) {
				log.error("发送邮件异常", e);
			}
			return new MsgBean(0, verification).toReturn();
		} else {
			return new MsgBean(1, "发送邮件失败，请确保有邮箱信息后再试").toReturn();
		}
	}

	/**
	 * 查询榜单
	 * 
	 * @param type
	 * @param start
	 * @param limit
	 * @return
	 */
	@RequestMapping(value = "/getList", method = RequestMethod.GET)
	@ResponseBody
	public String getList(@RequestParam int type, @RequestParam int start, @RequestParam int limit) {
		List<Map> result = ListCache.getListCache().getList(type);
		// List<Map> result =
		// OtherUtil.getRightInfos(ListCache.getListCache().getList(type),
		// start, limit);
		String infos = JSON.toJSONString(result);
		return infos;
	}

	/**
	 * 喜欢别人推荐
	 * 
	 * @param wannaInfo
	 * @return
	 */
	@RequestMapping(value = "/likeCommend", method = RequestMethod.POST)
	@ResponseBody
	public String likeCommend(@RequestBody Map info) {
		Map ifLike = userService.searchLikeCommend((String) info.get("book_id"), (String) info.get("user_id"));
		if (ifLike == null) {
			if (userService.saveLikeCommend((String) info.get("book_id"), (String) info.get("user_id"),
					OtherUtil.getDate())) {
				if (userService.updateCommend((String) info.get("book_id"), (String) info.get("user_id"))) {
					return new MsgBean(0, "喜欢推荐成功").toReturn();
				} else {
					return new MsgBean(1, "更新推荐失败").toReturn();
				}
			} else {
				return new MsgBean(1, "喜欢推荐失败").toReturn();
			}
		} else {
			return new MsgBean(1, "您已经喜欢该推荐").toReturn();
		}
	}

	/**
	 * 基于用户标识的头像上传
	 * 
	 * @param file
	 *            图片
	 * @param userId
	 *            用户标识
	 * @return
	 */
	@RequestMapping(value = "/fileUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public String fileUpload(@RequestParam("file") MultipartFile file, @RequestParam("user_id") Integer user_id) {
		String result;
		if (!file.isEmpty()) {
			if (file.getContentType().contains("image")) {
				try {
					// String temp = "images" + File.separator + "upload" +
					// File.separator;
					// 获取图片的文件名
					// String fileName = file.getOriginalFilename();
					// 获取图片的扩展名
					// String extensionName =
					// StringUtils.substringAfter(fileName, ".");
					// 新的图片文件名 = 获取时间戳+"."图片扩展名
					String newFileName = user_id + ".jpg";
					// 文件路径
					String filePath = webUploadPath;

					File dest = new File(filePath, newFileName);
					if (!dest.getParentFile().exists()) {
						dest.getParentFile().mkdirs();
					}
					// 上传到指定目录
					file.transferTo(dest);

					// 将图片流转换进行BASE64加码
					// BASE64Encoder encoder = new BASE64Encoder();
					// String data = encoder.encode(file.getBytes());

					return new MsgBean(0, "上传成功!").toReturn();
				} catch (IOException e) {
					result = "上传失败!";
				}
			} else {
				result = "上传的文件不是图片类型，请重新上传!";
			}
			return new MsgBean(1, result).toReturn();
		} else {
			return new MsgBean(1, "上传失败，请选择要上传的图片!").toReturn();
		}
	}
}
