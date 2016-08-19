package org.akalu.model;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * This is a model class in presentation tier for date form, consisted from 1
 * date field and used on the search page.
 * <p>
 * The validator uses annotations to determine a set of constraints that a
 * filled form must comply to.
 * <p>
 * Displayed messages are taken from file i18n/message_en.properties
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class Dob {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	@NotNull
	@Past
	private Date dob;

	public Dob() {

	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

}
