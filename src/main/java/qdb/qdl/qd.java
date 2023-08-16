package qdb.qdl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j //省略get,set方法
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class qd {
    public static void main(String[] args){
        SpringApplication.run(qd.class,args);
        log.info("项目启动成功");//日志
    }
}
