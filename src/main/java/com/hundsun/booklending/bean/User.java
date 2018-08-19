package com.hundsun.booklending.bean;

import lombok.Data;

/**
 * 用户 Bena类
 * 
 * @author mengjw
 *
 */
@Data
public class User {

	// 用户id，即工号
	public String userId;
	// 姓名
	public String name;
	// 性别:女性为0，男性为1
	public int sex;
	// 密码
	public String password;
	// 偏好：以,分割
	public String preference;
	// 总登陆数
	public int allLoginNum;
	// 年登陆数
	public int yearLoginNum;
	// 月登陆数
	public int mouthLoginNum;
	// 周登陆数
	public int weekLoginNum;

	/**
	 * 默认构造方法
	 */
	public User(){
		
	}
	/**
	 * User构成方法
	 * @param userId
	 * @param password
	 */
	public User(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	/**
	 * 获取性别
	 * 
	 * @return
	 */
	public String getSex() {
		if (this.sex == 0) {
			return "女";
		} else {
			return "男";
		}
	}

	/**
	 * 获取偏好数组
	 * 
	 * @return
	 */
	public String[] getPreferenceArray() {
		return this.preference.split(",");
	}

}
