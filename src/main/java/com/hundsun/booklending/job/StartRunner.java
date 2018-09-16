package com.hundsun.booklending.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.hundsun.booklending.cache.ListCache;
import com.hundsun.booklending.service.AutoService;
import com.hundsun.booklending.service.UserService;

import lombok.extern.log4j.Log4j;

@Component
@Order(value = 1)
@Log4j
public class StartRunner implements ApplicationRunner {

	@Autowired
	private AutoService autoService;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.info("项目已启动！开始初始化榜单,更新书籍信息....");
		ListCache.getListCache().init();
		autoService.handleIfNew();
	}

}
