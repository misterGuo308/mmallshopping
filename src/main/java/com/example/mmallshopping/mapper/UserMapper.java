package com.example.mmallshopping.mapper;

import com.example.mmallshopping.pojo.User;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int getCheckUsername(String username);

    User selectBbyLogin(String username, String password);
}