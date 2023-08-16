package qdb.qdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import qdb.qdl.entity.Orders;



public interface OrderService extends IService<Orders> {
    //用户下单
    public void submit(Orders orders);
}
