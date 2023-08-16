package qdb.qdl.filter;

//检查用户是否完成登录

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import qdb.qdl.common.BaaseContext;
import qdb.qdl.common.R;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "glq",urlPatterns ="/*")      //声明过滤器，设置过滤器名称，要拦截那个路径
@Slf4j
public class LoginCheckFiler implements Filter {
    //路径匹配器，支持通配符
    public static  final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request =(HttpServletRequest) servletRequest;
        HttpServletResponse response =(  HttpServletResponse) servletResponse;

        //获取本次请求的URI
        String requestURI =request.getRequestURI();

        log.info("拦截到：{}",requestURI );

        //设置要排除的URI
             String[] pc = new String[]{
                 "/employee/login",   //排除登入时发送的请求
                     "/employee/loginout", //排除退出时发送的请求
                     "/backend/**",          //排除所有要请求的图片静态资源
                     "/front/**"  ,          //排除所有移动端要请求的图片静态资源
                     "/common/**" ,
                     "user/sendMsg",
                     "user/login",
                     "/user/**" ,             //移动端发送短信
                     "/user/**"              //移动端登入
             };


             //判断本次请求需不需要处理
        boolean jg= check(pc,requestURI);
        //如果不需要处理直接放行
        if(jg){
            log.info("拦截到：{} ,不需要处理",requestURI );
            filterChain.doFilter(request,response);
            return;
        }
        //4-1，判断登入状态，如果已登入，则放行
       if( request.getSession().getAttribute("employee") != null){
           log.info("用户已登录id为：{}" , request.getSession().getAttribute("employee"));

           Long empId = (Long) request.getSession().getAttribute("employee");

           BaaseContext.setCurrentId(empId);


           filterChain.doFilter(request,response);
           return;
       }



        //4-2，判断登入状态，如果已登入，则放行
        if( request.getSession().getAttribute("user") != null){
            log.info("用户已登录id为：{}" , request.getSession().getAttribute("user"));

            Long userId = (Long) request.getSession().getAttribute("user");

            BaaseContext.setCurrentId(userId);


            filterChain.doFilter(request,response);
            return;
        }



        log.info("用户未登录");
       //如果没有登入则返回未登入结果，通过输出流的发式向客户端响应数据

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

        return;
    }

    //路径匹配的方法
    public boolean check(String[] pc,String requestURI){
        for (String s : pc) {
           boolean jg = PATH_MATCHER.match(s,requestURI);
           if (jg){
               return true;
           }
        }
        return false;
    }
}
