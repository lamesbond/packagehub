package com.liubusi.packagehub.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.common.pojo.entity.User;
import com.liubusi.packagehub.common.pojo.vo.UserAuthVO;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface UserManageService extends IService<User> {

    Page<User> selectPage(Integer current, Integer size);

    void auth(UserAuthVO userAuthVO);
}
