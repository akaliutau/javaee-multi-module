package org.akalu.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

public class Dob {
	
    @DateTimeFormat(pattern="yyyy/MM/dd")
    @NotNull @Past
	private Date dob;

	public Dob(){
		
	}
	
	public Date getDob() {
		 return dob;
		 }

	public void setDob(Date dob) {
			this.dob = dob;
		}

}
