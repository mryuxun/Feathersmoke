package qdb.qdl.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qdb.qdl.common.R;
import qdb.qdl.entity.User;
import qdb.qdl.service.UserService;
import qdb.qdl.utils.SMSUtils;
import qdb.qdl.utils.ValidateCodeUtils;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    //手机短信验证码
    @PostMapping("/sendMsg")
    public R<String> sendMes(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){

            //生成随机的4位验证码
            String yzm=ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("验证码为：{}",yzm);
            //调用阿里云短信服务
         //   SMSUtils.sendMessage("瑞吉外卖","瑞吉外卖",phone,yzm);
            //保存验证码到session
            session.setAttribute(phone,yzm);
            return R.success("手机验证码发送成功");
        }
        return R.success("手机验证码发送失败");

    }


    //移动端用户登入
    @PostMapping("/login")
    public R<User> login(@RequestBody Map user, HttpSession session){
        //获取手机号
        String phone = user.get("phone").toString();
        //获取验证码
        String code =user.get("code").toString();
        //获取session中的验证码
        Object codoInSession=session.getAttribute(phone);
        //比对验证码
        if(codoInSession !=null && codoInSession.equals(code)){
            //比对成功，登入成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user1=  userService.getOne(queryWrapper);
            if(user1 == null){
                user1 = new User();
                user1.setPhone(phone);
                user1.setStatus(1);
                userService.save(user1);
            }
session.setAttribute("user",user1.getId());
return R.success(user1);
        }
        //判断当前手机号对应的用户是否为新用户，如果是自动完成注册
        return R.error("登入失败");
    }
}
