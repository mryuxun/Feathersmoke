package qdb.qdl.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理
@ControllerAdvice(annotations ={RestController.class, Controller.class}) //拦截加了RestController 和 Controller 注解的类
@ResponseBody
@Slf4j
public class GlobalEsceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class) //要处理的异常类型为SQLIntegrityConstraintViolationException一旦触发这个异常就会执行下面的方法
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException yc){
        log.info(yc.getMessage());

        if (yc.getMessage().contains("Duplicate entry")){
           String[] sz= yc.getMessage().split(" ");
           String msg=sz[2]+"已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }


    @ExceptionHandler(CustomException.class) //要处理的异常类型为CustomException一旦触发这个异常就会执行下面的方法
    public R<String> exceptionHandler(CustomException yc){
        log.info(yc.getMessage());

        return R.error(yc.getMessage());
    }
}
