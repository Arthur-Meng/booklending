package com.hundsun.booklending.config;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName: WebAppConfig
 * @Description: 图片路径配置类
 * @author mengjw
 */
@Configuration
public class WebKImageConfig extends WebMvcConfigurerAdapter {
	// 获取配置文件中图片的路径
	@Value("${booklending.imagesPath}")
	private String mImagesPath;

	// 访问图片方法
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		if (mImagesPath.equals("") || mImagesPath.equals("${booklending.imagesPath}")) {
			String imagesPath = WebKImageConfig.class.getClassLoader().getResource("").getPath();
			if (imagesPath.indexOf(".jar") > 0) {
				imagesPath = imagesPath.substring(0, imagesPath.indexOf(".jar"));
			} else if (imagesPath.indexOf("classes") > 0) {
				imagesPath = "file:" + imagesPath.substring(0, imagesPath.indexOf("classes"));
			}
			imagesPath = imagesPath.substring(0, imagesPath.lastIndexOf("/")) + "/images/";
			mImagesPath = imagesPath;
		}
		LoggerFactory.getLogger(WebKImageConfig.class).info("imagesPath=" + mImagesPath);
		registry.addResourceHandler("/api/images/**").addResourceLocations(mImagesPath);
		super.addResourceHandlers(registry);
	}
}