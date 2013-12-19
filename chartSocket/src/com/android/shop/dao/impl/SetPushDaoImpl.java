package com.android.shop.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.android.shop.dao.SetPushDao;
import com.android.shop.database.DatabaseConnection;

public class SetPushDaoImpl implements SetPushDao {
	private DatabaseConnection conn = new DatabaseConnection();
	private Connection con = null;
	private PreparedStatement ps = null;
	private Statement st = null;
	private ResultSet rs = null;

	@Override
	public void insertPush(String ims) {
		con = conn.getConnection();
		String sql = "insert into dbo.set_push (imsi) values (?)";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, ims);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}

	}

	@Override
	public void deletePush() {
		con = conn.getConnection();
		String sql = "delete from dbo.set_push";
		try {
			ps = con.prepareStatement(sql);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}

	private void close() {
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (st != null) {
				st.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (ps != null) {
				ps.close();
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
