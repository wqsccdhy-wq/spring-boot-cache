package com.wq.cache.controller;

import com.wq.cache.bean.Department;
import com.wq.cache.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:58
 * @since 2020-2-8 16:58
 */
@RestController
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/dept/{id}")
    public Department getDepById(@PathVariable("id") Integer id){
        Department employee = departmentService.getEmployeeById(id);
        System.out.println(employee.getDepartmentName());
        return employee;
    }
}
