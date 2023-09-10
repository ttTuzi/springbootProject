package com.wei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.entity.ShoppingCart;
import com.wei.mapper.ShoppingCartMapper;
import com.wei.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
