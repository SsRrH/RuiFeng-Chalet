package com.ruihang.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruihang.entity.Dish;
import com.ruihang.dto.DishDto;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要操作两张表dish、dish_flavor
    public void  saveWithFlavor(DishDto dishdto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
