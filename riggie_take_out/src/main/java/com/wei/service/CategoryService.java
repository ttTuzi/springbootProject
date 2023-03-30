package com.wei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.entity.Category;
import com.wei.entity.Employee;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
