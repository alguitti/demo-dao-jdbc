package application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		Department testDep = new Department(1);
		
		try (Scanner sc = new Scanner(System.in)) {
			Seller seller = new Seller ("Andre", "asd@asd", sdf.parse("12/10/1992"), 5000.0, testDep);
			SellerDao sellerDao = DaoFactory.createSellerDao();

			System.out.println("****Test 1 - findById****");
			
			System.out.println(sellerDao.findById(3));

			System.out.println("****Test 2 - findByDeparment****");
			List<Seller> sellers = sellerDao.findByDepartment(new Department(sc.nextInt()));
			sellers.forEach(s -> System.out.println(s));
			
			System.out.println("****Test 4 - Insert****");
			sellerDao.insert(seller);
			System.out.println("Success, seller ID: " + seller.getId());
			
			
			System.out.println("****Test 5 - Update****");
			seller = sellerDao.findById(2);
			seller.setName("Pedro Capivara");
			sellerDao.update(seller);
			System.out.println("Update completed");
			
			
			/*System.out.println("****Test 3 - findAll****");
			sellers = sellerDao.findAll();
			sellers.sort((s1,s2) -> s1.getName().toUpperCase().compareTo(s2.getName().toUpperCase()));
			sellers.forEach(System.out::println); */
		
		}

		catch (NullPointerException e) {
			System.err.println("ID de Department não encontrado no DB \nError: " + e.getMessage());
			
			System.out.println("****Test 3 - findAll****");
			SellerDao sellerDao = DaoFactory.createSellerDao();
			List <Seller> sellers = sellerDao.findAll();
			sellers.forEach(System.out::println);
		}
		
		catch (InputMismatchException e) {
			System.err.println("Input must be an Integer!");
		}
		
		catch (ParseException e) {
			System.err.println("Error: " + e.getMessage());
		}

	}

}
