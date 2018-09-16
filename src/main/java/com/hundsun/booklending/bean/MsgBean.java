package com.hundsun.booklending.bean;

import com.alibaba.fastjson.JSON;
import lombok.Data;

@Data
public class MsgBean {
	// 0表示成功，1表示失败
	private int code;
	private String message;

	public MsgBean(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public String toReturn() {
		return JSON.toJSONString(this);
	}
}
