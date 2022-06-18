package com.liubusi.packagehub.core.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liubusi.packagehub.core.mapper.UserAuthMapper;
import com.liubusi.packagehub.core.mapper.UserMapper;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.common.pojo.vo.UserAuthVO;
import com.liubusi.packagehub.core.service.UserManageService;
import lombok.extern.slf4j.Slf4j;
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
public class UserManageServiceImpl extends ServiceImpl<UserMapper, User> implements UserManageService {

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
}
