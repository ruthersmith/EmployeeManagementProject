package system;

import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dao.EmployeeDao;
import employee.Employee;

public class MySystem {
	
	private static final String EXIT_STRING = "EXIT";
	public static final boolean DEBUG_MODE = true;
	private static EmployeeDao employeeDao;


	public static void main(String[] args) {
		ApplicationContext context=new ClassPathXmlApplicationContext("springHibernate.cfg.xml");
		employeeDao =  (EmployeeDao) context.getBean("employeeDao");
		Scanner scanner =  new Scanner(System.in);
		
		
		printMenu();

		String action = scanner.nextLine();
		
		while(!EXIT_STRING.equals(action)) {
			
			if("1".equals(action)) {
				handleAddingEmployee(scanner);
			}else if("2".equals(action)) {
				handleEmployeePunches(scanner);
			}else if("3".equals(action)) {
				handleGeneratingReport(scanner);
			}else if("4".equals(action)) {
				handleDeletingEmployee(scanner);
			}else if("5".equals(action)) {
				handleUpdatingEmployee(scanner);
			}else if("6".equals(action)) {
				handleAmountForGivenEmp(scanner);
			}
				
			printMenu();
			action = scanner.nextLine();
			
		}
		
	}

	private static void handleAmountForGivenEmp(Scanner scanner) {
		int id = Integer.parseInt(getStringInput(scanner, "enter employee id"));
		double hourlyWage = Double.parseDouble(getStringInput(scanner, "enter employee hourly wage"));
		double total =  employeeDao.calculateWage(id,hourlyWage);
		System.out.println("Total: " + total);
	}

	private static void handleUpdatingEmployee(Scanner scanner) {
		int id = Integer.parseInt(getStringInput(scanner, "Enter the employee Id"));
		String field = getStringInput(scanner, "Enter 'F' to update first name or 'L' to update last name");
		String value = getStringInput(scanner, "Enter new value");
		employeeDao.update(id,field,value);
	
	}

	private static void handleDeletingEmployee(Scanner scanner) {
		int id = Integer.parseInt(getStringInput(scanner, "Please Input Employee Id"));
		employeeDao.delete(id);
	}

	private static void handleGeneratingReport(Scanner s) {
		String reportType = getStringInput(s, "Enter ‘I’ to generate Report for singular employee or 'A' For all employee ");
		if("I".equals(reportType)) {
			int empId = Integer.parseInt(getStringInput(s, "Enter the employee id number"));
			employeeDao.generateReport(empId);
		}else if("A".equals(reportType)) {
			employeeDao.generateReport();
		}
	}

	private static void printMenu() {
		System.out.println("Enter '1' - to add a new employee");
		System.out.println("Enter '2' - to punch yourself in/out");
		System.out.println("Enter '3' - to print out the report");
		System.out.println("Enter '4' - to delete employee");
		System.out.println("Enter '5' - to update an employee");
		System.out.println("Enter '6' - to Get amount to give to employee");
		System.out.println("Enter 'EXIT' - to add a new employee");

	}

	private static void handleEmployeePunches(Scanner s) {
		if(DEBUG_MODE) System.err.println("handling employee punches");
		int id = Integer.parseInt(getStringInput(s, "Please Enter your Employee Id")) ;
		String punchType = getStringInput(s, "Enter ‘I’ for a Punch In or ‘O’ for a punch out");
		employeeDao.recordEmployeePunch(id,punchType);
	}


	private static void handleAddingEmployee(Scanner scanner) {
		String continueAddEmployee = "y";
		
		while("y".equals(continueAddEmployee)) {
			Employee e = createEmployee(scanner);
			saveEmployee(e);
			continueAddEmployee = getStringInput(scanner, "Do you want to enter another? (y/n) ");
		}
		
	}

	private static Employee createEmployee(Scanner s) {
		if(DEBUG_MODE) System.err.println("creating employee");
		String first_name = getStringInput(s, "Please enter employee's first name: ");
		String last_name = getStringInput(s, "Please enter employee's last name: ");
		return new Employee(first_name, last_name);
	}

	private static void saveEmployee(Employee e) {
		if(DEBUG_MODE) System.err.println("attempting to Employee to be implimenented");
		employeeDao.save(e);
		
	}

	
    private static String getStringInput(Scanner scanner, String msg) {
        System.out.println(msg);
        return scanner.nextLine();
    }
	
	
	

}
