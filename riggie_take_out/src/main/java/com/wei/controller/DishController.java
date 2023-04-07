package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.common.R;
import com.wei.dto.DishDto;
import com.wei.entity.Category;
import com.wei.entity.Dish;
import com.wei.entity.DishFlavor;
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

    /**
     * according to id, search for dish and flavor info
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * update dish
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update (@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        //dishService.updateById(dishDto);
        //dishService.saveWithFlavor(dishDto);
        dishService.updateById(dishDto);

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
        return R.success("dish added success");
    }

    /**
     * get dishes by different category
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId,dish.getCategoryId());
        //1 is for status, means on sale
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        return R.success(list);
    }

}
