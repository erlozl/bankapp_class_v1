package com.tenco.bankapp.utils;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import org.springframework.context.support.StaticApplicationContext;

public class CommonUtil {
 // 상태값을 가지는 변수를 사용하면 안된다.
	public static String timestampToString(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(time);
	}
	
	public static String unitFormat(long money) {
		DecimalFormat df = new DecimalFormat("###,###");
		return df.format(money);
	}
}
