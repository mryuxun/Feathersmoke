package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qdb.qdl.entity.Employee;
import qdb.qdl.mapper.EnployeeMapper;
import qdb.qdl.service.EmployeeService;

@Service
public class EmployeeServiceimpl extends ServiceImpl<EnployeeMapper,Employee> implements EmployeeService {

}
