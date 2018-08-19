package com.hundsun.booklending.util;

import java.util.List;
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
