package com.wei.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wei.entity.Orders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderService extends IService<Orders> {
    void submit(Orders orders);
}
