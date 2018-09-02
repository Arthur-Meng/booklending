package com.hundsun.booklending;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import com.github.pagehelper.PageHelper;
import com.hundsun.booklending.cache.ListCache;

import org.springframework.boot.SpringApplication;

/**
 * 启动类
 * 
 * @author mengjw
 *
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan("com.hundsun.booklending.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
}