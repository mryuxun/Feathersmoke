package qdb.qdl.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qdb.qdl.service.OrderDetailService;

@Slf4j
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailControiier {

    @Autowired
    private OrderDetailService orderDetailService;
}
