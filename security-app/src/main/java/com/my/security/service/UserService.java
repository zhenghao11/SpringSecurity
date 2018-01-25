package com.my.security.service;

import com.my.security.dataobject.User;
import com.my.security.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author hzheng
 * @Date 2017/12/13
 */
@Service
public class UserService implements TestDefaultInterface{
    @Autowired
    private UserMapper userMapper;

    public User selectByUsername(String username){
        return userMapper.selectByUsername(username);
    }

    public int addUser(User user) {
        return userMapper.add(user);
    }
}
