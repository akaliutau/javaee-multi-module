package org.akalu.dataclient;

/**
 * This class is intended to hold one option to choose from the OptionList
 * component on the page.
 * 
 * @see org.akalu.dataclient.ListOptions
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

public class ListOption {
	private Long id;
	private String title;

	public ListOption(Long long1, String title) {
		this.id = long1;
		this.title = title;
	}

	public ListOption() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String gettitle() {
		return title;
	}

	public void settitle(String title) {
		this.title = title;
	}
}
