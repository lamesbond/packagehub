package com.liubusi.packagehub.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liubusi.packagehub.common.pojo.entity.Project;
import com.liubusi.packagehub.common.pojo.vo.ProjectVO;

import java.util.List;

/**
 * <p>
 * 数据字典 Mapper 接口
 * </p>
 *
 * @author liubusi
 * @since 2022-05-21
 */
public interface ProjectMapper extends BaseMapper<Project> {

    List<Project> listNextChildNode(Long id, Long userId);

    List<ProjectVO> listParentNode(Long id);

    List<ProjectVO> listAllChildNode(Long id);

    void saveFile(Long id, String name, Long parentId, String type, String url);

    void saveCategory(Long id, String title, Long parentId, String department, String description, String pubStatus);

    void remove(Long id);

}
