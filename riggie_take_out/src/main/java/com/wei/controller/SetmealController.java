package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.common.R;
import com.wei.dto.SetmealDto;
import com.wei.entity.Category;
import com.wei.entity.Setmeal;
import com.wei.service.CategoryService;
import com.wei.service.SetmealDishServer;
import com.wei.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年04月04日 10:23 PM
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealDishServer setmealDishServer;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping()
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("add meal successfully");
    }


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(setmealPage);

        //copy to dto,
        //so,can get full info
        Page<SetmealDto> pageInfo = new Page<>(page,pageSize);
        BeanUtils.copyProperties(setmealPage, pageInfo,"records");

        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            //分类id
            Long categoryId = item.getCategoryId();
            //根据分类id查询分类对象
            Category category = categoryService.getById(categoryId);
            if(category != null){
                //分类名称
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        pageInfo.setRecords(list);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids: {}",ids);
        setmealService.removeWithDish(ids);
        return R.success("deletion success");
    }

    /**
     * 查询套餐
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);

        return R.success(list);
    }

    @PostMapping("/test")
    public R<String> test(){
        return null;
    }

}
