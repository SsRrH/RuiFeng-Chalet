package com.ruihang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruihang.common.CustomException;
import com.ruihang.entity.Category;
import com.ruihang.entity.Dish;
import com.ruihang.entity.Setmeal;
import com.ruihang.mapper.CategoryMapper;
import com.ruihang.service.CategoryService;
import com.ruihang.service.DishService;
import com.ruihang.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    //根据id删除分类，删除之前要判断
    @Override
    public void remove(Long id) {

        //如果关联了菜品，抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();//添加查询条件，根据分类id查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if(count1 > 0){
            //已经关联了菜品，抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }


        //如果关联了套餐，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();//添加查询条件，根据分类id查询
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if(count2 > 0){
            //已经关联了套餐，抛出业务异常
            throw new CustomException("当前分类下关联了套餐,不能删除");
        }

        //如果都没关联了菜品、套餐，正常删除
        super.removeById(id);
    }
}
