package com.liubusi.packagehub.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.common.pojo.entity.User;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface UserService extends IService<User> {

    Page<User> login(Integer current, Integer size);
}
