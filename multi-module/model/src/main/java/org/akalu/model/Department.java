
package org.akalu.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * This is a model class in presentation tier for 1 department record.
 * <p>
 * The validator uses annotations to determine a set of constraints that a
 * filled form must comply to.
 * <p>
 * Displayed messages are taken from file i18n/message_en.properties
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

public class Department implements Serializable {

	private static final long serialVersionUID = 2L;

	private Long id;

	@NotNull
	@Size(min = 3, max = 128)
	public String title;

	public Double avrSalary;

	public Department() {
	}

	public Department(Long id, String t, Double asalary) {
		this.id = id;
		this.avrSalary = asalary;
		this.title = t;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Double getAvrSalary() {
		return avrSalary;
	}

	public void setAvrSalary(Double avrSalary) {
		this.avrSalary = avrSalary;
	}

}
