package qdb.qdl.pzb;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//配置MP的分页插件
@Configuration    //声明配置类
public class MybatisPlusConfig {

    //申明拦截器
    @Bean   //让spring管理
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor(); //创建拦截器对象
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor()); //加入拦截器
        return  mybatisPlusInterceptor;
    }
}
