package com.mapper;

import com.entity.User;

/**
 * Created by admin on 2018/7/15.
 */
public interface UserMapper {
    User queryUserByUsername(String username);
}
