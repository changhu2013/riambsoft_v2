package com.riambsoft.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.RsMethodDao;
import com.riambsoft.core.pojo.Role;
import com.riambsoft.core.pojo.RsMethod;
import com.riambsoft.core.pojo.User;

public class RsMethodDaoImpl implements RsMethodDao {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getId(String controller, String service, String method) {
		return controller + "_" + service + "_" + method;
	}

	public RsMethod getById(String id) throws DaoException {
		String sql = "select id, controller_name, service_name, method_name, control, remark "
				+ "from rs_method where id = '" + id + "'";

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			if (rs != null && rs.next()) {
				String rid = rs.getString(1);
				String rcn = rs.getString(2);
				String rsn = rs.getString(3);
				String rmn = rs.getString(4);
				String rco = rs.getString(5);
				String rre = rs.getString(6);

				RsMethod rsmethod = new RsMethod();

				rsmethod.setId(rid);
				rsmethod.setController(rcn);
				rsmethod.setService(rsn);
				rsmethod.setMethod(rmn);
				rsmethod.setControl("true".equals(rco));
				rsmethod.setRemark(rre);

				return rsmethod;
			} else {
				throw new DaoException("未找到权限信息[" + id + "]");
			}
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stat != null) {
				try {
					stat.close();
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

	public void delById(String id) throws DaoException {
		String sql = "delete from rs_method where id = '" + id + "'";
		Connection conn = null;
		Statement stat = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			stat.execute(sql);
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if (stat != null) {
				try {
					stat.close();
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

	public void saveRsMethod(RsMethod rsmethod) throws DaoException {

		String dsql = "delete from rs_method where id = '" + rsmethod.getId() + "'";
		String isql = "insert into rs_method (id, controller_name, service_name, method_name, control, remark)"
				+ "values ('"
				+ rsmethod.getId()
				+ "','"
				+ rsmethod.getController()
				+ "', '"
				+ rsmethod.getService()
				+ "', '"
				+ rsmethod.getMethod()
				+ "', '"
				+ (rsmethod.isControl() ? "true" : "false")
				+ "', '"
				+ rsmethod.getRemark() + "')";

		System.out.println("DSQL:" + dsql);
		System.out.println("ISQL:" + isql);

		Connection conn = null;
		PreparedStatement dstat = null;
		PreparedStatement istat = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);

			dstat = conn.prepareStatement(dsql);
			istat = conn.prepareStatement(isql);

			dstat.execute();
			istat.execute();

			conn.commit();
		} catch (SQLException e) {
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException e1) {
				}
			}
			throw new DaoException(e);
		} finally {
			if (dstat != null) {
				try {
					dstat.close();
				} catch (SQLException e) {
				}
			}
			if (istat != null) {
				try {
					istat.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				}
			}
		}

	}

	public List<RsMethod> getRsMethods(Role role) throws DaoException {

		String sql = "select b.id, b.controller_name, b.service_name, b.method_name, b.control, b.remark"
				+ " from rs_role_right a, rs_method b"
				+ " where a.right_id = b.id and a.role_id = '"
				+ role.getId()
				+ "'";

		return doGetRsMethods(sql);
	}

	public List<RsMethod> getRsMethods(User user) throws DaoException {

		String sql = "select c.id, c.controller_name, c.service_name, c.method_name, c.control, c.remark"
				+ " from rs_user_role a, rs_role_right b, rs_method c"
				+ " where a.role_id = b.role_id"
				+ " and b.right_id = c.id "
				+ " and a.user_id = '" + user.getId() + "'";

		return doGetRsMethods(sql);
	}

	public List<RsMethod> getRsMethods(User user, String controller)
			throws DaoException {

		String sql = "select c.id, c.controller_name, c.service_name, c.method_name, c.control, c.remark"
				+ " from rs_user_role a, rs_role_right b, rs_method c"
				+ " where a.role_id = b.role_id"
				+ " and b.right_id = c.id "
				+ " and a.user_id = '"
				+ user.getId()
				+ "' and c.controller_name = '" + controller + "'";

		return doGetRsMethods(sql);
	}

	private List<RsMethod> doGetRsMethods(String sql) throws DaoException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			List<RsMethod> list = new ArrayList<RsMethod>();

			while (rs != null && rs.next()) {
				String rid = rs.getString(1);
				String rcn = rs.getString(2);
				String rsn = rs.getString(3);
				String rmn = rs.getString(4);
				String rco = rs.getString(5);
				String rre = rs.getString(6);

				RsMethod rsmethod = new RsMethod();

				rsmethod.setId(rid);
				rsmethod.setController(rcn);
				rsmethod.setService(rsn);
				rsmethod.setMethod(rmn);
				rsmethod.setControl("true".equals(rco));
				rsmethod.setRemark(rre);

				list.add(rsmethod);
			}

			return list;
		} catch (SQLException e) {
			throw new DaoException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (stat != null) {
				try {
					stat.close();
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
