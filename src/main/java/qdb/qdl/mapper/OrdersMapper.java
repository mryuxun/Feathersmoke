package qdb.qdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import qdb.qdl.entity.Orders;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
