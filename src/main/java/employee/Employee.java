package employee;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;



@Entity
public class Employee {
	
	@Id @GeneratedValue
	private int id;
	private String first_name;
	private String last_name;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "employee_id")
	private Set<Punches> myPunches = new TreeSet<Punches>();
	
	
	public Employee() {/* required empty constructor */	}

	public Employee(String first_name, String last_name) {
		this.first_name = first_name;
		this.last_name = last_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public Set<Punches> getMyPunches() {
		return myPunches;
	}

	public void setMyPunches(Set<Punches> myPunches) {
		this.myPunches = myPunches;
	}

	public boolean isReadyToSave() {
		return !first_name.isEmpty()  && !last_name.isEmpty();
	}

	public void punchIn() {
		Punches p = new Punches();
		p.setInPunch(new Date());
		this.getMyPunches().add(p);
		
	}

	public void generateReport() {
		System.out.println(id + " | " + first_name + " | " + last_name);

		if(myPunches.isEmpty()) {
			System.out.println("No Punching record for " + first_name);
			return;
		}
		
		for(Punches p : myPunches) {
			System.out.println(p);
		}
	}

	public double calculateWage(double hourlyWage) {
		double total = 0;
		for(Punches p : myPunches) {
			total += p.calculatePay(hourlyWage);
		}
		return total;
	}






}
