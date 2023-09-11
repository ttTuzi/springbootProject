package com.wei.dto;

import com.wei.entity.Orders;
import lombok.Data;

@Data
public class OrdersDto extends Orders {
    private String userName;
}
