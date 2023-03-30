package com.wei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.common.CustomerException;
import com.wei.entity.Category;
import com.wei.entity.Dish;
import com.wei.entity.Setmeal;
import com.wei.mapper.CategoryMapper;
import com.wei.service.CategoryService;
import com.wei.service.DishService;
import com.wei.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月06日 6:46 PM
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long id) {
        //check if it relates to dish
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        long count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //throw exception
            throw new CustomerException("有关联菜品,不能删除");
        }

        //check if it relates to setmeal
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        long count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0) {
            //throw exception
            throw new CustomerException("有关联套餐,不能删除");
        }

        //delete
        super.removeById(id);
    }
}
