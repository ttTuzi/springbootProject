package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.common.R;
import com.wei.dto.DishDto;
import com.wei.entity.Category;
import com.wei.entity.Dish;
import com.wei.service.CategoryService;
import com.wei.service.DishFlavorService;
import com.wei.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月22日 8:22 PM
 */
@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * add new dish
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save (@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("add dish success");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> pageDtoInfo = new Page<>();
        //condition structure
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();

        //search by name
        queryWrapper.like(name !=null,Dish::getName,name);

        //order by update time
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        //show the page
        dishService.page(pageInfo,queryWrapper);

        BeanUtils.copyProperties(pageInfo,pageDtoInfo,"records");
        List<Dish> records = pageInfo.getRecords();

        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category!=null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());


        pageDtoInfo.setRecords(list);
//        dishService.page(pageInfo,queryWrapper);

        return R.success(pageDtoInfo);
    }

}
