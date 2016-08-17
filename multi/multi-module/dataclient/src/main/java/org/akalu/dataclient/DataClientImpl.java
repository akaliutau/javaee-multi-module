package org.akalu.dataclient;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.akalu.model.Department;
import org.akalu.model.DobT;
import org.akalu.model.Employee;
import org.akalu.uri.DataURI;

import org.apache.log4j.Logger;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
* Implementation of the DataClient interface.
* <p>This class can be considered as a  service layer between remote DataServer and MVC infrastructure.
* It implements a transparent, updateable cache bound together with rest client. 
* Includes a set of methods to communicate with remote server and to work with data.
* 
* <p> Description of some class variables:
* <p> {@code depList}, {@code emplList}  - hold current [displayed] interconnected data
* <p> Booleans {@code isDepUpdated}, {@code isEmplUpdated} are set to true, if any changes 
* in current data are detected 
* <p> {@code foundList}  - holds resent list of found employees
* <p> {@code optlst}  - holds list of departments to choose 
* <p> {@code depOptlist}  - holds  the whole list of departments to choose 
* <p> {@code depPageMaxResults} - the maximum number of records for departments list,
* 	(before list begins paging) 
* <p> {@code emplPageMaxResults} - the maximum number of records for employees list,
* 	(before list begins paging) 
* 
* @author Alex Kalutov
* @since Version 1.0
*/

public class DataClientImpl implements DataClient{
	
	public  final String SERVER_URI;
	

	private static List<Department> depList = null;
	private static List<Employee> emplList = null;
	private static List<Employee> foundList = null;
	private static List<ListOption> optlst = null;
	private static List<Department> depOptList = null;
	
	private static Integer dep0 = 0;
	private static Integer empl0 = 0;
	private static Integer depPageMaxResults = 5;
	private static Integer emplPageMaxResults = 10;
	private static Boolean isDepUpdated = true; 
	private static Boolean isDepUpdated2 = true; 
	private static Boolean isEmplUpdated = true; 
	
	private PageDescriptor depPageDescriptor, emplPageDescriptor;




	
	
	private static RestTemplate restTemplate;
	
	private static final Logger logger = Logger.getLogger(DataClientImpl.class);
	
	/**
	 * One-parameter constructor for class DataClientImpl. 
	 * Except this, initial page descriptions are created.
	 * 
	 * @param uri - Current URI of the remote DataServer
	 */
	public DataClientImpl(String uri){
		SERVER_URI = uri;
	}

	private Long getEmplSize(Long id) {
		return getRestT().getForObject(SERVER_URI+DataURI.EMPL_DIR+"?id="+id, Long.class);
	}

	private Long getDepSize() {
		return getRestT().getForObject(SERVER_URI+DataURI.DEP_DIR, Long.class);
	}

	@Override
	public RestTemplate getRestT(){
		if (restTemplate == null) restTemplate = new RestTemplate();
		return restTemplate;
	}
	
	/**
	 * These two methods are  used for initial instantiation of page descriptions
	 * both for department and employee lists.
	 * 
	 * @see package org.akalu.webclient.PageDescriptor
	 * 
	 * 
	 */
	
	@Override
	public void init(){
		depPageDescriptor = new PageDescriptor(getDepSize(), dep0, depPageMaxResults);

	}
	
	@Override
	public void init2(Long id){
		emplPageDescriptor = new PageDescriptor(getEmplSize(id), empl0, emplPageMaxResults);

	}
	

	@Override
	public List<Department> getdepList() throws HttpClientErrorException{

		if (isDepUpdated){
			depList = updateDepList(depPageDescriptor.getFirst(),depPageDescriptor.getPageSize());
			isDepUpdated = false;
		}
		return depList;
	}
		
	
		
	private List<Department> updateDepList(Integer f, Integer n) throws HttpClientErrorException{
		 
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<LinkedHashMap> emps = 
			getRestT().getForObject(SERVER_URI+
					DataURI.GET_LIST_DEP+"?n="+ n +
					"&f=" + f, List.class);
		

		List<Department> depLst = new ArrayList<Department>();
		for(@SuppressWarnings("rawtypes") LinkedHashMap map : emps){
			Department d = new Department(
			 new Long( (Integer) map.get("id")),
			(String) map.get("title"),
			(Double) map.get("avrSalary"));
			depLst.add(d);
		}
		logger.debug("update department list: "+depLst.size()+" records"); 
		return depLst;
	}
		

	@Override
	public Department getdep(Long id)  throws HttpClientErrorException{
		
		Department d = getRestT().getForObject(
				SERVER_URI+DataURI.GET_DEP+"?id="+id, Department.class);
		return d;
	}

	@Override
	public Long addDep(Department d)  throws HttpClientErrorException{
		
		
		Long response = getRestT().postForObject(
				SERVER_URI+DataURI.UPDATE_DEP+"?new=true", d, Long.class);
		if (response != null) depPageDescriptor.incSize();

		return response;
	}

	@Override
	public Boolean updateDep(Department d)  throws HttpClientErrorException{
		
		Boolean response = getRestT().postForObject(
				SERVER_URI+DataURI.UPDATE_DEP, d, Boolean.class);
		return response;
	}

	@Override
	public Boolean deleteDep(Long id)  throws HttpClientErrorException{
		
		Boolean response = getRestT().getForObject(
				SERVER_URI+DataURI.DELETE_DEP+"?id="+id, Boolean.class);
		if (response) depPageDescriptor.decSize();
		return response;
	}
	
	/**
	 * This method should be invoked only when depList is not null
	 * 
	 */

	@Override
	public Department getDepbyIdx(Integer idx) {
		return depList.get(idx); 
	}

	@Override
	public List<Employee> getemplList(Long id)  throws HttpClientErrorException{
		if (isEmplUpdated){
			//get all lists from server
			updateEmplList(id);
			isEmplUpdated = false;
		}

		return emplList;
	}

	/**
	 * This method should be invoked only when emplList is not null
	 * 
	 */

	@Override
	public Employee getEmplbyIdx(Integer idx) {
		return emplList.get(idx); 
	}

	private void updateEmplList(Long id) throws HttpClientErrorException {
		 
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<LinkedHashMap> emps = 
			getRestT().getForObject(SERVER_URI+
					DataURI.GET_LIST_EMPL+"?id=" + id + 
					"&n="+ emplPageDescriptor.getPageSize() +
					"&f=" + emplPageDescriptor.getFirst(), List.class);
		emplList = convertMap2List(emps);
		logger.debug("update employee list: "+emplList.size()+" records"); 

	}
	
	/**
	 * Method makes conversion between department's id and department's index in displayed list. 
	 * (Note: generally index is not equal to id) 
	 * This method should be invoked only when depList is not null
	 * 
	 */


	@Override
	public Long getDepId(Integer idx) {
		return depList.get(idx).getId();
	}


	@Override
	public void setUpdateDep(boolean b) {
		isDepUpdated = b;
		isDepUpdated2 = b;
		optlst = null;
	}

	/**
	 * Method makes conversion between employee's id and employee's index in displayed list. 
	 * (Note: generally index is not equal to id) 
	 * This method should be invoked only when emplList is not null
	 */

	@Override
	public Long getEmplId(Integer idx) {
		return emplList.get(idx).getId();
	}


	@Override
	public void setUpdateEmpl(boolean b) {
		isEmplUpdated = b;
	}


	@Override
	public Employee getEmpl(Long id)  throws HttpClientErrorException{
		
		Employee e = getRestT().getForObject(
				SERVER_URI+DataURI.GET_EMPL+"?id="+id, Employee.class);
		return e;
	}

	@Override
	public Long addEmpl(Employee em)  throws HttpClientErrorException{
		
		
		Long response = getRestT().postForObject(
				SERVER_URI+DataURI.UPDATE_EMPL+"?new=true", em, Long.class);

		if (response != null) emplPageDescriptor.incSize();

		return response;
	}

	@Override
	public Boolean updateEmpl(Employee em)  throws HttpClientErrorException{
		
		Boolean response = getRestT().postForObject(
				SERVER_URI+DataURI.UPDATE_EMPL, em, Boolean.class);
		return response;
	}

	@Override
	public Boolean deleteEmpl(Long id)  throws HttpClientErrorException{
		
		Boolean response = getRestT().getForObject(
				SERVER_URI+DataURI.DELETE_EMPL+"?id="+id, Boolean.class);
		if (response) emplPageDescriptor.decSize();

		return response;
	}

	/**
	 * Method generates a list of possible departments to choose from.
	 * @see  org.akalu.webclient.ListOption
	 * @see  org.akalu.webclient.ListOptions
	 * 
	 * @return List<ListOption> - list of ListOption objects
	 */

	@Override
	public List<ListOption> getOptList() {

	if (isDepUpdated2){
		depOptList = updateDepList(dep0,100); // get the whole list
		isDepUpdated2 = false;
	}

	if (optlst == null){
		optlst = new ArrayList<ListOption>();
		for (Department d: depOptList){
			optlst.add( new ListOption(d.getId(), d.getTitle()));			
		}
	}
		return optlst;
	}



	@Override
	public List<Employee> getFoundList(Date dob1)  throws HttpClientErrorException{
		
		 
		
		DobT db2 = new DobT(dob1);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<LinkedHashMap> emps = 
			getRestT().postForObject(SERVER_URI+
					DataURI.SEARCH_EMPL,db2, List.class);
		foundList = convertMap2List(emps);
		logger.debug("search by 1 date: "+foundList.size()+" results"); 

		return foundList;
	}

	@Override
	public List<Employee> getFoundList(Date dob1,Date dob2)  throws HttpClientErrorException{
		
		 
		DobT db2 = new DobT(dob1,dob2);
		@SuppressWarnings({ "rawtypes", "unchecked" })
		List<LinkedHashMap> emps = 
			getRestT().postForObject(SERVER_URI+
					DataURI.SEARCH_EMPL, db2, List.class);
		foundList = convertMap2List(emps);
		logger.debug("search by 2 dates: "+foundList.size()+" results"); 
		return foundList;
	}
	
	/**
	 * Utility method for conversion
	 * 
	 * @param emps - list of maps, containing paired values retrieved from data in JSON format
	 * @return List of Employee records ready to display 
	 */

	private	List<Employee> convertMap2List(
			@SuppressWarnings("rawtypes") List<LinkedHashMap> emps) {
		List<Employee> outList = new ArrayList<Employee>();
		for(@SuppressWarnings("rawtypes") LinkedHashMap map : emps){
			Employee e = new Employee(
			 new Long( (Integer) map.get("id")),
			(String) map.get("firstName"),
			(String) map.get("secondName"),
			(String) map.get("surname"),
			Date.from(Instant.ofEpochMilli((Long) map.get("dob"))),
			 new Long( (Integer) map.get("depId")),
			(Double) map.get("salary"));
		outList.add(e);
		}
		return outList;
	}

	/**
	 * Getter methods for paginator 
	 * 
	 * 
	 */

	@Override
	public PageDescriptor getDepPageDescriptor() {
		return depPageDescriptor;
	}
	
	@Override
	public PageDescriptor getEmplPageDescriptor() {
		return emplPageDescriptor;
	}

	/**
	 * These three methods was created for testing purposes 
	 * 
	 * @see package org.akalu.webclient.test.DataClientTest
	 * 
	 */
	@Override
	public void setDepPageDescriptor(PageDescriptor depPageDescriptor) {
		this.depPageDescriptor = depPageDescriptor;
	}
	
	@Override
	public void setEmplPageDescriptor(PageDescriptor emplPageDescriptor) {
		this.emplPageDescriptor = emplPageDescriptor;
	}
	

}
