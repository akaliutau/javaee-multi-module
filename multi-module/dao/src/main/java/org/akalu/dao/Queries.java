package org.akalu.dao;

/**
 * This is a simple utility class, which holds only constant values (SQL queries
 * for DAO layer methods).
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class Queries {
	/*
	 * Departments related queries
	 */

	public static final String GET_DEPARTMENTS_LIST = "select d.department_id, d.title, avg(e.salary) as asalary "
			+ "from departments d left outer join employees e "
			+ "on d.department_id = e.department_id group by d.department_id " + "limit :amount offset :first";

	public static final String GET_DEPARTMENT = "select department_id, title from departments where department_id=:department_id";
	public static final String UPDATE_DEPARTMENT = "update departments set title=:title  where department_id=:department_id";
	public static final String COUNT_DEPARTMENTS = "select count(*) from departments";
	public static final String DELETE_DEPARTMENT = "delete from departments where department_id=:department_id";

	/*
	 * Employees related queries
	 */

	public static final String COUNT_EMPLOYEES = "select count(*) from employees e where e.department_id=:department_id";
	public static final String UPDATE_EMPLOYEE = "update employees set firstname=:firstname, secondname=:secondname, surname=:surname, "
			+ "dob=:dob, salary=:salary, department_id=:department_id " + "where employee_id=:employee_id";
	public static final String DELETE_EMPLOYEE = "delete from employees where employee_id=:employee_id";
	public static final String GET_EMPLOYEE = "select employee_id, firstname, secondname, surname, dob, salary, department_id "
			+ "from employees where employee_id=:employee_id";
	public static final String GET_EMPLOYEES_LIST = "select e.employee_id, e.firstname, e.secondname, e.surname, "
			+ "e.dob, e.salary, e.department_id " + "from employees e " + "where e.department_id=:depid "
			+ "limit :amount offset :first";

	/*
	 * Search queries
	 */

	public static final String SEACH_EMPLOYEE_1 = "select employee_id, firstname, secondname, surname, "
			+ "dob, salary, department_id " + "from employees " + "where dob=:dob";
	public static final String SEACH_EMPLOYEE_2 = "select employee_id, firstname, secondname, surname, "
			+ "dob, salary, department_id " + "from employees  " + "where dob > :dob1 and dob < :dob2";
}
