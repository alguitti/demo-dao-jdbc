package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn = null;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("INSERT INTO department "
			+ "(Id, Name) "
			+ "VALUES " 
			+ "(?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setInt(1, obj.getId());
			st.setString(2, obj.getName());
			int rows = st.executeUpdate();
			conn.commit();
			if (rows == 0) {
				System.err.println("Nothing was inserted!");
			} else {
				rs = st.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("Done! Id = " + id);
				}
			}
			conn.setAutoCommit(true);
		}
		
		catch (SQLException e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("UPDATE department "
					+ "SET Name = ? "
					+ "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			int rows = st.executeUpdate();
			if (rows == 0) {
				System.out.println("Failed");
			} else { 
				System.out.println("Updated row: " + rows);
			}
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE FROM department "
					+ "WHERE Id = ?");
			st.setInt(1, id);
			int rows = st.executeUpdate();
			if (rows == 0) {
				System.out.println("Failed");
			} else { 
				System.out.println("Delete row: " + rows);
			}
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeStatement(st);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		Department dep = null;
		try {
			st = conn.prepareStatement("select * from department "
					+ "where Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				dep = new Department(id, rs.getString("Name"));
			} else {
				System.out.println("Nothing was found under this id");
			}
			return dep;
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		
	}

	@Override
	public List<Department> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> list = new ArrayList<>();
		Department dep = null;
		try {
			st = conn.prepareStatement("select * from department");
			rs = st.executeQuery();
			while (rs.next()) {
				list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
			} 
			if (list.size() == 0) {
				System.err.println("Nothing returned!");
				return null;
			} else {
				return list;
			}
		}
		
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
