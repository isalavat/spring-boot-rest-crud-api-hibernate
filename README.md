# spring-boot-rest-crud-api-hibernate
<h1>Spring Boot - Build REST CRUD API with Hibernate</h1>
Create Database with employee table:<br>
<pre>
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
spring.datasource.url=jdbc:mysql://localhost:3306/employee_directory
spring.datasource.username=sbstudent
spring.datasource.password=sbstudent
</pre>
Then create employee class and map it to db table <br>


