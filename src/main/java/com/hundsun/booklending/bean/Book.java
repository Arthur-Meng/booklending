package com.hundsun.booklending.bean;

import lombok.Data;

/**
 * Book Bean类
 * 
 * @author mengjw
 *
 */
@Data
public class Book {

	// 图书id
	public String bookId;
	// 图书ISBN，有10位和13位两种
	public String ISBN;
	// 书名
	public String title;
	// 作者
	public String author;
	// 出版日期
	public String pubdate;
	// 标签，以,分割
	public String tags;
	// 图片地址
	public String image;
	// 装订类型：平装为1，精装为2
	public String binding;
	// 译者
	public String translator;
	// 出版社
	public String publisher;
	// 作者简介
	public String author_intro;
	// 图书简介
	public String summary;
	// 价格
	public String price;
	// 电子书价格
	public String ebook_price;
	// 图书状态（0表示已推荐，1表示可借阅状态，2表示不可借阅状态,9表示已经过期）
	// 借阅状态（0表示已预定，1表示确认借阅（待借），2表示已借阅状态,3表示需要归还状态，4表示已经归还状态,9表示取消）
	public String status;
	// 添加时间
	public String addTime;
	// 是否新书
	public Boolean ifNew;
	// 喜欢的总数
	public int likeAll;
	// 一年内的喜欢数
	public int likeYear;
	// 一月内的喜欢数
	public int likeMouth;
	// 一周内的喜欢数
	public int likeWeek;

	/**
	 * 获取标签数组
	 * 
	 * @return
	 */
	public String[] getTagsArray() {
		if (null != this.tags) {
			return this.tags.split(",");
		} else {
			return null;
		}
	}
}
