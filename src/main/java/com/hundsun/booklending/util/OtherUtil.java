package com.hundsun.booklending.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import lombok.Data;

/**
 * 其他工具类
 * 
 * @author mengjw
 *
 */
public class OtherUtil {

	/**
	 * 获取当前的天数
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new Date());
	}

	/**
	 * 造型日期
	 * 
	 * @param dateString
	 * @return
	 */
	public static Date getDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static Date getSQLDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取days天后的日期
	 * 
	 * @param days
	 * @return
	 */
	public static String getDate(Date date, int days) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(calendar.DATE, days);
		String days_after = sdf.format(calendar.getTime());
		return days_after;
	}

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
		List target = new ArrayList();
		if (null != list && list.size() >= start) {
			if (list.size() >= start + limit) {
				target = list.subList(start, start + limit);
			} else {
				target = list.subList(start, list.size());
			}
			return target;
		} else {
			return target;
		}

	}

	/**
	 * 计算当前比date1多的天数
	 * 
	 * @param date1
	 * @return
	 */
	public static int differentDays(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		int day1 = cal1.get(Calendar.DAY_OF_YEAR);
		int day2 = cal2.get(Calendar.DAY_OF_YEAR);
		int year1 = cal1.get(Calendar.YEAR);
		int year2 = cal2.get(Calendar.YEAR);
		// 同一年
		if (year1 != year2) {
			int timeDistance = 0;
			for (int i = year1; i < year2; i++) {
				if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) { // 闰年
					timeDistance += 366;
				} else {// 不是闰年
					timeDistance += 365;
				}
			}
			return timeDistance + (day2 - day1);
		} else {// 不同年
			return day2 - day1;
		}
	}

	/**
	 * Map转实体类共通方法 (Map2Bean)
	 *
	 * @param type
	 * @param map
	 * @return Object
	 * @throws Exception
	 */
	public static Object convertMap(Class type, Map map) throws Exception {
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		Object obj = type.newInstance();
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : propertyDescriptors) {
			String propertyName = descriptor.getName();
			if (map.containsKey(propertyName)) {
				Object value = map.get(propertyName);
				descriptor.getWriteMethod().invoke(obj, value);
			}
		}
		return obj;
	}

	/**
	 * 实体类转Map共通方法 (Bean2Map)
	 *
	 * @param bean
	 *            实体类
	 * @return Map
	 * @throws Exception
	 */
	public static Map convertBean(Object bean) throws Exception {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor descriptor : propertyDescriptors) {
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}

}
