package com.hundsun.booklending.util;

import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 其他工具类
 * 
 * @author mengjw
 *
 */
public class OtherUtil {

	/**
	 * 生产uuid
	 * 
	 * @return
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
		return uuid;

	}

	/**
	 * 传入位数生成对应位数的随机字符串
	 * 
	 * @param number
	 * @return
	 */
	public static String getRandomCode(int number) {
		String codeNum = "";
		int[] code = new int[3];
		Random random = new Random();
		for (int i = 0; i < number; i++) {
			int num = random.nextInt(10) + 48;
			int uppercase = random.nextInt(26) + 65;
			int lowercase = random.nextInt(26) + 97;
			code[0] = num;
			code[1] = uppercase;
			code[2] = lowercase;
			codeNum += (char) code[random.nextInt(3)];
		}
		return codeNum;
	}

	/**
	 * 截取List中的一部分
	 * 
	 * @param list
	 * @param start
	 * @param limit
	 * @return
	 */
	public static List getRightInfos(List list, int start, int limit) {
		if (null != list && list.size() >= start) {
			List target;
			if (list.size() >= start + limit) {
				target = list.subList(start, start + limit);
			} else {
				target = list.subList(start, list.size());
			}
			return target;
		} else {
			return list;
		}

	}

}
