package org.akalu.service;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import org.akalu.dao.EmployeeDAO;
import org.akalu.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is intended to separate the DAO layer and the web interface. All
 * methods here are generally mirroring the methods of the appropriate DAO class
 * 
 * @see org.akalu.dao.EmployeeDAOImpl
 * @author Aliaksei Kaliutau
 * @since Version 1.0
 */

@Component
public class EmployeeDataServiceImpl implements EmployeeDataService {
	@Autowired
	private EmployeeDAO emplDao;

	@Override
	public Employee getEmplbyId(Long id) {
		return emplDao.getEmplbyId(id);
	}

	@Override
	public List<Employee> list(int first, int n, Long depid) {
		return emplDao.list(first, n, depid);
	}

	@Override
	public Long addnew(Employee empl) {
		return emplDao.addnew(empl);
	}

	@Override
	public Boolean update(Employee empl) {
		return emplDao.update(empl);
	}

	@Override
	public Boolean delete(Long id) {
		return emplDao.delete(id);
	}

	@Override
	public List<Employee> search1(Date dob1) {
		return emplDao.search1(dob1);
	}

	@Override
	public List<Employee> search2(Date dob1, Date dob2) {

		if (dob1.after(dob2))
			return emplDao.search2(dob2, dob1);
		return emplDao.search2(dob1, dob2);

	}

	@Override
	public Long getEmplSize(Long id) {
		return emplDao.size(id);
	}
}
