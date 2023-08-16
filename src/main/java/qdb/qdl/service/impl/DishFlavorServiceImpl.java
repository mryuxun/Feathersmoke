package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qdb.qdl.entity.DishFlavor;
import qdb.qdl.mapper.DishFlavorMapper;
import qdb.qdl.service.DishFlavorService;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper,DishFlavor> implements DishFlavorService {
}
