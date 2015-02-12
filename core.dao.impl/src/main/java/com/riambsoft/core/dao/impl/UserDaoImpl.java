package com.riambsoft.core.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.riambsoft.core.dao.DaoException;
import com.riambsoft.core.dao.UserDao;
import com.riambsoft.core.pojo.User;

public class UserDaoImpl implements UserDao {

	private DataSource dataSource;

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public User getById(String id) throws DaoException {

		String sql = "select id, code, name, password, remark from rs_user where id = '"
				+ id + "'";

		return doGetBySql(sql);
	}

	public User getByCode(String userCode) throws DaoException {

		String sql = "select id, code, name, password, remark from rs_user where code = '"
				+ userCode + "'";

		return doGetBySql(sql);
	}

	private User doGetBySql(String sql) throws DaoException {
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			if (rs != null && rs.next()) {
				String rid = rs.getString(1);
				String rco = rs.getString(2);
				String rna = rs.getString(3);
				String rpa = rs.getString(4);
				String rre = rs.getString(5);

				User user = new User();
				user.setId(rid);
				user.setCode(rco);
				user.setName(rna);
				user.setPassword(rpa);
				user.setRemark(rre);

				return user;
			} else {
				throw new DaoException("未找到用户");
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

		String sql = "delete from rs_user where id = '" + id + "'";

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

	public void saveUser(User user) throws DaoException {

		String sql = "insert into rs_user(id, code, name, password, remark) values ('"
				+ user.getId()
				+ "', '"
				+ user.getCode()
				+ "', '"
				+ user.getName()
				+ "', '"
				+ user.getPassword()
				+ "', '"
				+ user.getRemark() + "');";

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

	public List<?> read(User user, Integer start, Integer limit,
			String sortField, String sortDir, String groupField,
			String groupDir, Map<String, ?> params) {

		String sql = "select * from (select a.id, a.code, a.name, a.password, a.remark, rownum rid "
				+ "from rs_user a order by "
				+ sortField
				+ " "
				+ sortDir
				+ ") b " + "where b.rid > " + start + " and rownum <= " + limit;

		System.out.println(sql);

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;

		List<User> users = new ArrayList<User>();

		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			while (rs != null && rs.next()) {

				String rid = rs.getString(1);
				String rco = rs.getString(2);
				String rna = rs.getString(3);
				String rre = rs.getString(4);

				User temp = new User();

				temp.setId(rid);
				temp.setCode(rco);
				temp.setName(rna);
				temp.setRemark(rre);

				users.add(temp);
			}

			return users;
		} catch (SQLException e) {
			throw new RuntimeException(e);
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

	public Integer getCount(User user, Map<String, ?> params) {
		String sql = "select count(*) from rs_user";
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = dataSource.getConnection();
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			if (rs != null && rs.next()) {
				return rs.getInt(1);
			} else {
				return 0;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
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
