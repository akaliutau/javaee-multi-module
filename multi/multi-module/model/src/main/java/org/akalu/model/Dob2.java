package org.akalu.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

public class Dob2 {
	
    @DateTimeFormat(pattern="yyyy/MM/dd")
    @NotNull @Past
	private Date dob1;

    @DateTimeFormat(pattern="yyyy/MM/dd")
    @NotNull @Past
	private Date dob2;

	public Dob2(){
		
	}
	
	public Date getDob1() {
		 return dob1;
		 }

	public void setDob1(Date dob) {
			this.dob1 = dob;
		}

	public Date getDob2() {
		 return dob2;
		 }

	public void setDob2(Date dob) {
			this.dob2 = dob;
		}
	
	

}
