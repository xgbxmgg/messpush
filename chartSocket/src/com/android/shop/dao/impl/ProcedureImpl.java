package com.android.shop.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import com.android.shop.database.DatabaseConnection;

public class ProcedureImpl {
	private DatabaseConnection conn = new DatabaseConnection();
	private Connection con = null;
	private CallableStatement call = null;

	public void execProcedure() {
		con = conn.getConnection();
		String sql = "{ call Set_Pushs() }";
		try {
			call = con.prepareCall(sql);
			call.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (call != null) {
					call.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
