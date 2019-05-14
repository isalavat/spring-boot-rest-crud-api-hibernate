package kg.salavat.springboot.cruddemo.entity;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;

import kg.salavat.springboot.cruddemo.dao.EmployeeDAO;

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

}
