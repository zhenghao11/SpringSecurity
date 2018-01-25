package com.my.security.mapper;

import com.my.security.dataobject.User;

/**
 * @Author hzheng
 * @Date 2017/12/13
 */
public interface UserMapper {
    User login(User user);

    int updateByParams(User user);

    User selectByUsername(String username);

    int add(User user);

}
