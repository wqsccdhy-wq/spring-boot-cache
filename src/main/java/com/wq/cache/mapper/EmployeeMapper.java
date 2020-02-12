package com.wq.cache.mapper;

import com.wq.cache.bean.Employee;
import org.apache.ibatis.annotations.*;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-08 16:26
 * @since 2020-2-8 16:26
 */
@Mapper
public interface EmployeeMapper {

    @Options(useGeneratedKeys = true,keyProperty = "id")
    @Insert("INSERT INTO employee(lastName,email,gender,d_id) VALUES(#{lastName},#{email},#{gender},#{dId})")
    public int insertEmployee(Employee employee);

    @Delete("DELETE FROM employee WHERE id=#{id}")
    public int delEmployeeById(Integer id);

    @Update("UPDATE employee SET lastName=#{lastName},email=#{email},gender=#{gender},d_id=#{dId} WHERE id=#{id}")
    public int updateEmployee(Employee employee);

    @Select("SELECT * FROM employee WHERE id=#{id}")
    public Employee getEmployeeById(Integer id);
}
