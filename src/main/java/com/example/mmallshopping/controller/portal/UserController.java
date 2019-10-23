package com.example.mmallshopping.controller.portal;

import com.example.mmallshopping.common.Const;
import com.example.mmallshopping.common.ServerResponse;
import com.example.mmallshopping.pojo.User;
import com.example.mmallshopping.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author guoyou
 * @date 2019/10/23 15:45
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @PostMapping("/login")
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse response = userService.login(username, password);
        if (response.isSuccess()) {
            session.setAttribute(Const.CURRENT_USER, response.getData());
        }
        return response;
    }


}
