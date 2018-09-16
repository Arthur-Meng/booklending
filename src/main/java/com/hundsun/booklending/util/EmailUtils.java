package com.hundsun.booklending.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class EmailUtils {
	// private static final String HOST = "smtp.163.com";
	private static final String HOST = "smtp.qq.com";
	// private static final Integer PORT = 25;
	private static final Integer PORT = 587;
	// private static final String USERNAME = "17816876766@163.com";
	private static final String USERNAME = "903019946@qq.com";
	// private static final String PASSWORD ="mjw612mjw";
	private static final String PASSWORD = "";
	// private static final String EMAILFORM = "17816876766@163.com";
	private static final String EMAILFORM = "903019946@qq.com";
	private static JavaMailSenderImpl mailSender = createMailSender();

	/**
	 * 邮件发送器
	 *
	 * @return 配置好的工具
	 */
	private static JavaMailSenderImpl createMailSender() {
		JavaMailSenderImpl sender = new JavaMailSenderImpl();
		sender.setHost(HOST);
		sender.setPort(PORT);
		sender.setUsername(USERNAME);
		sender.setPassword(PASSWORD);
		sender.setDefaultEncoding("Utf-8");
		Properties p = new Properties();
		p.setProperty("mail.smtp.timeout", "25000");
		p.setProperty("mail.smtp.auth", "false");
		sender.setJavaMailProperties(p);
		return sender;
	}

	/**
	 * 发送邮件
	 *
	 * @param to
	 *            接受人
	 * @param subject
	 *            主题
	 * @param html
	 *            发送内容
	 * @throws MessagingException
	 *             异常
	 * @throws UnsupportedEncodingException
	 *             异常
	 */
	public static void sendHtmlMail(String to, String subject, String html)
			throws MessagingException, UnsupportedEncodingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		// 设置utf-8或GBK编码，否则邮件会有乱码
		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
		messageHelper.setFrom(EMAILFORM, "恒生图书借阅系统");
		messageHelper.setTo(to);
		messageHelper.setSubject(subject);
		messageHelper.setText(html, true);
		mailSender.send(mimeMessage);
	}
}
