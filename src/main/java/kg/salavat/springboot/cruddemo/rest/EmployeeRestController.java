package kg.salavat.springboot.cruddemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kg.salavat.springboot.cruddemo.dao.EmployeeDAO;
import kg.salavat.springboot.cruddemo.entity.Employee;
import kg.salavat.springboot.cruddemo.service.EmployeeService;

@RestController
@RequestMapping("/api") // main req mapping
public class EmployeeRestController {
	
	private EmployeeService employeeService;
	
	// injecttion of the employee dao
	@Autowired
	public EmployeeRestController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	//expose single employee "/employees/{id}"
	@GetMapping ("/employees/{employeeId}")
	public Employee getEmployee(@PathVariable int employeeId) {
		Employee employee = employeeService.findById(employeeId);
		if(employee == null) {
			throw new RuntimeException("Employee id not found : "+employeeId);
		}
		return employee;
	}
	
	// GET method to expose "/employees"
	@GetMapping ("/employees")
	public List<Employee> findAll() {
		return employeeService.findAll();
	}
	
	// POST method for adding a new employee from given request body
	@PostMapping("/employees")
	public Employee addEmployee(@RequestBody Employee employee) {
		// set id to 0 to force a save of new employee
		employee.setId(0);
		employeeService.save(employee);
		return employee;
	}
	 
	// PUT method for updating the given employee in request body
	@PutMapping("/employee")
	public Employee updateEmployee (@RequestBody Employee employee) {
		employeeService.save(employee);
		return employee;
	}
	
	// Delete method to delete an employee by given id 
	@DeleteMapping ("/employee/{employeeId}")
	public String deleteEmployee(@PathVariable int employeeId) {
		Employee tempEmployee = employeeService.findById(employeeId);
		
		// throw exception if null
		if(tempEmployee == null) {
			throw new RuntimeException("Employee id not found - "+employeeId);
		}
		
		employeeService.deleteById(employeeId);
		
		return "Deleted employee id - " + employeeId;
	}
}
