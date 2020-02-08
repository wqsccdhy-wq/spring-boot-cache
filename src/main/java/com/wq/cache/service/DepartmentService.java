package com.wq.cache.service;

import com.wq.cache.bean.Department;
import com.wq.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:56
 * @since 2020-2-8 16:56
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    public int insertDepartment(Department department) {
        return departmentMapper.insertDepartment(department);
    }

    public int delDepartmentById(Integer id) {
        return departmentMapper.delDepartmentById(id);
    }

    public int updateDepartment(Department department) {
        return departmentMapper.updateDepartment(department);
    }

    public Department getEmployeeById(Integer id) {
        return departmentMapper.getEmployeeById(id);
    }

}
