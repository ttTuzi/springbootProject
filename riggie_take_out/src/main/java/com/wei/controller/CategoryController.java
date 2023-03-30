package com.wei.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wei.common.R;
import com.wei.entity.Category;
import com.wei.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月06日 6:48 PM
 */
@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category: {}",category);
        categoryService.save(category);
        return R.success("新增分页成功");
    }

    /**
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page (int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //sort
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,lambdaQueryWrapper);
       return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long id){
        log.info("delete category, id:{}",id);
        categoryService.remove(id);
        return R.success("delete success");
    }

    /**
     * update by id
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("change category info");
        categoryService.updateById(category);
        return R.success("update success");

    }

    /**
     * search category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        //condition
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(lambdaQueryWrapper);
        return R.success(categoryList);
    }
}
