package com.wq.cache.controller;

import com.wq.cache.bean.Employee;
import com.wq.cache.mapper.EmployeeMapper;
import com.wq.cache.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-09 10:11
 * @since 2020-2-9 10:11
 * Spring之缓存注解
 * https://www.cnblogs.com/yuluoxingkong/p/10143810.html
 */
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/emp/{id}")
    public Employee getEmployeeById(@PathVariable("id") Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/emp/update")
    public Employee updateEmployee(Employee employee) {
        employeeService.updateEmployee(employee);
        return employee;
    }

    @GetMapping("/emp/save")
    public Employee saveEmployee(Employee employee) {
        employeeService.insertEmployee(employee);
        return employee;
    }

    @GetMapping("/emp/del/{id}")
    public String delEmployee(@PathVariable("id") Integer id) {
        int i = employeeService.delEmployeeById(id);
        System.out.println(i);
        String result = "Fail";
        if (i > 0) {
            result = "OK";
        }
        return result;
    }

}
