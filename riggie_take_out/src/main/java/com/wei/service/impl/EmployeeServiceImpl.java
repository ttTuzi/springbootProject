package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.entity.Employee;
import com.wei.mapper.EmployeeMapper;
import com.wei.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年02月27日 1:09 PM
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
