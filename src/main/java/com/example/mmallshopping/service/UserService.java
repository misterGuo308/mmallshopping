package com.example.mmallshopping.service;

import com.example.mmallshopping.common.ServerResponse;

/**
 * @author guoyou
 * @date 2019/10/23 16:25
 */
public interface UserService {
    ServerResponse login(String username, String password);
}
