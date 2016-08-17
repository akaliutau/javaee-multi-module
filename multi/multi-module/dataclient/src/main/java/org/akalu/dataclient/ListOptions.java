package org.akalu.dataclient;

import java.util.List;

/**
* This class is intended to hold a list of options tied to
* OptionList component on the page. Additional variable 
* {@code selected} holds a selected option.

* 
* @see org.akalu.dataclient.ListOption
* 
* @author Alex Kalutov
* @since Version 1.0
*/


public class ListOptions {
	private List<ListOption> list;
	private Long selected;
	
	public ListOptions(List<ListOption> optList, Long selectedDepId) {
		list = optList;
		selected = selectedDepId;
	}
	public List<ListOption> getList() {
		return list;
	}
	public void setList(List<ListOption> list) {
		this.list = list;
	}
	public Long getSelected() {
		return selected;
	}
	public void setSelected(Long selected) {
		this.selected = selected;
	}

}
