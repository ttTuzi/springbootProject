package com.wei.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.common.CustomerException;
import com.wei.dto.SetmealDto;
import com.wei.entity.Setmeal;
import com.wei.entity.SetmealDish;
import com.wei.mapper.SetmealMapper;
import com.wei.service.SetmealDishServer;
import com.wei.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description: TODO
 * @author: Wei Liang
 * @date: 2023年03月20日 3:09 PM
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishServer setmealDishServer;

    /**
     * add new meal, and need to save the relation with meal and dish
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        //save meal basic info
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDto.getId());
        }

        //save relations between dish and meal
        setmealDishServer.saveBatch(setmealDishes);
    }

    /**
     * delete meal and dish relationship
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //check meal status(on sale or not)
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);//SELECT * FROM user WHERE id IN (1, 2, 3...);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = (int)this.count(queryWrapper);

        if (count>0){
            throw new CustomerException("meal is on sale,cannot be delete");
        }

        //if is sale than it can be deleted,delete meal data
        this.removeByIds(ids);

        //delete relationship with dish data
        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId,ids);
        setmealDishServer.remove(queryWrapper2);
    }
}
