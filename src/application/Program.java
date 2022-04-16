package application;

import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		try (Scanner sc = new Scanner(System.in)) {
			SellerDao sellerDao = DaoFactory.createSellerDao();

			System.out.println("****Test 1 - findById****");
			System.out.println(sellerDao.findById(3));

			System.out.println("****Test 2 - findByDeparment****");
			List<Seller> sellers = sellerDao.findByDepartment(new Department(sc.nextInt()));
			sellers.forEach(s -> System.out.println(s));
			
			System.out.println("****Test 3 - findAll****");
			sellers = sellerDao.findAll();
			sellers.forEach(System.out::println);
		}

		catch (NullPointerException e) {
			System.err.println("ID de Department não encontrado no DB \nError: " + e.getMessage());
		}

	}

}
