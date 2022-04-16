package application;

import java.util.Date;

import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		Department dept = new Department(1, "Books");
		
		Seller seller = new Seller(1, "Bob", "asd@asd", new Date(), 3000.0, dept);
		
		System.out.println(dept);
		System.out.println(seller);
		
	}

}
