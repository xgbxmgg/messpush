package com.android.shop.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class TableToBean {
	private static final String LINE = "\r\n";
	private static final String TAB = "\t";
	String packages = this.getClass().getPackage().getName()
			.replace("util", "vo");
	private static Map<String, String> map;
	static {
		map = new HashMap<String, String>();
		map.put("VARCHAR", "String");
		map.put("INTEGER", "int");
		map.put("INT", "int");
		map.put("FLOAT", "float");
		map.put("TIMESTAMP", "Date");
		map.put("CHAR", "String");
		map.put("DATETIME", "Date");
		map.put("DATE", "Date");
		map.put("TIMESTAMP_IMPORT", "import java.sql.Date");
		map.put("DATETIME_IMPORT", "import java.sql.Date");
		map.put("DATE_IMPORT", "import java.sql.Date");
	}

	public static String getPojoType(String dataType) {
		StringTokenizer st = new StringTokenizer(dataType);
		return map.get(st.nextToken());
	}

	public static String getImport(String dataType) {
		// 浠巑ap鑾峰彇value鍊肩湅鏄惁闇�寮曞叆鍖呫�
		if (map.get(dataType) == null || "".equals(map.get(dataType))) {
			return null;
		} else {
			return map.get(dataType);
		}
	}

	public void tableToBean(Connection connection, String tableName)
			throws SQLException {
		String sql = "select * from " + tableName + " where 1<>1";
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = connection.prepareStatement(sql);
		rs = ps.executeQuery();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		StringBuffer sb = new StringBuffer();
		tableName = tableName.toLowerCase();
		tableName = this.dealLine(tableName);
		tableName = tableName.substring(0, 1).toUpperCase()
				+ tableName.substring(1, tableName.length());
		sb.append("package " + this.packages + ";");
		sb.append(LINE);
		// 寮曞叆class鏂囦欢
		importPackage(md, columnCount, sb);
		sb.append(LINE);
		sb.append(LINE);
		sb.append("public class " + tableName + "{");
		sb.append(LINE);
		// 瀹氫箟灞炴�
		defProperty(md, columnCount, sb);
		// 鐢熸垚set鍜実et鏂规硶
		genSetGet(md, columnCount, sb);
		sb.append("}");
		// 鑾峰彇椤圭洰璺緞
		String paths = System.getProperty("user.dir");
		String endPath = paths + "\\src\\"
				+ (packages.replace("/", "\\")).replace(".", "\\");
		// 鐢熸垚java绫�鏂囦欢
		buildJavaFile(endPath + "\\" + tableName + ".java", sb.toString());
	}

	private void buildJavaFile(String filePath, String fileContent) {
		try {
			File file = new File(filePath);
			FileOutputStream osw = new FileOutputStream(file);
			PrintWriter pw = new PrintWriter(osw);
			pw.println(fileContent);
			pw.close();

		} catch (Exception e) {
			System.out.println("" + e.getMessage());
		}
	}

	private void genSetGet(ResultSetMetaData md, int columnCount,
			StringBuffer sb) throws SQLException {
		for (int i = 1; i <= columnCount; i++) {
			sb.append(TAB);
			String pojoType = getPojoType(md.getColumnTypeName(i));
			String columnName = dealLine(md, i);
			String getName = null;
			String setName = null;
			if (columnName.length() > 1) {
				getName = "public " + pojoType + " get"
						+ columnName.substring(0, 1).toUpperCase()
						+ columnName.substring(1, columnName.length())
						+ "( ) {";
				setName = "public void set"
						+ columnName.substring(0, 1).toUpperCase()
						+ columnName.substring(1, columnName.length()) + "("
						+ pojoType + " " + columnName + ") {";
			} else {
				getName = "public get" + columnName.toUpperCase() + "() {";
				setName = "public void set" + columnName.toUpperCase() + "("
						+ pojoType + " " + columnName + ") {";
			}
			sb.append(LINE).append(TAB).append(getName);
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("return " + columnName + ";");
			sb.append(LINE).append(TAB).append("}");
			sb.append(LINE);
			sb.append(LINE).append(TAB).append(setName);
			sb.append(LINE).append(TAB).append(TAB);
			sb.append("this." + columnName + "=" + columnName + ";");
			sb.append(LINE).append(TAB).append("}");
			sb.append(LINE);
		}
	}

	private String dealLine(ResultSetMetaData md, int i) throws SQLException {
		String columnName = md.getColumnName(i);
		columnName = dealName(columnName);
		return columnName;
	}

	private void defProperty(ResultSetMetaData md, int columnCount,
			StringBuffer sb) throws SQLException {
		for (int i = 1; i <= columnCount; i++) {
			sb.append(TAB);
			String columnName = dealLine(md, i);
			sb.append("private " + getPojoType(md.getColumnTypeName(i)) + " "
					+ columnName + ";");
			sb.append(LINE);
		}
	}

	private void importPackage(ResultSetMetaData md, int columnCount,
			StringBuffer sb) throws SQLException {
		for (int i = 1; i <= columnCount; i++) {
			//
			String im = getImport(md.getColumnTypeName(i) + "_IMPORT");
			if (im != null) {
				sb.append(im + ";");
				sb.append(LINE);
			}
		}
	}

	private String dealLine(String tableName) {
		tableName = dealName(tableName);
		return tableName;
	}

	// 鍘婚櫎涓嬪垝绾匡紝骞跺皢鍗曡瘝绗竴涓瓧姣嶅ぇ鍐�
	private String dealName(String columnName) {
		if (columnName.contains("_")) {
			StringBuffer names = new StringBuffer();
			String arrayName[] = columnName.split("_");
			names.append(arrayName[0].toLowerCase());
			for (int i = 1; i < arrayName.length; i++) {
				String arri = arrayName[i].toLowerCase();
				String tem = arri.substring(0, 1).toUpperCase()
						+ arri.subSequence(1, arri.length());
				names.append(tem);
			}
			columnName = names.toString();
		} else {
			columnName = columnName.toLowerCase();
		}
		return columnName;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			SQLException {
		String jdbcString = "jdbc:sqlserver://220.172.33.53:1433;DatabaseName=infomaxt";
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		Connection con = DriverManager.getConnection(jdbcString, "sa",
				"gyetenginfo");
		DatabaseMetaData databaseMetaData = con.getMetaData();
		String[] tableType = { "TABLE" };
		ResultSet rs = databaseMetaData.getTables(null, null, "%", tableType);
		TableToBean d = new TableToBean();
		while (rs.next()) {
			String tableName = rs.getString(3).toString();
			d.tableToBean(con, tableName);
		}
	}
}
