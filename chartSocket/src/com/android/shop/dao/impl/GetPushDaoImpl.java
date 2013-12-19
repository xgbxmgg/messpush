package com.android.shop.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.android.shop.dao.GetPushDao;
import com.android.shop.database.DatabaseConnection;
import com.enveesoft.gz163.domain.GetPush;

public class GetPushDaoImpl implements GetPushDao {
	private DatabaseConnection conn = new DatabaseConnection();
	private Connection con = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;

	@Override
	public List<GetPush> findPush() {
		List<GetPush> list = new ArrayList<GetPush>();
		con = conn.getConnection();
		// String sql = "select imsi,type,context,flag,adid from dbo.get_push";
		String sql = "select type,context,flag,adid,get_push_id from dbo.get_push";
		try {
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				GetPush push = new GetPush();
				// push.setImsi(rs.getString(1));
				push.setType(rs.getInt(1));
				push.setContext(rs.getString(2));
				push.setFlag(rs.getString(3));
				push.setAdid(rs.getInt(4));
				push.setPushId(rs.getInt(5));
				list.add(push);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return list;
	}

	@Override
	public List<GetPush> findByIms(String ims) {
		List<GetPush> list = new ArrayList<GetPush>();
		con = conn.getConnection();
		// String sql =
		// "select imsi,type,context,flag,adid from dbo.get_push where imsi=?";
		String sql = "select type,context,flag,adid,get_push_id from dbo.get_push where imsi=?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, ims);
			rs = ps.executeQuery();
			while (rs.next()) {
				GetPush push = new GetPush();
				// push.setImsi(rs.getString(1));
				push.setType(rs.getInt(1));
				push.setContext(rs.getString(2));
				push.setFlag(rs.getString(3));
				push.setAdid(rs.getInt(4));
				push.setPushId(rs.getInt(5));
				list.add(push);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return list;
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
