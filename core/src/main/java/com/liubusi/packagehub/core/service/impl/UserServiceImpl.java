package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.common.exception.Assert;
import com.liubusi.packagehub.common.pojo.vo.UserAuthVO;
import com.liubusi.packagehub.common.pojo.vo.UserInfoVO;
import com.liubusi.packagehub.common.pojo.vo.UserVO;
import com.liubusi.packagehub.common.result.ResponseEnum;
import com.liubusi.packagehub.core.mapper.UserAuthMapper;
import com.liubusi.packagehub.core.mapper.UserMapper;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.core.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Override
    public Page<User> selectPage(Integer current, Integer size) {
        Page<User> userPage = userMapper.selectPage(new Page<>(current, size), null);
        return userPage;
    }

    @Override
    public void auth(UserAuthVO userAuthVO) {
        Long userId = userAuthVO.getUserId();
        List<Long> projectList = userAuthVO.getProjectList();
        List<Long> docList = userAuthVO.getDocList();
        userAuthMapper.removeAuthById(userId);
        if (projectList.size() == 0) {
            log.info("项目列表为空");
        } else {
            userAuthMapper.authProject(userId,projectList);
        }
        if (docList.size() == 0) {
            log.info("文档列表为空");
        } else {
            userAuthMapper.authDoc(userId,docList);
        }
    }

    @Override
    public String login(UserVO userVO) {
        String username = userVO.getUsername();
        String password = userVO.getPassword();
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("username", username);
        User user = userMapper.selectOne(userQueryWrapper);
        Assert.equals(user.getPassword(), password, ResponseEnum.LOGIN_PASSWORD_ERROR);
        String token = "usertoken-" + user.getId();
        return token;
    }

    @Override
    public UserInfoVO getInfo(String token) {
        Long id = Long.parseLong(token.substring(10));
        User user = userMapper.selectById(id);
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtils.copyProperties(user, userInfoVO);
        return userInfoVO;
    }
}
