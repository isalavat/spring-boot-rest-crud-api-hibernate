# spring-boot-rest-crud-api-hibernate
<h3>This project was developed as an example following the lessons of the Spring Framework and Hibernate Course of Chad Darby on Udemy (Spring & Hibernate for Beginners (Includes Spring Boot))<h3> <hr>

<h1>Spring Boot - Build REST CRUD API with Hibernate</h1>
Create Database with employee table:<br>
<pre>
#TODO create database employee_directory
use employee_directory;
create table employee (
	id int not null auto_increment,
    first_name varchar(45) default null,
    last_name varchar(45) default null,
    email varchar(45) default null,
    primary key(id)
 );
 
 insert into employee values
	(1, 'Lesile', 'Andrews', 'leslie@luv2code.com'),
    (2, 'Emma', 'Baumgarten', 'emma@luv2code.com'),
    (3, 'Avani', 'Gupta', 'avani@luv2code.com'),
    (4, 'Yuri', 'Petrov', 'yuri@luv2code.com'),
    (5, 'Juan', 'Vega', 'juan@luv2code.com')
</pre>

<h2>Integrating Hibernate and JPA</h2>
Spring Boot configures datasource automaticly based on pom.xml(Session Factory needs datasource and Session Factory will be used from DAO)<br>
That means Datasource, EntityManager will be configured automatically 
<h3>What is JPA</h3>
JPA - Java Persistence API. Standart API for ORM. It defines only the specification (interface) but not the implementation<br>
Hibernate is one the technologies that implements those specifications of JPA<br>
Using of Standatr API as JPA gives us the flexibility. We may not go to the details of the vendors such as Hibernate<br> 
EntityManager is similar to SessionFactory of Hibernate, but it wrapps the SessionFactory. EntityManager will be used in DAO to access the DB<br>
You need to configure following properties:<br>
<pre>
#JDBC properties
spring.datasource.url=jdbc:mysql://localhost:3306/employee_directory?useSSL=false&useLegacyDatetimeCode=false&serverTimezone=UTC
spring.datasource.username=hbstudent
spring.datasource.password=hbstudent
</pre>
Then create employee class and map it to db table: <br>
<pre>
@Entity
@Table(name="employee")
public class Employee {
	//fields and their mapping to db table columns
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="email")
	private String email;
	
	//default constructor is required by hibernate
	public Employee() {}

	public Employee(String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	//getter and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	//toSrting
	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}
}
</pre><br>
<b>Create DAO interface and implementation class:</b><br>
<pre>
public interface EmployeeDAO {
	public List<Employee> findAll();
}

public class EmployeeDAOHibernateImpl implements EmployeeDAO{
	
	private EntityManager entityManager;
	
	//constructor injection
	@Autowired
	public EmployeeDAOHibernateImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	@Override
	@Transactional
	public List<Employee> findAll() {
		// get the current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		
		// create query
		Query<Employee> query = currentSession.createQuery("from Employee", Employee.class);
		
		// get the result by executing query
		List<Employee> employees = query.getResultList();
		
		return employees;
	}
	
	......
}
</pre>

<b>Create Service layer</b><br>
<pre>
@Service
public class EmployeeServiceImpl implements EmployeeService{
	private EmployeeDAO employeeDAO;
	@Autowired
	public EmployeeServiceImpl(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}
		
	@Override
	@Transactional
	public List<Employee> findAll() {
		// TODO Auto-generated method stub
		return employeeDAO.findAll();
	}

	@Override
	@Transactional
	public Employee findById(int id) {
		// TODO Auto-generated method stub
		return employeeDAO.findById(id);
	}
	.......
}	
</pre>
<b>And then Create corresponiding RestController with mapped methods:<b><br>
<pre>
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
	
</pre>

<b>Soruce - Spring Hibernate Tutorial on Udemy. Author Chad Derby</b>
