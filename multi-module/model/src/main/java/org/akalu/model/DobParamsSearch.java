package org.akalu.model;

import java.util.Date;

import java.io.Serializable;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

/**
 * This class is used as parameters holder for search methods, in particular
 * date of birth, boundaries for date of birth , etc.
 * 
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class DobParamsSearch implements Serializable  {

	private static final long serialVersionUID = 3L;

	@JsonSerialize(using = DateSerializer.class)
	private Date dob1;

	@JsonSerialize(using = DateSerializer.class)
	private Date dob2;

	public DobParamsSearch() {

	}

	public DobParamsSearch(Date dob1) {
		this.dob1 = dob1;

	}

	public DobParamsSearch(Date dob1, Date dob2) {
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
