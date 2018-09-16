package com.hundsun.booklending.job;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hundsun.booklending.cache.ListCache;
import com.hundsun.booklending.service.AutoService;
import com.hundsun.booklending.service.CountService;
import com.hundsun.booklending.util.OtherUtil;

import lombok.extern.log4j.Log4j;

@Component
@Configurable
@EnableScheduling
@Log4j
public class ScheduledTasks {
	private static Boolean ifStart = false;
	@Autowired
	private AutoService autoService;

	@Autowired
	private CountService countService;

	// 每6小时执行一次，计算周榜单
	@Scheduled(cron = "0 0 */6 * * * ")
	public void resetDayList() {
		ListCache.getListCache().initWeek();
		log.info("当前时间为" + dateFormat().format(new Date()) + ",日榜单已重新排名并缓存");
	}

	// 每6小时执行一次，保存每日登陆
	@Scheduled(cron = "0 0 */6 * * * ")
	public void saveLoingCount() {
		countService.updateLogin(OtherUtil.getDate(), ListCache.getLoginCount());
		log.info("当前时间为" + dateFormat().format(new Date()) + ",日登陆已保存，为" + ListCache.getLoginCount());
	}

	// 每天早上0点0分统一重置每日登陆
	@Scheduled(cron = "0 0 0 ? * *")
	public void resetLoingCount() {
		ListCache.setLoginCount(0);
		countService.saveLogin(OtherUtil.getDate(), ListCache.getLoginCount());
		log.info("当前时间为" + dateFormat().format(new Date()) + ",日登陆已重置为0！");
	}

	// 每12小时执行一次，计算月榜单
	@Scheduled(cron = "0 0 */12 * * * ")
	public void resetMouthList() {
		ListCache.getListCache().initMonth();
		log.info("当前时间为" + dateFormat().format(new Date()) + ",周榜单已重新排名并缓存");
	}

	// 每月的第一个9点开始每隔3天，计算年榜单
	@Scheduled(cron = "0 0 9 1/3 * ?")
	public void resetYearList() {
		ListCache.getListCache().initYear();
		log.info("当前时间为" + dateFormat().format(new Date()) + ",年榜单已重新排名并缓存");
	}

	// 每天早上8点30分统一处理借阅到期信息并发邮件通知
	@Scheduled(cron = "0 30 8 ? * *")
	public void handleExpire() {
		autoService.handleExpireInfo();
		log.info("当前时间为" + dateFormat().format(new Date()) + ",已经处理借阅到期信息并发邮件通知！");
	}

	// 每天早上8点统一处理预定未借阅
	@Scheduled(cron = "0 0 8 ? * *")
	public void handleUnConfirmed() {
		autoService.handleUnConfirmedInfo();
		log.info("当前时间为" + dateFormat().format(new Date()) + ",已成功处理预定未借阅！");
	}

	// 每周清空用户周登陆,每周一9点30
	@Scheduled(cron = "0 30 9 ? * MON")
	public void handleWeekUserLogin() {
		autoService.handleUserLogin("week");
		log.info("当前时间为" + dateFormat().format(new Date()) + ",已成功清空用户周登陆！");
	}

	// 每月清空用户月登陆,每月1号上午10点触发
	@Scheduled(cron = "0 0 10 1	 * ? ")
	public void handleMouthUserLogin() {
		autoService.handleUserLogin("mouth");
		log.info("当前时间为" + dateFormat().format(new Date()) + ",已成功清空用户月登陆！");
	}

	// 每年清空用户年登陆,每年的1月1号 11点分触发
	@Scheduled(cron = "0 0 11 1 1 ? ")
	public void handleYearUserLogin() {
		autoService.handleUserLogin("year");
		log.info("当前时间为" + dateFormat().format(new Date()) + ",已成功清空用户年登陆！");
	}

	private SimpleDateFormat dateFormat() {
		return new SimpleDateFormat("HH:mm:ss");
	}

}
