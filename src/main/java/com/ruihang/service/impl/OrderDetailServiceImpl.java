package com.ruihang.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruihang.mapper.OrderDetailMapper;
import com.ruihang.service.OrderDetailService;
import com.ruihang.entity.OrderDetail;

import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {

}