package com.wq.cache.service;

import com.wq.cache.bean.Department;
import com.wq.cache.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CachePut(cacheNames = "{dept}",key = "#result.id")
    public Department insertDepartment(Department department) {
        departmentMapper.insertDepartment(department);
        return department;
    }

    @CacheEvict(cacheNames = "{dept}",key = "#id")
    public int delDepartmentById(Integer id) {
        return departmentMapper.delDepartmentById(id);
    }

    @CachePut(cacheNames = "{dept}",key = "#department.id",condition = "#result != null ")
    public Department updateDepartment(Department department) {
        int num = departmentMapper.updateDepartment(department);
        if (num > 0){
            return department;
        }
        return null;
    }

    @Cacheable(cacheNames = "{dept}",key = "#id")
    public Department getDeptById(Integer id) {
        System.out.println("查询部门:" + id);
        return departmentMapper.getEmployeeById(id);
    }

}
