package com.project001.service.impl;

import com.project001.entity.User;
import com.project001.mapper.UserMapper;
import com.project001.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lynn
 * @since 2022-07-04
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
