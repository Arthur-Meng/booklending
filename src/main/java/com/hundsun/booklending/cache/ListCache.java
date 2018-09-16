package com.hundsun.booklending.cache;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hundsun.booklending.bean.User;
import com.hundsun.booklending.service.BookService;
import com.hundsun.booklending.service.UserService;
import com.hundsun.booklending.util.OtherUtil;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class ListCache {
	private List<Map> wannaList_year = new ArrayList<Map>();
	private List<Map> userList_year = new ArrayList<Map>();
	private List<Map> likeList_year = new ArrayList<Map>();
	private List<Map> highScoreList_year = new ArrayList<Map>();
	private List<Map> mostCommentList_year = new ArrayList<Map>();
	private List<Map> wannaList_month = new ArrayList<Map>();
	private List<Map> userList_month = new ArrayList<Map>();
	private List<Map> likeList_month = new ArrayList<Map>();
	private List<Map> highScoreList_month = new ArrayList<Map>();
	private List<Map> mostCommentList_month = new ArrayList<Map>();
	private List<Map> wannaList_week = new ArrayList<Map>();
	private List<Map> userList_week = new ArrayList<Map>();
	private List<Map> likeList_week = new ArrayList<Map>();
	private List<Map> highScoreList_week = new ArrayList<Map>();
	private List<Map> mostCommentList_week = new ArrayList<Map>();
	private static int login_count = 0;

	private static ListCache listCache;

	public ListCache() {
	}

	public static ListCache getListCache() {
		if (null == listCache) {
			listCache = new ListCache();
		}
		return listCache;
	}

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@PostConstruct
	public void init() {
		listCache = this;
		listCache.userService = this.userService;
		listCache.bookService = this.bookService;
		initWeek();
		initMonth();
		initYear();
	}

	public void initWeek() {
		// 清空之前的缓存
		wannaList_week.clear();
		// userList_week.clear();
		likeList_week.clear();
		highScoreList_week.clear();
		mostCommentList_week.clear();
		// 重新计算排行放入新的数据
		rankWeek();
	}

	public void initMonth() {
		// 清空之前的缓存
		wannaList_month.clear();
		// userList_month.clear();
		likeList_month.clear();
		highScoreList_month.clear();
		mostCommentList_month.clear();
		// 重新计算排行放入新的数据
		rankmonth();
	}

	public void initYear() {
		// 清空之前的缓存
		wannaList_year.clear();
		// userList_year.clear();
		likeList_year.clear();
		highScoreList_year.clear();
		mostCommentList_year.clear();
		// 重新计算排行放入新的数据
		rankYear();
	}

	/**
	 * 计算年排行
	 */
	public void rankYear() {
		rankWannaList(365, "wanna_year", wannaList_year);
		rankLikeList(365, "like_year", likeList_year);
		rankCommentList(365, "comment_year", mostCommentList_year);
		rankScoreList(365, "score_year", highScoreList_year);
		rankLoginList(365, "login_year", userList_year);
	}

	/**
	 * 计算月排行
	 */
	public void rankmonth() {
		rankWannaList(30, "wanna_month", wannaList_month);
		rankLikeList(30, "like_month", likeList_month);
		rankCommentList(30, "comment_month", mostCommentList_month);
		rankScoreList(30, "score_month", highScoreList_month);
		rankLoginList(30, "login_month", userList_month);
	}

	/**
	 * 计算周排行
	 */
	public void rankWeek() {
		rankWannaList(7, "wanna_week", wannaList_week);
		rankLikeList(7, "like_week", likeList_week);
		rankCommentList(7, "comment_week", mostCommentList_month);
		rankScoreList(7, "score_week", highScoreList_week);
		rankLoginList(7, "login_week", userList_week);
	}

	public void rankLoginList(int days, String type, List<Map> list) {
		List<User> allUser = userService.getAllUser();
		Map<String, Integer> count = new LinkedHashMap<String, Integer>();
		if (days == 7) {
			for (User u : allUser) {
				count.put(u.getUserId(), u.getWeekLoginNum());
			}
		} else if (days == 30) {
			for (User u : allUser) {
				count.put(u.getUserId(), u.getMonthLoginNum());
			}
		} else if (days == 365) {
			for (User u : allUser) {
				count.put(u.getUserId(), u.getYearLoginNum());
			}
		}
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		Stream<Entry<String, Integer>> st = count.entrySet().stream();
		st.sorted(Comparator.comparing(e -> e.getValue())).forEach(e -> result.put(e.getKey(), e.getValue()));
		for (Entry<String, Integer> entry : result.entrySet()) {
			Map map = new HashMap();
			try {
				map = OtherUtil.convertBean(userService.getUserByUserId(entry.getKey()));
			} catch (Exception e) {
				log.error("转换User为Map发送异常", e);
			}
			map.put(type, entry.getValue());
			list.add(map);
		}
	}

	/**
	 * 排名分数榜单
	 */
	public void rankScoreList(int days, String type, List<Map> list) {
		// 获取全部分数信息
		List<Map> allList = bookService.searchBookComments(null);
		// 仅获取最近days天的
		List<Map> daysList = daysFilter(days, "date", allList);
		// 筛选最近days天的数据
		Map<String, Double> count = higherScore(daysList);
		// 排序
		Map<String, Double> result = doubleSorter(count);
		// 更新图书的分数榜单
		for (Entry<String, Double> entry : result.entrySet()) {
			Map map = new HashMap();
			try {
				map = OtherUtil.convertBean(bookService.searchBookDetails(entry.getKey()));
			} catch (Exception e) {
				log.error("转换Book为Map发送异常", e);
			}
			map.put(type, entry.getValue());
			list.add(map);
		}
	}

	/**
	 * 排名评论榜单
	 */
	public void rankCommentList(int days, String type, List<Map> list) {
		// 获取全部评论榜单
		List<Map> allList = bookService.searchBookComments();
		// 仅获取最近days天的
		List<Map> daysList = daysFilter(days, "date", allList);
		// 筛选最近days天的数据
		Map<String, Integer> count = counter(daysList);
		// 排序
		Map<String, Integer> result = sorter(count);
		// 更新图书的榜单状态
		for (Entry<String, Integer> entry : result.entrySet()) {
			Map map = new HashMap();
			try {
				map = OtherUtil.convertBean(bookService.searchBookDetails(entry.getKey()));
			} catch (Exception e) {
				log.error("转换Book为Map发送异常", e);
			}
			map.put(type, entry.getValue());
			list.add(map);
		}
	}

	/**
	 * 排名想看榜单
	 */
	public void rankWannaList(int days, String type, List<Map> list) {
		// 获取全部想看榜单
		List<Map> allWannaList = bookService.searchWannaBook();
		// 仅获取最近days天的
		List<Map> wannaList = daysFilter(days, "date", allWannaList);
		// 筛选最近days天的数据
		Map<String, Integer> count = counter(wannaList);
		// 排序
		Map<String, Integer> result = sorter(count);
		// 更新图书的榜单状态
		for (Entry<String, Integer> entry : result.entrySet()) {
			Map map = new HashMap();
			try {
				map = OtherUtil.convertBean(bookService.searchBookDetails(entry.getKey()));
			} catch (Exception e) {
				log.error("转换Book为Map发送异常", e);
			}
			map.put(type, entry.getValue());
			list.add(map);
		}
	}

	/**
	 * 排名点赞榜单
	 */
	public void rankLikeList(int days, String type, List<Map> list) {
		// 获取全部想看榜单
		List<Map> allLikeList = bookService.searchLikeBook();
		// 仅获取最近days天的
		List<Map> likeList = daysFilter(days, "date", allLikeList);
		// 筛选最近days天的数据
		Map<String, Integer> count = counter(likeList);
		// 排序
		Map<String, Integer> result = sorter(count);
		// 更新图书的点赞榜单
		for (Entry<String, Integer> entry : result.entrySet()) {
			Map map = new HashMap();
			try {
				map = OtherUtil.convertBean(bookService.searchBookDetails(entry.getKey()));
			} catch (Exception e) {
				log.error("转换Book为Map发送异常", e);
			}
			map.put(type, entry.getValue());
			list.add(map);
		}
	}

	/**
	 * 筛选最近days天的数据
	 * 
	 * @param days
	 * @param allList
	 * @return
	 */
	public List<Map> daysFilter(int days, String date, List<Map> allList) {
		List<Map> list = new ArrayList<Map>();
		for (Map m : allList) {
			int diff = OtherUtil.differentDays(OtherUtil.getSQLDate(String.valueOf(m.get(date))), new Date());
			if (diff <= days) {
				list.add(m);
			}
		}
		return list;
	}

	/**
	 * 遍历生成Map<ISBN,次数>
	 * 
	 * @param allList
	 * @return
	 */
	public Map<String, Integer> counter(List<Map> allList) {
		Map<String, Integer> count = new HashMap<String, Integer>();
		// 遍历生成Map<ISBN,次数>
		for (Map m : allList) {
			if (count.containsKey(m.get("ISBN"))) {
				count.put((String) m.get("ISBN"), new Integer((Integer) count.get((String) m.get("ISBN")) + 1));
			} else {
				count.put((String) m.get("ISBN"), new Integer(1));
			}
		}
		return count;
	}

	/**
	 * 遍历生成Map<ISBN,分数>
	 * 
	 * @param allList
	 * @return
	 */
	public Map<String, Double> higherScore(List<Map> allList) {
		Map<String, Double> count = new HashMap<String, Double>();
		Map<String, String> countMap = new HashMap<String, String>();
		// 遍历生成Map<ISBN,分数>
		if (null != allList) {
			for (Map m : allList) {
				if (null != m.get("score")) {
					if (countMap.containsKey((String) m.get("ISBN"))) {
						String[] numInfo = countMap.get((String) m.get("ISBN")).split("-");
						countMap.put((String) m.get("ISBN"), Integer.valueOf(Integer.parseInt(numInfo[0]) + 1) + "-"
								+ Integer.valueOf(Integer.parseInt(numInfo[1]) + (int) m.get("score")));
					} else {
						countMap.put((String) m.get("ISBN"), "1-" + m.get("score"));
					}

				}
			}
		}
		for (Entry<String, String> entry : countMap.entrySet()) {
			String[] numInfo = entry.getValue().split("-");
			count.put(entry.getKey(), new Double(Integer.parseInt(numInfo[1]) / Integer.parseInt(numInfo[0])));
		}
		return count;
	}

	/**
	 * 排序
	 * 
	 * @param count
	 * @return
	 */
	public Map<String, Integer> sorter(Map<String, Integer> count) {
		Map<String, Integer> result = new LinkedHashMap<String, Integer>();
		Stream<Entry<String, Integer>> st = count.entrySet().stream();
		Comparator comparator = Comparator.comparing(e -> ((Entry<String, Integer>) e).getValue());
		st.sorted(comparator.reversed()).forEach(
				e -> result.put(((Entry<String, Integer>) e).getKey(), ((Entry<String, Integer>) e).getValue()));
		return result;
	}

	/**
	 * 排序
	 * 
	 * @param count
	 * @return
	 */
	public Map<String, Double> doubleSorter(Map<String, Double> count) {
		Map<String, Double> result = new LinkedHashMap<String, Double>();
		Stream<Entry<String, Double>> st = count.entrySet().stream();
		Comparator comparator = Comparator.comparing(e -> ((Entry<String, Double>) e).getValue());
		st.sorted(comparator.reversed())
				.forEach(e -> result.put(((Entry<String, Double>) e).getKey(), ((Entry<String, Double>) e).getValue()));
		return result;
	}

	public List<Map> getList(int type) {
		if (type == 1) {
			return wannaList_year;
		} else if (type == 2) {
			return userList_year;
		} else if (type == 3) {
			return likeList_year;
		} else if (type == 4) {
			return highScoreList_year;
		} else if (type == 5) {
			return mostCommentList_year;
		} else if (type == 6) {
			return wannaList_month;
		} else if (type == 7) {
			return userList_month;
		} else if (type == 8) {
			return likeList_month;
		} else if (type == 9) {
			return highScoreList_month;
		} else if (type == 10) {
			return mostCommentList_month;
		} else if (type == 11) {
			return wannaList_week;
		} else if (type == 12) {
			return userList_week;
		} else if (type == 13) {
			return likeList_week;
		} else if (type == 14) {
			return highScoreList_week;
		} else if (type == 15) {
			return mostCommentList_week;
		} else {
			return null;
		}
	}

	public static void addLoginCount() {
		login_count++;
	}

	public static int getLoginCount() {
		return login_count;
	}

	public static void setLoginCount(int count) {
		login_count = count;
	}
}
