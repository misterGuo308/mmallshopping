package com.example.mmallshopping.service;

import com.example.mmallshopping.common.ServerResponse;
import com.example.mmallshopping.pojo.User;

/**
 * @author guoyou
 * @date 2019/10/23 16:25
 */
public interface UserService {
    ServerResponse login(String username, String password);

    ServerResponse<String> register(User user);

    ServerResponse<String> checkValid(String str, String type);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation(User user);

    ServerResponse<User> getInformation(Integer id);
}
