package com.example.mmallshopping.service.impl;

import com.example.mmallshopping.common.ServerResponse;
import com.example.mmallshopping.mapper.UserMapper;
import com.example.mmallshopping.pojo.User;
import com.example.mmallshopping.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author guoyou
 * @date 2019/10/23 16:26
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public ServerResponse login(String username, String password) {
        if (userMapper.getCheckUsername(username) == 0) {
            return ServerResponse.createByErrorMessage("用户名称不存在");
        }
        //MD5
        User user = userMapper.selectBbyLogin(username, password);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }
}
