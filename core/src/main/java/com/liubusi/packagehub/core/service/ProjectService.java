package com.liubusi.packagehub.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface ProjectService extends IService<Project> {

    List<ProjectVO> listNextChildNode(Long id, Long userId);

    String listParentNode(Long id);

    List<ProjectVO> listAllChildNode(Long id);

    void save(ProjectVO projectVO);

    void remove(Long id);

}
