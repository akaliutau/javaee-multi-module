package org.akalu.dataserver;

import java.util.List;

import org.akalu.model.Department;
import org.akalu.model.DobParamsSearch;
import org.akalu.model.Employee;
import org.akalu.service.DepartmentDataService;
import org.akalu.service.EmployeeDataService;
import org.akalu.uri.DataURI;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This is a web service class which will be exposed for rest service. The
 * annotation @RequestMapping is used at method level for Rest Web Service URL
 * mapping.
 * 
 * 
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@RestController
public class DataController {
	@Autowired
	private DepartmentDataService depDataService;

	@Autowired
	private EmployeeDataService emplDataService;

	private static final Logger logger = Logger.getLogger(DataController.class);

	/**
	 * 
	 * Method returns 1 requested department record.
	 * <p>
	 * In the request parameters attribute {@code defaultValue} will assign a
	 * default value for parameter which value is not available in request.
	 * 
	 * @param id
	 * @return object of Department.class
	 */

	@RequestMapping(value = DataURI.DEPARTMENT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Department getDepartmentDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

		logger.debug("GET for Department with id=" + id);
		Department p = depDataService.getDepbyId(id);
		return p;
	}

	/**
	 * 
	 * Method returns requested list of departments.
	 * <p>
	 * In the request parameters attribute {@code defaultValue} will assign a
	 * default value for parameter which value is not available in request.
	 * 
	 * @param first
	 *            - defines the index of the first departments record in
	 *            resulted list
	 * @param n
	 *            - defines the maximum number of records to return
	 * @return List<Department>
	 */
	@RequestMapping(value = DataURI.DEPARTMENTS, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Department> getDepartmentsList(
			@RequestParam(value = "f", required = false, defaultValue = "0") Integer first,
			@RequestParam(value = "n", required = false, defaultValue = "10") Integer n) {

		logger.debug("GET for list of Departments with index in range " + first + "-" + n);

		List<Department> p = depDataService.list(first, n);
		return p;
	}

	/**
	 * 
	 * Method returns requested list of employees.
	 * 
	 * @param first
	 *            - defines the value of MIN(id) of the first employee in
	 *            resulted list
	 * @param n
	 *            - defines the id of department for list of employees to return
	 *            from.
	 * @return List<Employee>
	 */

	@RequestMapping(value = DataURI.EMPLOYEES, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Employee> getEmployeesList(
			@RequestParam(value = "f", required = false, defaultValue = "0") Integer first,
			@RequestParam(value = "n", required = false, defaultValue = "10") Integer n,
			@RequestParam(value = "id", required = false, defaultValue = "1") Long id) {

		logger.debug("GET for list of Employees from department with id=" + id);

		List<Employee> p = emplDataService.list(first, n, id);

		return p;
	}

	/**
	 * 
	 * Main search method.
	 * 
	 * <p>
	 * d - contains a pair of Date variables which define search criteria.
	 * 
	 * @return List<Employee> - list of employees
	 */
	@RequestMapping(value = DataURI.SEARCH, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Employee> getSearchList(@RequestBody DobParamsSearch d) {

		logger.debug("PUT for list of Employees with dob =" + d.toString());

		if (d.getDob2() == null)
			return emplDataService.search1(d.getDob1());
		return emplDataService.search2(d.getDob1(), d.getDob2());
	}

	/**
	 * 
	 * Method used to delete a single department record.
	 * 
	 * @param isNew
	 *            - boolean which defines branch of code to execute (add or
	 *            update)
	 * @param dep
	 *            - contains updated data
	 * @return true if success
	 */

	@RequestMapping(value = DataURI.DEPARTMENT, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteDepartment(@RequestParam(value = "id") Long id) {

		logger.debug("DELETE department with id=" + id);

		return depDataService.delete(id);
	}

	/**
	 * 
	 * Method used to add a single department record.
	 * 
	 * @param dep
	 *            - contains updated data
	 * @return id of the newly added department
	 */

	@RequestMapping(value = DataURI.DEPARTMENT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long addDepartment(@RequestBody Department dep) {

		logger.debug("POST to add department");
		return depDataService.addnew(dep);
	}

	/**
	 * 
	 * Method used to update a single department record.
	 * 
	 * @param dep
	 *            - contains updated data
	 * @return id of updated department
	 */

	@RequestMapping(value = DataURI.DEPARTMENT, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long updateDepartment(@RequestBody Department dep) {

		logger.debug("PUT to update department with id=" + dep.getId());
		depDataService.update(dep);
		return dep.getId();
	}

	@RequestMapping(value = DataURI.EMPLOYEE, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Employee getEmployeeDetail(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

		logger.debug("GET for Employee with id=" + id);

		Employee p = emplDataService.getEmplbyId(id);
		return p;
	}

	@RequestMapping(value = DataURI.EMPLOYEE, method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Boolean deleteEmployee(@RequestParam(value = "id") Long id) {

		logger.debug("DELETE Employee with id=" + id);

		return emplDataService.delete(id);

	}

	/**
	 * 
	 * Method used to add a single employee record.
	 * 
	 * @param empl
	 *            - contains updated data
	 * @return id of newly added employee
	 */

	@RequestMapping(value = DataURI.EMPLOYEE, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long addEmployee(@RequestBody Employee empl) {

		logger.debug("POST to add Employee");

		return emplDataService.addnew(empl);
	}

	/**
	 * 
	 * Method used to update a single employee record.
	 * 
	 * @param empl
	 *            - contains updated data
	 * @return id of newly updated employee
	 */

	@RequestMapping(value = DataURI.EMPLOYEE, method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long updateEmployee(@RequestBody Employee empl) {

		logger.debug("PUT to update Employee with id=" + empl.getId());
		emplDataService.update(empl);
		return empl.getId();
	}

	@RequestMapping(value = DataURI.DEPARTMENTS_INFO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getDepartmentListSize() {

		logger.debug("GET for Departments size");
		Long s = depDataService.getDepSize();
		return s;
	}

	@RequestMapping(value = DataURI.EMPLOYEES_INFO, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Long getEmployeesListSize(@RequestParam(value = "id", required = false, defaultValue = "0") Long id) {

		logger.debug("GET for Employees size from dep with id=" + id);
		Long s = emplDataService.getEmplSize(id);
		return s;
	}

}
