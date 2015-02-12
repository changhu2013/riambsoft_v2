package com.riambsoft.core.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.riambsoft.core.dao.RsAppDao;
import com.riambsoft.core.pojo.RsApp;
import com.riambsoft.core.pojo.RsApp.Types;
import com.riambsoft.core.pojo.User;

public class RsAppDaoImpl implements RsAppDao {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public RsApp getById(String id) {
		String sql = "select id, system, code, name, type, remark from rs_app where id = '"
				+ id + "'";
		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			state = conn.createStatement();

			rs = state.executeQuery(sql);
			if (rs != null && rs.next()) {

				String system = rs.getString(2);
				String code = rs.getString(3);
				String name = rs.getString(4);
				String type = rs.getString(5);
				String remark = rs.getString(6);

				RsApp app = new RsApp();
				app.setId(id);
				app.setSystem(system);
				app.setCode(code);
				app.setName(name);
				Types t = Types.WEBAPP;
				if ("ANDROID".equals(type)) {
					t = Types.ANDROIE;
				} else if ("IOS".equals(type)) {
					t = Types.IOS;
				} else {
					t = Types.WEBAPP;
				}
				app.setType(t);
				app.setRemark(remark);

				return app;
			} else {
				throw new RuntimeException("未找到ID为" + id + "的应用程序");
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public void delById(String id) {

		String sql = "delete from rs_app where id = '" + id + "'";
		Connection conn = null;
		Statement state = null;
		try {
			conn = dataSource.getConnection();
			state = conn.createStatement();
			state.execute(sql);
		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

	public List<RsApp> getRsApps(User user) {

		String sql = "select d.id, d.system, d.code, d.name, d.type, d.remark "
				+ " from rs_user a, rs_user_role b, rs_role_app_method c, rs_app d "
				+ "where a.id = b.user_id and b.role_id = c.role_id "
				+ " and c.app_id = d.id and a.id = '" + user.getId() + "'";

		Connection conn = null;
		Statement state = null;
		ResultSet rs = null;

		List<RsApp> apps = new ArrayList<RsApp>();

		try {

			conn = dataSource.getConnection();
			state = conn.createStatement();
			rs = state.executeQuery(sql);

			while (rs != null && rs.next()) {

				String id = rs.getString(1);
				String system = rs.getString(2);
				String code = rs.getString(3);
				String name = rs.getString(4);
				String type = rs.getString(5);
				String remark = rs.getString(6);

				RsApp app = new RsApp();

				app.setId(id);
				app.setSystem(system);
				app.setCode(code);
				app.setName(name);

				Types t = Types.WEBAPP;
				if ("ANDROID".equals(type)) {
					t = Types.ANDROIE;
				} else if ("IOS".equals(type)) {
					t = Types.IOS;
				} else {
					t = Types.WEBAPP;
				}
				app.setType(t);
				app.setRemark(remark);

				apps.add(app);
			}

			return apps;

		} catch (SQLException e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (state != null) {
				try {
					state.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
				}
			}
		}
	}

}
