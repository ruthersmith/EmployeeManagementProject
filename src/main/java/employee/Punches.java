package employee;

import java.util.Date;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.joda.time.DateTime;
import org.joda.time.Period;

import system.MySystem;

@Entity
public class Punches {
	@Id @GeneratedValue
	private int id;
	private Date inPunch;
	private Date outPunch;
	
	@Transient
	private int totalHours;
	@Transient
	private int totalMinutes;
	@Transient
	private int totalTimeWorkedInMin;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getInPunch() {
		return inPunch;
	}
	public void setInPunch(Date inPunch) {
		this.inPunch = inPunch;
	}
	public Date getOutPunch() {
		return outPunch;
	}
	public void setOutPunch(Date outPunch) {
		this.outPunch = outPunch;
	}
	
	private String getInterval() {
		Period p = makePeriod();
		calculateInterval(p);
		return totalHours + " hours and " + totalMinutes + " minutes";
	}
	

	
	public int getTotalHours() {
		return totalHours;
	}
	public void setTotalHours(int totalHours) {
		this.totalHours = totalHours;
	}
	public int getTotalMinutes() {
		return totalMinutes;
	}
	public void setTotalMinutes(int totalMinutes) {
		this.totalMinutes = totalMinutes;
	}
	@Override
	public String toString() {
		return "IN: " + inPunch + " | OUT: " + outPunch + " | hours worked:  " + getInterval(); 
	}
	
	public Period makePeriod() {
		DateTime startTime = new DateTime(inPunch);
		DateTime endTime = new DateTime(outPunch);
		Period p = new Period(startTime, endTime);
		return p;
	}
	
	public void calculateInterval(Period p) {
		totalHours = p.getHours();
		totalMinutes = p.getMinutes();
		totalTimeWorkedInMin = (totalHours * 60) + totalMinutes;
	}
	
	public double calculatePay(double hourlyWage) {
		Period p = makePeriod();
		calculateInterval( p);
		double wagePerMin = hourlyWage / 60.0;
		return totalTimeWorkedInMin * wagePerMin;
	}

}
