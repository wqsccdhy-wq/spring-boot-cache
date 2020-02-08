package com.wq.cache.service;

import com.wq.cache.bean.Employee;
import com.wq.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:53
 * @since 2020-2-8 16:53
 */
@Service
public class EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    public int insertEmployee(Employee employee) {
        int num = employeeMapper.insertEmployee(employee);
        return num;
    }

    public int delEmployeeById(Integer id) {
        return employeeMapper.delEmployeeById(id);
    }

    public int updateEmployee(Employee employee) {
        return employeeMapper.updateEmployee(employee);
    }

    public Employee getEmployeeById(Integer id) {
        return employeeMapper.getEmployeeById(id);
    }

}
