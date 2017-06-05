package com.autoUpdate;

import java.util.Date;
import java.util.Calendar;

import java.text.SimpleDateFormat;

public class DateSearch {
	static int hour;
	static int minute;
	static int second;

	public DateSearch() {
		init();
	}

	private static void init() {
		Date now = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");// 可以方便地修改日期格式
		String hehe = dateFormat.format(now);
		System.out.println(hehe);

		Calendar c = Calendar.getInstance();// 可以对每个时间域单独修改
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int date = c.get(Calendar.DATE);
		hour = c.get(Calendar.HOUR_OF_DAY);
		minute = c.get(Calendar.MINUTE);
		second = c.get(Calendar.SECOND);
		// System.out.println(year + "/" + month + "/" + date + " " + hour + ":"
		// + minute + ":" + second);
	}
	
}