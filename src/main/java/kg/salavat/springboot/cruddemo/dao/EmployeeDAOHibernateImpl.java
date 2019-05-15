package kg.salavat.springboot.cruddemo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kg.salavat.springboot.cruddemo.entity.Employee;
@Repository
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

	@Override
	@Transactional
	public Employee findById(int id) {
		// get the curren hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		// fetch the employee by given id 
		Employee employee = currentSession.get(Employee.class, id);
		
		return employee;
	}
	@Override
	public void save(Employee employee) {
		// get current hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		
		/*
		 * save/insert the given Entity
		 * Note that if id is 0 then it will be saved/inserted 
		 * otherwise updated
		 */
		currentSession.saveOrUpdate(employee);
	}
	@Override
	public void deleteById(int id) {
		// get the curren hibernate session
		Session currentSession = entityManager.unwrap(Session.class);
		// delete employee by given id
		Query<Employee> query = currentSession.createQuery(
				"delete from Employee where id=:employeeId");
		query.setParameter("employeeId", id);
		query.executeUpdate();
				
	}

}
