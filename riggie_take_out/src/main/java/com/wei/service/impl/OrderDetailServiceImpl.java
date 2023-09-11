package com.wei.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wei.entity.OrderDetail;
import com.wei.mapper.OrderDetailMapper;
import com.wei.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
