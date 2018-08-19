package com.hundsun.booklending;

import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.github.pagehelper.PageHelper;

import org.springframework.boot.SpringApplication;

/**
 * 启动类
 * 
 * @author mengjw
 *
 */
@SpringBootApplication
@MapperScan("com.hundsun.booklending.mapper")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	
}
