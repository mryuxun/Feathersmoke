package qdb.qdl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import qdb.qdl.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
