package com.wei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.entity.Employee;

public interface EmployeeService extends IService<Employee> {
    Employee FindByUsername(String username);
}
