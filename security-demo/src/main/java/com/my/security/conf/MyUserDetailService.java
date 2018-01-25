package com.my.security.conf;

import com.my.security.dataobject.User;
import com.my.security.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


/**
 * @Author hzheng
 * @Date 2017/10/25
 */
@Component
public class MyUserDetailService implements UserDetailsService {
/*    @Autowired
    PasswordEncoder passwordEncoder;*/
    @Autowired
    UserService userService;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isBlank(username)) throw new RuntimeException("用户名不能为空");
        User user = userService.selectByUsername(username);
        if (null == user) throw new RuntimeException("用户名不存在");
        return new User(user);
    }
}
