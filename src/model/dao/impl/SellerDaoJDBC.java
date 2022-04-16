package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Seller> findByDepartment(Department department) {
		List<Seller> sellers = new ArrayList<>();
		PreparedStatement st = null;
		ResultSet rs = null;

		try {

			st = conn.prepareStatement(
					"select seller.*,department.Name as DepName " + "from seller inner join department "
							+ "on seller.DepartmentId = department.Id " + "where DepartmentId = ? " + "order by Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();

			while (rs.next()) {
				Department dept = instantiateDepartment(rs);
				sellers.add(instantiateSeller(dept, rs));
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