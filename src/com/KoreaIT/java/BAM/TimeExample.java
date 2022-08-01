package com.KoreaIT.java.BAM;

import java.text.SimpleDateFormat;
import java.util.Date;



	class TimeExample {
		public static void main(String[] args) {

			SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date now = new Date();

			String nowTime1 = sdf1.format(now);

			System.out.println(nowTime1);
		}
	} 


