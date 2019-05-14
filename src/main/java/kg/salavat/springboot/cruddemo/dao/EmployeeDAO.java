package kg.salavat.springboot.cruddemo.dao;

import java.util.List;

import kg.salavat.springboot.cruddemo.entity.Employee;

public interface EmployeeDAO {
	public List<Employee> findAll();
}
