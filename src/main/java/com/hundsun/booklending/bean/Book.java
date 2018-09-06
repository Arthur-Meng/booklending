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
	// 借阅状态（0表示已过期，1表示待取书，2表示确认借阅（未还书），3表示已还书，4表示已经评价,8表示被动取消,9表示主动取消）
	public String status;
	// 添加时间
	public String addTime;
	// 总喜欢数
	public int likeAll;
	// 总想看数
	public int wannaall;
	// 总评论数
	public int commentall;
	// 总评分
	public int scoreall;
	// 剩余
	public int remain;
	//是否喜欢
	public int ifLike;
	//过期时间,-99表示待取书
	public int timeout;

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

	public String[] getAuthorArray() {
		if (null != this.author) {
			return this.author.split(",");
		} else {
			return null;
		}
	}

	public String[] getTranslatorArray() {
		if (null != this.translator) {
			return this.translator.split(",");
		} else {
			return null;
		}
	}
}
