package com.wei.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wei.common.R;
import com.wei.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
