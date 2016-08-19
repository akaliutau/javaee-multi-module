package org.akalu.dataclient;

import java.util.Date;
import java.util.List;

import org.akalu.model.Department;
import org.akalu.model.Employee;
import org.springframework.web.client.RestTemplate;

/**
 * Interface to DataClientImpl class
 * 
 * @see org.akalu.dataclient.DataClientImpl
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public interface DataClient {
	public List<Department> getdepList();

	public Department getdep(Long id);

	public Long addDep(Department dep);

	public Boolean updateDep(Department dep);

	public Boolean deleteDep(Long id);

	// can be also used as util class
	public Department getDepbyIdx(Integer idx);

	public List<Employee> getemplList(Long selectedDepId);

	public Employee getEmpl(Long id);

	public Long addEmpl(Employee em);

	public Boolean updateEmpl(Employee em);

	public Boolean deleteEmpl(Long id);

	public Employee getEmplbyIdx(Integer idx);

	public Long getDepId(Integer idx);

	public void setUpdateDep(boolean b);

	public Long getEmplId(Integer idx);

	public void setUpdateEmpl(boolean b);

	public List<ListOption> getOptList();

	public List<Employee> getFoundList(Date dob1);

	public List<Employee> getFoundList(Date dob1, Date dob2);

	public RestTemplate getRestTemplate();

	public PageDescriptor getEmplPageDescriptor();

	public PageDescriptor getDepPageDescriptor();

	public void init();

	public void init2(Long id);

	void setDepPageDescriptor(PageDescriptor depPageDescriptor);

	void setEmplPageDescriptor(PageDescriptor emplPageDescriptor);

}
