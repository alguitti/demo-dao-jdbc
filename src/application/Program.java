package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		List <Seller> sellers = null;
		Department dept = new Department(1, "Computers");
		
		SellerDao sellerDao = DaoFactory.createSellerDao();

		System.out.println("****Test 1 - finById****");
		System.out.println(sellerDao.findById(3));

		System.out.println("****Test 2 - finByDeparment****");
		sellers = sellerDao.findByDepartment(dept);
		sellers.forEach(s -> System.out.println(s));
	}

}
