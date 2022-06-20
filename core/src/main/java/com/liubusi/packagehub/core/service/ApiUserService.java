package com.liubusi.packagehub.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.common.pojo.vo.UserAuthVO;
import com.liubusi.packagehub.common.pojo.vo.UserInfoVO;
import com.liubusi.packagehub.common.pojo.vo.UserVO;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface ApiUserService extends IService<User> {

    void register(UserVO userVO);

    UserVO login(UserVO userVO, String ip);

    boolean checkEmail(String email);
}
