package com.wei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.dto.SetmealDto;
import com.wei.entity.Setmeal;
import com.wei.mapper.SetmealMapper;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    /**
     *
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * delete meal and dish relationship
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
