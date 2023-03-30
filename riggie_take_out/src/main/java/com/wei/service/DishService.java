package com.wei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.dto.DishDto;
import com.wei.entity.Dish;

public interface DishService extends IService<Dish> {

    //add new dish, insert dish and flavor at same time
    public void saveWithFlavor(DishDto dishDto);
}
