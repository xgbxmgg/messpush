package com.android.shop.test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.android.shop.database.DatabaseConnection;

public class ProcedureTest {
	public static void main(String[] args) {
		DatabaseConnection conn = new DatabaseConnection();
		Connection connection = conn.getConnection();
		System.out.println(connection);
		/*String sql = "{ call mutProc() }";
		CallableStatement proc;
		try {
			proc = connection.prepareCall(sql);
			boolean hadResults = proc.execute();
			while (hadResults) {
				ResultSet rs = proc.getResultSet();
				ResultSetMetaData rsmd1 = rs.getMetaData();
				while (rs != null && rs.next()) {
					 * int id = rs.getInt(1); System.out.println(id);
					 
					for (int i = 1; i < rsmd1.getColumnCount(); i++) {
						String type = rsmd1.getColumnTypeName(i);
						//System.out.println(type);
						String str = rs.getObject(i).toString();
						System.out.println(str);
					}
				}
				hadResults = proc.getMoreResults();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	}
}
