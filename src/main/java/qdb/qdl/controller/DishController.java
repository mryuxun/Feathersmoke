package qdb.qdl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import qdb.qdl.common.R;
import qdb.qdl.dto.DishDto;
import qdb.qdl.entity.Category;
import qdb.qdl.entity.Dish;
import qdb.qdl.entity.DishFlavor;
import qdb.qdl.service.CategoryService;
import qdb.qdl.service.DishFlavorService;
import qdb.qdl.service.DishService;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//菜品管理
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisTemplate redisTemplate;

//新增菜品
    @PostMapping
public R<String> save(@RequestBody DishDto dishDto){
dishService.saveWithFlavor( dishDto);
    return R.error("新增菜品成功");
}

//菜品信息分页查询

@GetMapping("/page")
public R<Page> page(int page,int pageSize,String name){
     //构造分页构造器
Page<Dish> pageInfo = new Page<>(page,pageSize);
    Page<DishDto> dishDtoPage = new Page<>();

//条件构造器
    LambdaQueryWrapper<Dish> queryWrapper = new  LambdaQueryWrapper<>();
    //添加过滤条件
    queryWrapper.like(name != null,Dish::getName, name);
//添加排序条件
    queryWrapper.orderByDesc(Dish::getUpdateTime);
    //执行分页查询
    dishService.page(pageInfo,queryWrapper);
    //对象拷贝
    BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

    List<Dish> records = pageInfo.getRecords();

    List<DishDto> list =records.stream().map((item)->{
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(item,dishDto);
        Long categoryId = item.getCategoryId(); //分类ID
        //根据id查询分类对象
       Category category = categoryService.getById(categoryId);

       if(category != null){

           String categoryNome = category.getName();
           dishDto.setCategoryName(categoryNome);
       }

       return dishDto;
    }).collect(Collectors.toList());


dishDtoPage.setRecords(list);

return R.success(dishDtoPage);

}

//根据ID查询菜品信息和对应的口味信息
@GetMapping("/{id}")
public R<DishDto> get(@PathVariable Long id){
DishDto dishDto =dishService.getByIdWithFlaveor(id);
return R.success(dishDto);
}


    //修改菜品
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor( dishDto);

        //清理菜品所有数据
     //   Set keys=redisTemplate.keys("dish_*");
     //   redisTemplate.delete(keys);
//清理某一个分类下面的菜品缓存
        String key="dish"+dishDto.getCategoryId()+" 1";
        redisTemplate.delete(key);
        return R.error("修改菜品成功");

    }


    //根据条件查询对应的菜品s数据
    @GetMapping("/list")
public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList =null;

                String key= "dish"+dish.getCategoryId()+" "+dish.getStatus(); //dish

//先从redls中获取缓存数据
        Object xx = redisTemplate.opsForValue().get(key);

        //如果存在直接在redls中获取缓存数据
        dishDtoList=( List<DishDto>)redisTemplate.opsForValue().get(key);
if (dishDtoList !=null){
    return R.success(dishDtoList);
}

//构造查询条件对象
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
      //查询状态为1的
        queryWrapper.eq(Dish::getStatus,1);
        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish>  list=  dishService.list(queryWrapper);



        dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId(); //分类ID
            //根据id查询分类对象
            Category category = categoryService.getById(categoryId);

            if(category != null){
                String categoryNome = category.getName();
                dishDto.setCategoryName(categoryNome);
            }
            //当前菜品ID
             Long dishId=item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
               queryWrapper1.eq(DishFlavor::getDishId,dishId);
           List<DishFlavor> dishFlavorList =   dishFlavorService.list(queryWrapper1);
           dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        //如果不存在，添加上数据再调取
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);


        return R.success(dishDtoList);
}
}
