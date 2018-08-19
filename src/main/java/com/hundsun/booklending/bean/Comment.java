package com.hundsun.booklending.bean;

import lombok.Data;

/**
 * 评论 Bean类
 * 
 * @author mengjw
 *
 */
@Data
public class Comment {
	// 图书id
	public String bookId;
	// 用户id
	public String userID;
	// 评论时间
	public String time;
	// 评论内容
	public String content;
	// 评论分数
	public int score;

}
