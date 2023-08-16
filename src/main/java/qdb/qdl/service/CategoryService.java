package qdb.qdl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import qdb.qdl.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
