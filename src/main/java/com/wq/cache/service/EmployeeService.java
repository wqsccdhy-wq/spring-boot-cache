package com.wq.cache.service;

import com.wq.cache.bean.Employee;
import com.wq.cache.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @CachePut(value = "{emp}", key = "#result.id")
    public Employee insertEmployee(Employee employee) {
        int num = employeeMapper.insertEmployee(employee);
        System.out.println("保存Emp:" + employee.getdId());
        return employee;
    }

    /**
     * @CacheEvict
     * 是用来标注在需要清除缓存元素的方法或类上的，默认是在方法执行成功之后，清除缓存
     * @param id
     * @return
     */
    @CacheEvict(value = "{emp}", key = "#id")
    public int delEmployeeById(Integer id) {
        return employeeMapper.delEmployeeById(id);
    }

    /**
     * @CachePut
     * 执行方法前不会检查缓存中是否存在，而是在每次执行完方法后，将执行结果放到缓存中
     * @param employee
     * @return
     */
    @CachePut(value = "{emp}", key = "#employee.id")
    public Employee updateEmployee(Employee employee) {
        employeeMapper.updateEmployee(employee);
        return employee;
    }

    /**
     * @Cacheable
     * 查询时，先在缓存中查找值是否存在，如果存在则返回结果，如果不存在则执行方法查询数据库，将查询的结果存入缓存
     *
     * @param id id
     * @return Employee
     */
    @Cacheable(value = "{emp}", key = "#id")
    public Employee getEmployeeById(Integer id) {
        System.out.println("查询第" + id + "号员工");
        return employeeMapper.getEmployeeById(id);
    }

}
