package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;

	public SellerDaoJDBC() {
	}

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {

		PreparedStatement st = null;
		ResultSet rs = null;

		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement("INSERT INTO seller " + "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES " + "(?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			int row = st.executeUpdate();
			conn.commit();
			if (row == 0) {
				System.err.println("Nothing was inserted!");
			} else {
				rs = st.getGeneratedKeys();
				while (rs.next()) {
					int id = rs.getInt(1);
					System.out.println("Done! Id = " + id);
					obj.setId(id);
				}
			}
			conn.setAutoCommit(true);
		}

		catch (SQLException e) {
			try {
				conn.rollback();
				throw new DbException("Rolled back!: " + e.getMessage());
			} catch (SQLException e1) {
				throw new DbException("Error while rolling back: " + e1.getMessage());
			}
		}

		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"UPDATE seller "
					+ "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+ "WHERE Id = ?");
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date (obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			int rows = st.executeUpdate();
			if (rows == 0) {
				System.out.println("failed");
			} else {
				System.out.println("rows = " + rows);
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

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"select seller.*, department.Name as DepName " + "From seller inner join department "
							+ "on seller.DepartmentId = department.Id " + "where seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			/*******************************
			 * O rs é um ponteiro para as informações processadas e começa no valor 0 onde
			 * não há nenhuma informação, nesse caso num if se o rs.next() der false quer
			 * dizer que o Id não retornou nenhum row e retorna null caso tenha já estaremos
			 * na linha e basta atribuir os dados para o Seller e Department de acordo com
			 * os comandos abaixo
			 **********************************/
			if (rs.next()) {
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(dep, rs);
				return obj;
			}
			return null;
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
	public List<Seller> findAll() {
		List<Seller> sellers = new ArrayList<>();
		Map<Integer, Department> mapDept = new HashMap<>();
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
					+ "FROM seller INNER JOIN department "
					+ "ON seller.DepartmentId = department.Id "
					+ "ORDER BY Name");
			rs = st.executeQuery();
			while (rs.next()) {
				Department dep = mapDept.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					mapDept.put(dep.getId(), dep);
				}
				sellers.add(instantiateSeller(dep, rs));
			}
			if (sellers.size() != 0) {
				return sellers;
			} else {
				return null;
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
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		List<Seller> sellers = new ArrayList<>();
		Map<Integer, Department> mapDept = new HashMap<>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName " + "from seller inner join department "
							+ "on seller.DepartmentId = department.Id " + "where DepartmentId = ? " + "order by Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				//Se fizemos simplesmente o comando abaixo cada elemento da lista 
				//terá uma instancia própria de department (errado)
				//Department dept = instantiateDepartment(rs);
				//testa se já existe instancia com a chave Id
				Department dep = mapDept.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					//se nulo instancia e guarda com a chave
					mapDept.put(rs.getInt("DepartmentId"), dep);
				}
				sellers.add(instantiateSeller(dep, rs));
			}
			if (sellers.size() != 0) {
				return sellers;
			} else {
				return null;
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

	private Seller instantiateSeller(Department dep, ResultSet rs) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

}