package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qdb.qdl.common.CustomException;
import qdb.qdl.dto.SetmealDto;
import qdb.qdl.entity.Setmeal;
import qdb.qdl.entity.SetmealDish;
import qdb.qdl.mapper.SetmealMapper;
import qdb.qdl.service.SetmealDishService;
import qdb.qdl.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper,Setmeal>  implements SetmealService {

@Autowired
private SetmealDishService setmealDishService;

   @Transactional
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
//保存套餐基本信息，操作setmeal,执行insert操作

this.save(setmealDto);
       List<SetmealDish> setmealDishes= setmealDto.getSetmealDishes();
       setmealDishes.stream().map((item)->{
           item.setSetmealId(setmealDto.getId());
           return item;
       }).collect(Collectors.toList());

       //保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作
setmealDishService.saveBatch(setmealDishes);


    }
//删除套餐，同时需要删除套餐和菜品的关联数据
    @Transactional
    @Override
    public void removeWitbDish( List<Long> ids) {
      // select count(*) from setmal where id in(1,2,3) and status =1

//查询套餐状态，确定是否可以删除

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
queryWrapper.in(Setmeal::getId,ids);
queryWrapper.in(Setmeal::getStatus,1);
int coun = this.count(queryWrapper);
if(coun>0){
    //如果不可以删除，抛出一个业务异常
    throw  new CustomException("套餐正在售卖中，不能删除");
}


        //如果可以删除，先删除套餐表中的数据--setmal

        this.removeByIds(ids);

        //删除关系表中的数据--setmal_dish

        LambdaQueryWrapper<SetmealDish> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper2);

    }
}
