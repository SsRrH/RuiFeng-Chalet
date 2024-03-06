package com.ruihang.dto;

import com.ruihang.entity.Setmeal;
import com.ruihang.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
