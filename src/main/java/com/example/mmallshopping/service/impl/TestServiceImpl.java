package com.example.mmallshopping.service.impl;

import com.example.mmallshopping.mapper.UserMapper;
import com.example.mmallshopping.pojo.User;
import com.example.mmallshopping.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author guoyou
 * @date 2019/10/22 14:49
 */
@Service
public class TestServiceImpl implements TestService {

    @Resource
    private UserMapper userMapper;

    @Override
    public String test() {
        User user = userMapper.selectByPrimaryKey(1);
        System.out.println(user.getUsername());
        return user.getUsername();
    }
}
