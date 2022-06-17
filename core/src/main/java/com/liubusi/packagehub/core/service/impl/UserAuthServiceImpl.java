package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.core.mapper.UserMapper;
import com.liubusi.packagehub.core.pojo.entity.User;
import com.liubusi.packagehub.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
@Service
@Slf4j
public class UserAuthServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UserMapper userMapper;

    @Override
    public Page<User> selectPage(Integer current, Integer size) {
        Page<User> userPage = userMapper.selectPage(new Page<>(current, size), null);
        return userPage;
    }
}
