package qdb.qdl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import qdb.qdl.common.BaaseContext;
import qdb.qdl.common.R;
import qdb.qdl.entity.ShoppingCart;
import qdb.qdl.service.ShoppingCartService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartContoller {

    @Autowired
private ShoppingCartService shoppingCartService;


    //添加购物车
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){


        //设置用户ID，指定当前是哪个用户的购物车数据
        Long currentId=BaaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);



        Long dishid=shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);

    if( dishid !=null){
            //添加到购物车
            queryWrapper.eq(ShoppingCart::getDishId,dishid);
        }else {
            //添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询当前菜品或套餐是否在购物车中
        ShoppingCart shoppingCart1=  shoppingCartService.getOne(queryWrapper);
        if(shoppingCart1 !=null){
            //如果已经存在就在原来的数量基础三加一
            Integer number =  shoppingCart1.getNumber();
            shoppingCart1.setNumber(number+1);
            shoppingCartService.updateById(shoppingCart1);
        }else{
            //如果不存在，则添加购物车，数量默认为一
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            shoppingCart1 =shoppingCart;
        }
        return R.success(shoppingCart1);
    }
    //查看购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
queryWrapper.eq(ShoppingCart::getUserId,BaaseContext.getCurrentId());
queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
List<ShoppingCart>list= shoppingCartService.list(queryWrapper);
return R.success(list);
    }
    //清空购物车
    @DeleteMapping("/clean")
    public R<String> clean(){
LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>( );
queryWrapper.eq(ShoppingCart::getUserId,BaaseContext.getCurrentId());
shoppingCartService.remove(queryWrapper);
return R.success("清空购物车成功");
    }
}
