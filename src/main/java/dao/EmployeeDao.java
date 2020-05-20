package dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import employee.Employee;
import employee.Punches;
import system.MySystem;

@Repository
public class EmployeeDao {

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void save(Employee e) {
		Session session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		
		if(e.isReadyToSave()) {
			if (MySystem.DEBUG_MODE) System.err.println("Geeting ready to save Employee");
			int empId =  (Integer) session.save(e);
			transaction.commit();
			if (MySystem.DEBUG_MODE) System.out.println("Employee saved succesfully Employee Id: " + empId);
			session.close();
		}else {
			System.out.println("Employee needs to have both first and last name try again");
		}
	}

	public Employee getEmployee(int id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee employee = (Employee) session.get(Employee.class, id);
		session.close();
		
		return employee;
	}
	
	public void update(Employee e) {
		Session session = getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.update(e);
		transaction.commit();
		session.close();
		if (MySystem.DEBUG_MODE) System.out.println("Employee updated succesfully");
		
	}

	public void recordEmployeePunch(int id, String punchType) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		Employee employee = (Employee) session.get(Employee.class, id);
		if(employee == null) {
			System.err.println("employee does not exist");
			return;
		}
			
		
		Query query =  session.createQuery(	"from Punches where employee_id = ? and outPunch is null");
		
		query.setInteger(0, employee.getId());
		List<Punches> listOfPunches  = query.list();
		
		
		if (listOfPunches.size() == 0 && "I".equals(punchType)) {
			employee.punchIn();
			if (MySystem.DEBUG_MODE) System.out.println("ADDING A NEW IN PUNCH");
			session.close();
			update(employee);

		} else if (listOfPunches.size() == 1 && "O".equals(punchType)) {
			Punches p = listOfPunches.get(0);
			p.setOutPunch(new Date());
			if (MySystem.DEBUG_MODE) System.out.println("ADDING A NEW OUT PUNCH");
			session.update(p);
			session.getTransaction().commit();

		} else if (listOfPunches.size() > 1) {
			System.err.println("UNRESOLVED ERROR IN YOUR PUNCHES CALL MANAGER");

		} else {
			System.err.println("ILLEGAL PUNCH NOT ACCPETED");
			if( "I".equals(punchType)) System.out.println("You need to punch out before you can punch in");
			if( "O".equals(punchType)) System.out.println("You need to punch in before you can punch out");
		}
		
	}

	public void generateReport(int empId) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee employee = (Employee) session.get(Employee.class, empId);
		employee.getMyPunches();
		employee.generateReport();
		session.close();
		System.out.println();
	}

	public void generateReport() {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		
		String hql = " from Employee";
		Query query = session.createQuery(hql);
		
		List<Employee> empList = query.list();
		for(Employee e : empList) {
			e.getMyPunches();
			e.generateReport();
			System.out.println();
		}
		session.close();
	}

	public void delete(int id) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee e = getEmployee(id);
		session.delete(e);
		session.getTransaction().commit();
		session.close();
		System.out.println("Employee successfully deleted");
	}

	public void update(int id, String field, String value) {
		Employee e = getEmployee(id);
		if("F".equals(field)) {
			e.setFirst_name(value);
		}else if ("L".equals(field)) {
			e.setLast_name(value);
		}else {
			System.err.println("Incorrect field selected");
		}
		update(e);
	}

	public double calculateWage(int id, double hourlyWage) {
		Session session = getSessionFactory().openSession();
		session.beginTransaction();
		Employee emp = (Employee) session.get(Employee.class, id);
		emp.getMyPunches();
		double total =  emp.calculateWage(hourlyWage);
		session.close();
		return total;
	}




	
	
}
