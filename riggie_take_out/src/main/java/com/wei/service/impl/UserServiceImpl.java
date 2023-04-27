package com.wei.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.wei.entity.User;
import com.wei.mapper.UserMapper;
import com.wei.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
