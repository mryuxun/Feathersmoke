package qdb.qdl.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import qdb.qdl.entity.AddressBook;
import qdb.qdl.mapper.AddressBookMapper;
import qdb.qdl.service.AddressBookService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
