package org.akalu.dataclient;

/**
 * A POJO to hold some parameters about current partial [departments|employees] list,
 * in particular:
 * <p> {@code totalSize} - the total number of records in the list (got from db).
 * <p> {@code first} - index of the first record in the list, starting from 0.
 * <p> {@code pageSize} - amount of records to display.
 * <p> Class PageDescriptor is used for creating a simple paginator for [too long] lists
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class PageDescriptor {
	
	Long totalSize;
	Integer first;
	Integer pageSize;
	
	Boolean isNext;
	Boolean isPrevious;
	Boolean isVisible;
	
	public PageDescriptor(Long totalSize, Integer first, Integer pageSize) {
		this.totalSize = totalSize;
		this.first = first;
		this.pageSize = pageSize;
		this.isNext = (totalSize - (first+pageSize) > 0)?true:false;
		this.isPrevious = (first == 0)?false:true;
	}

	
	/**
	 * Some predicative functions to determine existence of the next|previous page
	 * 
	 */
	
	public Boolean goNext(){
		if (isNext){
			first+=pageSize;
			updateBooleans();
			return true;
		}
		return false;
	}
	
	public Boolean goPrevious(){
		if (isPrevious){
			first-=pageSize;
			if (first < 0) first = 0;
			updateBooleans();

			return true;
		}
		return false;
	}
	
	private void updateBooleans(){
		isNext = (totalSize - (first+pageSize) > 0)?true:false;
		isPrevious = (first >= pageSize)?true:false;
	}
	
	/**
	 * Some utility functions; these methods should be  usually invoked 
	 * after add/delete operations (successful, of course).
	 * 
	 */

	public void decSize(){
		if (totalSize >0) totalSize--;
		if (first > totalSize) first-=pageSize;
		updateBooleans();

	}
	
	public void incSize(){
		totalSize++;
		updateBooleans();

	}
	
	/**
	 * 
	 * Standard getter and setter methods
	 * 
	 */
	public Long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Long totalSize) {
		this.totalSize = totalSize;
		updateBooleans();

	}
	public Integer getFirst() {
		return first;
	}
	public void setFirst(Integer first) {
		this.first = first;
		updateBooleans();

	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
		updateBooleans();

	}

	public Boolean getIsNext() {
		return isNext;
	}
	public Boolean getIsPrevious() {
		return isPrevious;
	}
	
	public Boolean getIsVisible() {
		return isPrevious || isNext;
	}
	
	

}
