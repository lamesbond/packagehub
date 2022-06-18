package com.liubusi.packagehub.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.common.pojo.entity.UserAuth;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    void removeAuthById(Long userId);

    void authProject(Long userId, List<Long> projectList);

    void authDoc(Long userId, List<Long> docList);

}
