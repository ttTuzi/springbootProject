package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年02月27日 1:03 PM
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);
}
