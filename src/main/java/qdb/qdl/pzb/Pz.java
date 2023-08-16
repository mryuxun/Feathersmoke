package qdb.qdl.pzb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.cbor.MappingJackson2CborHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import qdb.qdl.common.JacksonObjectMapper;
import qdb.qdl.common.R;
import qdb.qdl.entity.Employee;
import qdb.qdl.service.EmployeeService;

import java.util.List;

@Slf4j
@Configuration             //声明配置类
public class Pz extends WebMvcConfigurationSupport {
    //设置前端资源映射
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry){
        log.info("开始进行前端资源映射");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
    }

    //扩展mvc框架的消息转换器
    @Override
    protected void extendMessageConverters(List<HttpMessageConverter<?>> converters){
//创建信息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
         //设置对象转换器，底层使用jackson对象转为json
        messageConverter.setObjectMapper(new JacksonObjectMapper());
       //将上面的消息转换器追加到mvc框架的转换器集合中
        converters.add(0, messageConverter);
    }

}
