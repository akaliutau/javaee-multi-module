package org.akalu.service;

import org.springframework.stereotype.Component;

import java.util.List;

import org.akalu.dao.DepartmentDAO;
import org.akalu.model.Department;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This class is intended to separate the DAO layer and the web interface.
 * All methods here are generally mirroring the methods of the appropriate 
 * DAO class 
 * 
 * @see org.akalu.dao.DepartmentDAOImpl
 * @author Alex Kalutov
 * @since Version 1.0
 */

@Component
public class DepartmentDataServiceImpl implements DepartmentDataService {
	@Autowired
	private DepartmentDAO depDao;

	@Override
	public Department getDepbyId(Long id){
		return depDao.getDepbyId(id);
	}

	@Override
	public List<Department> list(int first, int amount) {
		return depDao.list(first, amount);
	}

	@Override
	public Long addnew(Department dep) {
		return depDao.addnew(dep);
	}

	@Override
	public Boolean update(Department dep) {
		return depDao.update(dep);
	}

	@Override
	public Boolean delete(Long id) {
		return depDao.delete(id);
	}

	@Override
	public Long getDepSize() {
		return depDao.size();
	}

} 
