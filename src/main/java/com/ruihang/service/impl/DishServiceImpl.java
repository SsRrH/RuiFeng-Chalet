package com.ruihang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruihang.dto.DishDto;
import com.ruihang.entity.Dish;
import com.ruihang.entity.DishFlavor;
import com.ruihang.mapper.DishMapper;
import com.ruihang.service.DishFlavorService;
import com.ruihang.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    //新增菜品，同时保存口味数据
    @Transactional
    public void saveWithFlavor(DishDto dishdto) {
        //保存菜品的基本信息到菜品表dish
        this.save(dishdto);

        // 获取菜品的id，否则下方的getFlavors得不到dishId，只有name和flavors，只添加了名字和忌口，不知道是哪个菜品。
        Long dishId = dishdto.getId();

        //菜品口味
        List<DishFlavor> flavors = dishdto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到菜品口味表dish_flavor
        dishFlavorService.saveBatch(flavors);
        //dishFlavorService.saveBatch(dishdto.getFlavors());


    }

    //根据id查询菜品信息和对应的口味信息
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        //查询菜品口味信息，从dish_flavor表查询0
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(flavors);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {

        //更新dish表信息
        this.updateById(dishDto);

        //清理当前菜品对应的口味数据---delete操作
        LambdaQueryWrapper<DishFlavor> queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        //添加当前提交的口味信息----insert操作

        List<DishFlavor> flavors = dishDto.getFlavors();

        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavors);
    }
}
