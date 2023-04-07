package com.wei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.dto.DishDto;
import com.wei.entity.Dish;
import com.wei.entity.DishFlavor;
import com.wei.mapper.DishMapper;
import com.wei.service.DishFlavorService;
import com.wei.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月20日 3:06 PM
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;


    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        dishFlavorService.saveBatch(flavors);

    }

    /**
     * according to id, search for dish and flavor info
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //get dish by id
        Dish dish = this.getById(id);

        //copy dish and paste to dishDto
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);

        //get flavors for dish
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        //copy flavor to dishDto
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateByIdWithFlavor(DishDto dishDto) {
        //update dish basic info
        log.info("hereservice");
        this.updateById(dishDto);

        //remove all the flavor
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
        dishFlavorService.remove(queryWrapper);

        //add new flavor, and assign dish id to each flavor
        List<DishFlavor> flavors = dishDto.getFlavors();
        Long id = dishDto.getId();
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(id);
        }
        log.info("hereservice");
        dishFlavorService.saveBatch(flavors);
        //更新dish表基本信息
//        this.updateById(dishDto);
//
//        //清理当前菜品对应口味数据---dish_flavor表的delete操作
//        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
//        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());
//
//        dishFlavorService.remove(queryWrapper);
//
//        //添加当前提交过来的口味数据---dish_flavor表的insert操作
//        List<DishFlavor> flavors = dishDto.getFlavors();
//
//        flavors = flavors.stream().map((item) -> {
//            item.setDishId(dishDto.getId());
//            return item;
//        }).collect(Collectors.toList());
//
//        dishFlavorService.saveBatch(flavors);
   }
}
