package com.wq.cache.mapper;

import org.apache.ibatis.annotations.*;

import com.wq.cache.bean.Department;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:48
 * @since 2020-2-8 16:48
 */
public interface DepartmentMapper {

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("INSERT INTO department(departmentName) VALUES(#{departmentName})")
    public int insertDepartment(Department department);

    @Delete("DELETE FROM department WHERE id=#{id}")
    public int delDepartmentById(Integer id);

    @Update("UPDATE department SET departmentName=#{departmentName} WHERE id=#{id}")
    public int updateDepartment(Department department);

    @Select("SELECT * FROM department WHERE id=#{id}")
    public Department getEmployeeById(Integer id);
}
