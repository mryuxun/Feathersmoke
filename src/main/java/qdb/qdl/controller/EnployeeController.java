package qdb.qdl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import qdb.qdl.common.R;
import qdb.qdl.entity.Employee;
import qdb.qdl.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EnployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
      //将页面提交的密码a进行md5加密处理
        String a=employee.getPassword();
        a= DigestUtils.md5DigestAsHex(a.getBytes());
//根据页面提交的用户名Username查询数据库
        LambdaQueryWrapper<Employee> JH = new  LambdaQueryWrapper<>();
        JH.eq(Employee::getUsername,employee.getUsername());
        Employee em=employeeService.getOne(JH);
        //如果没有查询到则返回登入失败结果
        if(em==null){
            return R.error("登入失败");
        }
                //密码比对如果不一致则返回登入失败结果
        if(!em.getPassword().equals(a)){
            return R.error("登入失败");
        }
        //查看员工状态，如果为已禁用状态，则返回账号已禁用
        if(em.getStatus()==0){
            return R.error("账号已禁用");
        }
        //登入成功，将员工id存入success并返回结果
        request.getSession().setAttribute("employee",em.getId());
        return R.success(em)  ;
    }



    //员工退出方法
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Seeion中保存的当前登入的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    //新增员工方法
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody  Employee employee){
log.info("新增员工信息：{}",employee.toString());
//设置初始密码，并md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("12345".getBytes()));

       // employee.setCreateTime(LocalDateTime.now()) ;
       // employee.setUpdateTime(LocalDateTime.now());

        //获得当前登入用户Id
     //  Long id= (Long) request.getSession().getAttribute("employee");
    //    employee.setCreateUser(id);
     //   employee.setUpdateUser(id);
        employeeService.save(employee);
                return R.success("新增员工成功");
    }

    //分页查询方法
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){    //page是从第几页开始查，pageSize查几页 ，name是查询条件
        log.info("page = {},pageSize = {} ,name = {}",page,pageSize,name);

        //分页构造器

        Page pageInfo =new Page(page, pageSize);

        //条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        if(name !=null){
            queryWrapper.like(Employee::getName,name);
        }
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);
        //执行查询
        return R.success(pageInfo);
    }


//根据ID修改员工信息
@PutMapping
public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
   // long l=(long)request.getSession().getAttribute("employee");
  //  employee.setUpdateTime(LocalDateTime.now());
   // employee.setUpdateUser(l);
    employeeService.updateById(employee);
    return R.success("员工信息修改成功");

}
//根据id查询员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("执行了查询");
        Employee employee = employeeService.getById(id);

if(employee != null){
    log.info("有数据");
    return R.success(employee);
}
return  R.error("没有查询到员工信息");

    }
}
