package com.liubusi.packagehub.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.core.pojo.entity.User;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface UserAuthService extends IService<User> {

    Page<User> selectPage(Integer current, Integer size);
}
