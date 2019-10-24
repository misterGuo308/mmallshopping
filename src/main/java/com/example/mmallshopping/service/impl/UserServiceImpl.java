package com.example.mmallshopping.service.impl;

import com.example.mmallshopping.common.Const;
import com.example.mmallshopping.common.ServerResponse;
import com.example.mmallshopping.common.TokenCache;
import com.example.mmallshopping.mapper.UserMapper;
import com.example.mmallshopping.pojo.User;
import com.example.mmallshopping.service.UserService;
import com.example.mmallshopping.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

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
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        //MD5
        String md5Encode = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectBbyLogin(username, md5Encode);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户密码不正确");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySuccess("登录成功", user);
    }

    @Override
    public ServerResponse<String> addRegister(User user) {
        ServerResponse<String> validResponse = this.checkValid(user.getUsername(), Const.USERNAME);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        validResponse = this.checkValid(user.getEmail(), Const.EMAIL);
        if (!validResponse.isSuccess()) {
            return validResponse;
        }
        user.setRole(Const.role.ROLE_CUSTOMER);
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        return userMapper.insertSelective(user) > 0 ? ServerResponse.createBySuccessMessage("用户注册成功") : ServerResponse.createByErrorMessage("用户注册失败");
    }

    @Override
    public ServerResponse<String> checkValid(String str, String type) {
        if (StringUtils.isBlank(type)) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        if (Const.USERNAME.equals(type)) {
            int resultCount = userMapper.checkUsername(str);
            if (resultCount > 0) {
          return  ServerResponse.createByErrorMessage("用户已存在");
            }
        }
        if (Const.EMAIL.equals(type)) {
            int resultCount = userMapper.checkEmail(str);
            if (resultCount > 0) {
                return ServerResponse.createByErrorMessage("邮箱已存在");
            }
        }
        return ServerResponse.createBySuccessMessage("验证成功");
    }

    @Override
    public ServerResponse<String> selectQuestion(String username) {
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isNoneBlank(question)) {
            return ServerResponse.createBySuccess(question);
        }
        return ServerResponse.createByErrorMessage("密保问题不存在");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        int resultRequest = userMapper.checkAnswer(username, question, answer);
        if (resultRequest > 0) {
            String token = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, token);
            return ServerResponse.createBySuccess(token);
        }
        return ServerResponse.createByErrorMessage("答案不正确");
    }

    @Override
    public ServerResponse<String> updatePassword(String username, String passwordNew, String forgetToken) {
        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误,token需要传递");
        }
        ServerResponse<String> validResponse = this.checkValid(username, Const.USERNAME);
        if (validResponse.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (StringUtils.equals(token, forgetToken)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int result = userMapper.updatePasswordByUsername(username, md5Password);
            if (result > 0) {
                return ServerResponse.createBySuccessMessage("密码重置成功");
            }
        } else {
            return ServerResponse.createByErrorMessage("token失效,请重新回答密保问题");
        }
        return ServerResponse.createByErrorMessage("密码重置失败");
    }

    @Override
    public ServerResponse<String> updatePassword(String passwordOld, String passwordNew, User user) {
        int result = userMapper.checkPassword(user.getId(), MD5Util.MD5EncodeUtf8(passwordOld));
        if (result == 0) {
            return ServerResponse.createByErrorMessage("旧密码不正确");
        }
        result = userMapper.updatePasswordByUsername(user.getUsername(), MD5Util.MD5EncodeUtf8(passwordNew));
        return result > 0 ? ServerResponse.createBySuccessMessage("密码修改成功") : ServerResponse.createByErrorMessage("密码修改失败");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //保证每一个用户的邮箱唯一
        int result = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if (result > 0) {
            return ServerResponse.createByErrorMessage("邮箱已经存在");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());
        result = userMapper.updateByPrimaryKeySelective(updateUser);
        return result > 0 ? ServerResponse.createBySuccessMessage("用户信息修改成功") : ServerResponse.createByErrorMessage("用户信息修改失败");
    }

    @Override
    public ServerResponse<User> getInformation(Integer id) {
        User user = userMapper.selectByPrimaryKey(id);
        if (user != null) {
            user.setPassword(StringUtils.EMPTY);
            ServerResponse.createBySuccess(user);
        }
        return ServerResponse.createByErrorMessage("获取用户信息失败");
    }
}
