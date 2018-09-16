package com.hundsun.booklending.config;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

import com.alibaba.druid.support.http.WebStatFilter;

/**
 * druid过滤器.
 * 
 * @author mengjw
 *
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*", initParams = {
		@WebInitParam(name = "sessionStatEnable", value = "true"),
		@WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")
})
public class DruidStatFilter extends WebStatFilter {

}
