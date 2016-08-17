package org.akalu.model;

import java.util.Date;

public class DobT  {

//private static final long serialVersionUID = 3L; 

//@JsonSerialize(using=DateSerializer.class)
	private Date dob1;

//@JsonSerialize(using=DateSerializer.class)
	private Date dob2;
	
public DobT(){
	
}
	
public DobT(Date dob1){
	this.dob1 = dob1;

}
	public DobT(Date dob1, Date dob2){
		this.dob1 = dob1;
		this.dob2 = dob2;
		
	}
	
	public Date getDob1() {
		return dob1;
	}
	public void setDob1(Date dob1) {
		this.dob1 = dob1;
	}
	public Date getDob2() {
		return dob2;
	}
	public void setDob2(Date dob2) {
		this.dob2 = dob2;
	}
	

}
