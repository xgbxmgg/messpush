package com.android.shop.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	// mysql数据库连接驱动 //--
	// private String driver = "com.mysql.jdbc.Driver";
	// mysql数据库连接url //--
	// private String url = "jdbc:mysql://127.0.0.1:3306/interface";
	// private String user = "root";
	// private String password = "bbs8704";
	// PropertiesUtil pro = new PropertiesUtil("sysParam.properties");

	// sqlserver 数据库连接驱动 private String 220.172.33.53
	// private String driver = pro.getValues("Database.Driver");
	// private String url = pro.getValues("Database.Url");
	// private String userName = pro.getValues("Database.UserName");
	// private String password = pro.getValues("Database.Password");

	// oracle数据库连接驱动
	private String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
	private String url = "jdbc:sqlserver://localhost:1433;DatabaseName=infomax";
	private String user = "sa";
	private String password = "www.gz163.cnyfz";
	private Connection conn = null;

	public DatabaseConnection() {
		try {

			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

}
