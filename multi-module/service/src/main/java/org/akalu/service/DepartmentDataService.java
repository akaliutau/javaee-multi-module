package org.akalu.service;

import java.util.List;

import org.akalu.model.Department;

public interface DepartmentDataService {
	public List<Department> list(int first, int amount);

	public Department getDepbyId(Long id);

	public Long addnew(Department dep);

	public Boolean update(Department dep);

	public Boolean delete(Long id);

	public Long getDepSize();

}
