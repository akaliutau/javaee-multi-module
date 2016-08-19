package org.akalu.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * This is a model class in presentation tier for date form, consisted from 2
 * date fields and used on the search page.
 * <p>
 * The validator uses annotations to determine a set of constraints that a
 * filled form must comply to.
 * <p>
 * Displayed messages are taken from file i18n/message_en.properties
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */
public class Dob2 {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@Past
	private Date dob1;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@Past
	private Date dob2;

	public Dob2() {

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
