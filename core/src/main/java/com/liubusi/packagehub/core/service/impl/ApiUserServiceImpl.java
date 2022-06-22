package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.common.exception.Assert;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.common.pojo.vo.UserVO;
import com.liubusi.packagehub.common.result.ResponseEnum;
import com.liubusi.packagehub.common.util.JwtUtils;
import com.liubusi.packagehub.common.util.MD5;
import com.liubusi.packagehub.core.mapper.UserMapper;
import com.liubusi.packagehub.core.service.ApiUserService;
import lombok.extern.slf4j.Slf4j;
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
public class ApiUserServiceImpl extends ServiceImpl<UserMapper, User> implements ApiUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void register(UserVO userVO) {
        //判断用户是否被注册
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userVO.getEmail());
        Integer count = baseMapper.selectCount(queryWrapper);
        //EMAIL_EXIST_ERROR(-207, "邮箱已被注册"),
        Assert.isTrue(count > 0, ResponseEnum.EMAIL_EXIST_ERROR);

        //插入用户基本信息
        User user = new User();
        user.setUsername(userVO.getEmail());
        user.setEmail(userVO.getEmail());
        user.setPassword(MD5.encrypt(userVO.getPassword()));
        user.setStatus(userVO.STATUS_NORMAL); //正常
        //设置一张静态资源服务器上的头像图片
        user.setAvatar("https://liubusi-shangrongbao.oss-cn-hangzhou.aliyuncs.com/ellefanning.jpeg");
        baseMapper.insert(user);
    }

    @Override
    public UserVO login(UserVO userVO, String ip) {
        String email = userVO.getEmail();
        String password = userVO.getPassword();

        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        User user = baseMapper.selectOne(userQueryWrapper);
        Assert.notNull(user, ResponseEnum.LOGIN_EMAIL_ERROR);

        Assert.equals(MD5.encrypt(password), user.getPassword(), ResponseEnum.LOGIN_PASSWORD_ERROR);

        Assert.equals(user.getStatus(), user.STATUS_NORMAL, ResponseEnum.LOGIN_LOKED_ERROR);

        //生成token
        String token = JwtUtils.createToken(user.getId(), user.getUsername());
        UserVO userVO1 = new UserVO();
        userVO1.setId(user.getId());
        userVO1.setToken(token);
        userVO1.setUsername(user.getUsername());
        userVO1.setAvatar(user.getAvatar());
        userVO1.setEmail(user.getEmail());

        return userVO1;
    }

    @Override
    public boolean checkEmail(String email) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("email", email);
        Integer count = baseMapper.selectCount(userQueryWrapper);
        return count > 0;
    }
}
