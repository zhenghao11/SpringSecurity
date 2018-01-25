package com.my.security.controller;

import com.my.security.dataobject.User;
import com.my.security.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @Author hzheng
 * @Date 2017/11/15
 */
@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @RequestMapping(value = "/session/timeout")
    public Object timeout(){
        System.out.println(111);
        return "登录超时";
    }

    /**
     * 用于测试
     * @return
     */
    @RequestMapping("/test")
    public Object test(Authentication authentication,HttpServletRequest request) throws UnsupportedEncodingException {
        String header = request.getHeader("Authorization");
        // 获取token
        String token = StringUtils.substringAfter(header, "bearer ");
        Claims claims = Jwts.parser().setSigningKey("xingeKey".getBytes("UTF-8")).parseClaimsJws(token).getBody();
     //   String company = (String) claims.get("company");
     //   System.out.println(company);
        System.out.println(claims);
        return authentication;
    }

    @RequestMapping(value = "/testLogin")
    public void testLogin(){

    }

    @RequestMapping(value = "/json")
    public Object json(){
        return "json";
    }

    /**
     * 测试post
     * @param user
     * @return
     */
    @RequestMapping(value = "/saveUser",method = RequestMethod.POST)
    public Object saveUser(User user) {
        Assert.notNull(user.getUsername(),"username can not be null");
        Assert.notNull(user.getPassword(),"password can not be null");
        int res = userService.addUser(user);
        if (res > 0)
            return "ok";
        else
            return "fail";
    }
}
