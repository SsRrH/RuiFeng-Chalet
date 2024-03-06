package com.ruihang.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruihang.common.R;
import com.ruihang.entity.Employee;
import com.ruihang.entity.OrderDetail;
import com.ruihang.entity.Orders;
import com.ruihang.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    //用户下单orders
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("下单成功");
    }


    //分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {

        //构造分页构造器
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Orders::getNumber);
        //查询
        orderService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);

    }


    //修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Orders orders){

        orderService.updateById(orders);
        return R.success("开始派送");
    }
}