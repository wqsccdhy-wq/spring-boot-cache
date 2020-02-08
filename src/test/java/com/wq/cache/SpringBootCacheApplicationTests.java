package com.wq.cache;

import com.wq.cache.bean.Department;
import com.wq.cache.bean.Employee;
import com.wq.cache.mapper.DepartmentMapper;
import com.wq.cache.mapper.EmployeeMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootCacheApplicationTests {

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    @Test
    void contextLoads() {

//        Employee employee = new Employee();
//        employee.setLastName("李四");
//        employee.setEmail("lishi@126.com");
//        employee.setGender(1);
//        employee.setdId(1);
//        employeeMapper.insertEmployee(employee);
//        Employee employee = employeeMapper.getEmployeeById(4);
////        System.out.println(employee);
        Department department = new Department();
        department.setDepartmentName("开发部");
        departmentMapper.insertDepartment(department);

    }

}
