package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qdb.qdl.entity.OrderDetail;
import qdb.qdl.mapper.OrderDetailMapper;
import qdb.qdl.service.OrderDetailService;

@Service
public class OrderDetailServicelmpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
