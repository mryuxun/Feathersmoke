package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qdb.qdl.entity.ShoppingCart;
import qdb.qdl.mapper.ShoppingCartMapper;
import qdb.qdl.service.ShoppingCartService;


@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
