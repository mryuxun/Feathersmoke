package qdb.qdl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qdb.qdl.common.R;
import qdb.qdl.dto.SetmealDto;
import qdb.qdl.entity.Category;
import qdb.qdl.entity.Setmeal;
import qdb.qdl.entity.SetmealDish;
import qdb.qdl.service.CategoryService;
import qdb.qdl.service.SetmealDishService;
import qdb.qdl.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;

//套餐管理
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;

//新增套餐
    @PostMapping
    public R<String> sasve(@RequestBody SetmealDto setmealDto){

log.info("套餐信息：{}",setmealDto);

setmealService.saveWithDish(setmealDto);

return R.error("新增套餐成功");
    }


//套餐分页查询
    @GetMapping("/page")
public R<Page> page(int page,int pageSize,String name){
//分页构造器对象
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> droPage = new Page<>();
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();

        //添加查询条件，根据name,进行like模糊查询
        queryWrapper.like(name!=null,Setmeal::getName,name);

        //排序添加
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo,queryWrapper);

        //拷贝
        BeanUtils.copyProperties(pageInfo,droPage,"recorda");
        List<Setmeal> records= pageInfo.getRecords();

        List<SetmealDto> list= records.stream().map((item)->{
            SetmealDto setmealDto = new  SetmealDto();

            //拷贝
            BeanUtils.copyProperties(item,setmealDto);


            //分类ID
            Long categoryId = item.getCategoryId();


            //根据分类ID查询分类对象
          Category category= categoryService.getById(categoryId);
        if(category!=null){


    //分类名称
    String categoryName = category.getName();
            setmealDto.setCategoryName(categoryName);
}
return setmealDto;

        }).collect(Collectors.toList());
droPage.setRecords(list);
        return R.success(droPage);
}

//删除套餐

@DeleteMapping
public R<String> doiotc(@RequestParam List<Long> ids){
setmealService.removeWitbDish(ids);
        return R.success("套餐删除成功");
}



//根据条件查询套餐数据
@GetMapping("/list")
public R<List<Setmeal>> list( Setmeal setmeal){
LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
queryWrapper.eq(setmeal.getCategoryId() != null ,Setmeal::getCategoryId,setmeal.getCategoryId());
    queryWrapper.eq(setmeal.getStatus() != null ,Setmeal::getStatus,setmeal.getStatus());
    queryWrapper.orderByDesc(Setmeal::getUpdateTime);
   List<Setmeal> list= setmealService.list(queryWrapper);
   return R.success(list);

}

}
