
package org.akalu.model;

import java.util.Date;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable; 
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
/**
 * 
 *  This is a model class in  presentation tier for 1 employee record.
 *  <p>The validator uses annotations to determine a set of constraints 
 *  that a filled from must comply to. 
 *  <p>Displayed messages are taken from file  i18n/message_en.properties
 *  
 * @author Alex Kalutov
 * @since Version 1.0
 */
public class Employee implements Serializable {

private static final long serialVersionUID = 1L; 

	private Long id;
	
	@NotNull @Size(min=2, max=30) 
    private String firstName;
	
	@NotNull @Size(min=2, max=30) 
    private String secondName;
	
	@NotNull @Size(min=1, max=50) 
    private String surname;
	
	@NotNull @Min(0) @Max(1000000)
    private Double salary;
    
    @DateTimeFormat(pattern="yyyy/MM/dd")
    @NotNull @Past
    @JsonSerialize(using=DateSerializer.class)
    private Date dob;
    
    @NotNull 
	private Long depId;

	
public Employee(){
}



public Employee(Long id, String n1, String n2, String n3, Date dob, Long dep,
		Double salary) {
	this.id = id;
	this.firstName = n1;
	this.secondName = n2;
	this.surname = n3;
	this.dob = dob;
	this.depId = dep;
	this.salary = salary;
}

public Long getId() {
 return id;
 }

 public void setId(Long id) {
	this.id  = id;
}

 public String getFirstName() {
 return firstName;
 }

 public void setFirstName(String firstName) {
	this.firstName = firstName;
}

 public String getSecondName() {
 return secondName;
 }

 public void setSecondName(String secondName) {
	this.secondName = secondName;
}

 public String getSurname() {
 return surname;
 }

 public void setSurname(String surname) {
	this.surname = surname;
}

 
 public Date getDob() {
 return dob;
 }

 public void setDob(Date dob) {
	this.dob = dob;
}

 public Long getDepId() {
 return depId;
 }

 public void setDepId(Long dep) {
	this.depId = dep;
}
 
public Double getSalary() {
 return salary;
 }

 public void setSalary(Double salary) {
	this.salary = salary;
}

	
}
