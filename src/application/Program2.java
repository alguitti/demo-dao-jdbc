package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import db.DB;
import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program2 {

	public static void main(String[] args) {
		Department testDep = new Department(1, "Mercado");
		Department getDep = null;
		List<Department> list = new ArrayList<>();
		
		try (Scanner sc = new Scanner(System.in)) {
		DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
		
		System.out.println("****Teste 1 - Insert****");
		testDep.setId(sc.nextInt());
		departmentDao.insert(testDep);
		
		System.out.println("****Test 2 - Delete****");
		departmentDao.deleteById(sc.nextInt());
		
		System.out.println("****Test 3 - FindById****");
		getDep = departmentDao.findById(sc.nextInt());
		System.out.println(getDep);
		
		System.out.println("****Test 4 - Update****");
		getDep.setName("PetShop");
		departmentDao.update(getDep);
		
		System.out.println("****Test 5 - FindAll****");
		list = departmentDao.findAll();
		list.forEach(d -> System.out.println(d));
		
		}
		
		catch (InputMismatchException e) {
			System.err.println(e.getMessage());
		}
		
		finally {
			DB.closeConnection();
		}
	}

}
