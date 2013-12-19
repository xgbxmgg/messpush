package com.android.shop.test;


import com.android.shop.database.PropertiesUtil;

public class PropertiesTest {
	public static void main(String[] arfgs) {
		PropertiesUtil pro = new PropertiesUtil("sysParam.properties");
		String str=pro.getValues("Database.Driver");
		System.out.println(str);

	}

}
