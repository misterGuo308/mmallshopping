package com.example.mmallshopping.controller.backend;

import com.example.mmallshopping.common.Const;
import com.example.mmallshopping.common.ServerResponse;
import com.example.mmallshopping.pojo.User;
import com.example.mmallshopping.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/user")
public class ManageUserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    public ServerResponse<User> login(String username, String password, HttpSession session) {
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            if (response.getData().getRole() == Const.role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, response.getData());
            } else {
                return ServerResponse.createByErrorMessage("该用户不是管理员,无法登录");
            }
        }
        return response;
    }
}
