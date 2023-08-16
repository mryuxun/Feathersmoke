package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import qdb.qdl.common.CustomException;
import qdb.qdl.entity.Category;
import qdb.qdl.entity.Dish;
import qdb.qdl.entity.Setmeal;
import qdb.qdl.mapper.CategoryMapper;
import qdb.qdl.service.DishService;
import qdb.qdl.service.SetmealService;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> implements qdb.qdl.service.CategoryService {

    @Autowired
    private DishService dishService;
    private SetmealService setmealService;

  //根据id删除分类，删除之前需要进行判断
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count=dishService.count(dishLambdaQueryWrapper);

//查询当前分类是否关联菜品，如果关联，抛出一个业务异常
if(count>0){
    //已关联菜品，抛出一个业务异常
    throw new CustomException("当前分类项关联了菜品，不能删除");

}

        //查询当前分类是否关联套餐，如果关联，抛出一个业务异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
       int sconut= setmealService.count(setmealLambdaQueryWrapper);
       if(sconut>0){
           //已关联套餐，抛出一个业务异常
           throw new CustomException("当前分类项关联了套餐，不能删除");
       }
  //正常删除分类
        super.removeById(id);
    }
}
