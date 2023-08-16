package qdb.qdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import qdb.qdl.dto.SetmealDto;
import qdb.qdl.entity.Setmeal;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {

    //新增菜品，同时需要保存和菜品的关联关系

    public void saveWithDish(SetmealDto setmealDto);
    //删除套餐，同时需要删除套餐和菜品的关联数据

    public void removeWitbDish(List<Long> ide);

}
