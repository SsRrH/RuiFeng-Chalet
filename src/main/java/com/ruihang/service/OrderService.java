package com.ruihang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruihang.entity.Orders;

public interface OrderService extends IService<Orders> {

    //orders
    public void submit(Orders orders);
}
